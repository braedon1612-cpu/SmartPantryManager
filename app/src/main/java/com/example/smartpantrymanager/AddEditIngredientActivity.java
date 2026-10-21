package com.example.smartpantrymanager;

import android.database.SQLException;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smartpantrymanager.database.PantryDatabaseHelper;
import com.example.smartpantrymanager.database.PantryRepository;
import com.example.smartpantrymanager.model.Ingredient;
import com.example.smartpantrymanager.ui.ScreenInsets;

public class AddEditIngredientActivity extends AppCompatActivity {
    public static final String EXTRA_INGREDIENT_ID = "ingredient_id";
    private PantryDatabaseHelper helper;
    private PantryRepository repository;
    private EditText nameInput, quantityInput;
    private Spinner unitInput;
    private long ingredientId;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit);
        ScreenInsets.apply(findViewById(R.id.main));
        helper = new PantryDatabaseHelper(this);
        repository = new PantryRepository(helper);
        nameInput = findViewById(R.id.nameInput);
        quantityInput = findViewById(R.id.quantityInput);
        unitInput = findViewById(R.id.unitInput);
        ingredientId = getIntent().getLongExtra(EXTRA_INGREDIENT_ID, -1);
        if (ingredientId <= 0 && state == null) {
            String preferred = getSharedPreferences(SettingsActivity.PREFERENCES, MODE_PRIVATE)
                    .getString(SettingsActivity.DEFAULT_UNIT, "g");
            String[] options = getResources().getStringArray(R.array.pantry_units);
            for (int index = 0; index < options.length; index++) {
                if (options[index].equals(preferred)) unitInput.setSelection(index);
            }
        }
        findViewById(R.id.backToPantry).setOnClickListener(v -> finish());
        findViewById(R.id.saveIngredient).setOnClickListener(v -> save());
        if (ingredientId > 0) {
            ((TextView) findViewById(R.id.formTitle)).setText(R.string.edit_title);
            // Android restores unsaved fields on rotation; load database values only once.
            if (state == null) loadIngredient();
        }
    }

    private void loadIngredient() {
        try {
            Ingredient item = repository.getIngredient(ingredientId);
            if (item == null) {
                Toast.makeText(this, R.string.item_missing, Toast.LENGTH_LONG).show();
                finish();
                return;
            }
            nameInput.setText(item.getName());
            quantityInput.setText(java.math.BigDecimal.valueOf(item.getQuantity())
                    .stripTrailingZeros().toPlainString());
            String[] units = getResources().getStringArray(R.array.pantry_units);
            for (int index = 0; index < units.length; index++) {
                if (units[index].equals(item.getUnit())) unitInput.setSelection(index);
            }
        } catch (SQLException exception) {
            Toast.makeText(this, R.string.storage_error, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void save() {
        nameInput.setError(null);
        quantityInput.setError(null);
        if (nameInput.getText().toString().trim().isEmpty()) {
            nameInput.setError(getString(R.string.name_label));
            nameInput.requestFocus();
            return;
        }
        double quantity;
        try {
            quantity = Double.parseDouble(quantityInput.getText().toString().trim());
            if (Double.isNaN(quantity) || Double.isInfinite(quantity) || quantity < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException exception) {
            quantityInput.setError(getString(R.string.number_error));
            quantityInput.requestFocus();
            return;
        }
        try {
            String name = nameInput.getText().toString();
            String unit = unitInput.getSelectedItem().toString();
            if (ingredientId > 0) {
                if (!repository.updateIngredient(ingredientId, name, quantity, unit)) {
                    Toast.makeText(this, R.string.item_missing, Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
            } else {
                repository.addIngredient(name, quantity, unit);
            }
            Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
            finish();
        } catch (IllegalArgumentException exception) {
            nameInput.setError(exception.getMessage());
            nameInput.requestFocus();
        } catch (SQLException exception) {
            Toast.makeText(this, R.string.storage_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override protected void onDestroy() {
        if (helper != null) helper.close();
        super.onDestroy();
    }
}
