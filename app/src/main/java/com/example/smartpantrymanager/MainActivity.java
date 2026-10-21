package com.example.smartpantrymanager;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartpantrymanager.adapter.PantryAdapter;
import com.example.smartpantrymanager.database.PantryDatabaseHelper;
import com.example.smartpantrymanager.database.PantryRepository;
import com.example.smartpantrymanager.model.Ingredient;
import com.example.smartpantrymanager.ui.ScreenInsets;
import java.util.List;

/** Pantry List Activity: refresh in onResume after Add/Edit returns. */
public class MainActivity extends AppCompatActivity implements PantryAdapter.Listener {
    private PantryDatabaseHelper helper;
    private PantryRepository repository;
    private PantryAdapter adapter;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ScreenInsets.apply(findViewById(R.id.main));
        com.example.smartpantrymanager.ui.Navigation.setup(this);
        helper = new PantryDatabaseHelper(this);
        repository = new PantryRepository(helper);
        adapter = new PantryAdapter(this);
        RecyclerView list = findViewById(R.id.pantryList);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        findViewById(R.id.addIngredient).setOnClickListener(v ->
                startActivity(new Intent(this, AddEditIngredientActivity.class)));
    }

    @Override protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        try {
            List<Ingredient> items = repository.getAllIngredients();
            adapter.submitItems(items);
            findViewById(R.id.emptyPantry).setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
        } catch (SQLException exception) {
            Toast.makeText(this, R.string.storage_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override public void onEdit(Ingredient ingredient) {
        Intent intent = new Intent(this, AddEditIngredientActivity.class);
        intent.putExtra(AddEditIngredientActivity.EXTRA_INGREDIENT_ID, ingredient.getId());
        startActivity(intent);
    }

    @Override public void onDelete(Ingredient ingredient) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_title)
                .setMessage(getString(R.string.delete_message, ingredient.getName()))
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    try {
                        boolean deleted = repository.deleteIngredient(ingredient.getId());
                        Toast.makeText(this, deleted ? R.string.deleted : R.string.item_missing,
                                Toast.LENGTH_SHORT).show();
                        refresh();
                    } catch (SQLException exception) {
                        Toast.makeText(this, R.string.storage_error, Toast.LENGTH_LONG).show();
                    }
                }).show();
    }

    @Override protected void onDestroy() {
        if (helper != null) helper.close();
        super.onDestroy();
    }
}
