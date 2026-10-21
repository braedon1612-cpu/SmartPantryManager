package com.example.smartpantrymanager;

import android.database.SQLException;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smartpantrymanager.database.PantryDatabaseHelper;
import com.example.smartpantrymanager.database.RecipeRepository;
import com.example.smartpantrymanager.model.Ingredient;
import com.example.smartpantrymanager.model.Recipe;
import com.example.smartpantrymanager.ui.ScreenInsets;
import java.text.NumberFormat;

public class RecipeDetailActivity extends AppCompatActivity {
    public static final String EXTRA_RECIPE_ID = "recipe_id";

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_detail);
        ScreenInsets.apply(findViewById(R.id.main));
        findViewById(R.id.backToRecipes).setOnClickListener(v -> finish());
        long id = getIntent().getLongExtra(EXTRA_RECIPE_ID, -1);
        if (id <= 0) {
            missingRecipe();
            return;
        }
        try (PantryDatabaseHelper helper = new PantryDatabaseHelper(this)) {
            Recipe recipe = new RecipeRepository(helper).getRecipe(id);
            if (recipe == null) {
                missingRecipe();
                return;
            }
            ((TextView) findViewById(R.id.detailName)).setText(recipe.getName());
            StringBuilder ingredients = new StringBuilder();
            NumberFormat format = NumberFormat.getNumberInstance();
            for (Ingredient item : recipe.getRequiredIngredients()) {
                if (ingredients.length() > 0) ingredients.append("\n");
                ingredients.append(getString(R.string.ingredient_line, item.getName(),
                        format.format(item.getQuantity()), item.getUnit()));
            }
            ((TextView) findViewById(R.id.detailIngredients)).setText(ingredients.toString());
            ((TextView) findViewById(R.id.detailInstructions)).setText(recipe.getInstructions());
        } catch (SQLException exception) {
            Toast.makeText(this, R.string.recipes_error, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void missingRecipe() {
        Toast.makeText(this, R.string.recipe_missing, Toast.LENGTH_LONG).show();
        finish();
    }
}

