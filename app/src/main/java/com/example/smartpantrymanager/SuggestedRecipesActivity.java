package com.example.smartpantrymanager;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartpantrymanager.adapter.RecipeAdapter;
import com.example.smartpantrymanager.database.PantryDatabaseHelper;
import com.example.smartpantrymanager.database.RecipeRepository;
import com.example.smartpantrymanager.model.Recipe;
import com.example.smartpantrymanager.ui.Navigation;
import com.example.smartpantrymanager.ui.ScreenInsets;
import java.util.Collections;
import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity {
    private PantryDatabaseHelper helper;
    private RecipeRepository repository;
    private RecipeAdapter adapter;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suggested_recipes);
        ScreenInsets.apply(findViewById(R.id.main));
        Navigation.setup(this);
        helper = new PantryDatabaseHelper(this);
        repository = new RecipeRepository(helper);
        adapter = new RecipeAdapter(recipe -> {
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.getId());
            startActivity(intent);
        });
        RecyclerView list = findViewById(R.id.recipeList);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
    }

    @Override protected void onResume() {
        super.onResume();
        TextView empty = findViewById(R.id.emptyRecipes);
        try {
            List<Recipe> recipes = repository.getSuggestedRecipes();
            adapter.submitItems(recipes);
            empty.setText(R.string.no_recipes);
            empty.setVisibility(recipes.isEmpty() ? View.VISIBLE : View.GONE);
        } catch (SQLException exception) {
            adapter.submitItems(Collections.emptyList());
            empty.setText(R.string.recipes_error);
            empty.setVisibility(View.VISIBLE);
        }
    }

    @Override protected void onDestroy() {
        if (helper != null) helper.close();
        super.onDestroy();
    }
}
