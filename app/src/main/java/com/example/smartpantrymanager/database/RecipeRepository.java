package com.example.smartpantrymanager.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.smartpantrymanager.logic.StrictRecipeMatcher;
import com.example.smartpantrymanager.model.Ingredient;
import com.example.smartpantrymanager.model.Recipe;
import java.util.ArrayList;
import java.util.List;

/** Loads complete recipe requirements before applying the strict rule. */
public class RecipeRepository {
    private final PantryDatabaseHelper helper;

    public RecipeRepository(PantryDatabaseHelper helper) {
        this.helper = helper;
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        try (Cursor cursor = db.query(PantryDatabaseHelper.TABLE_RECIPES,
                null, null, null, null, null, "name COLLATE NOCASE ASC")) {
            while (cursor.moveToNext()) {
                recipes.add(readRecipe(db, cursor));
            }
        }
        return recipes;
    }

    public Recipe getRecipe(long id) {
        try (Cursor cursor = helper.getReadableDatabase().query(
                PantryDatabaseHelper.TABLE_RECIPES, null, "_id = ?",
                new String[]{String.valueOf(id)}, null, null, null)) {
            return cursor.moveToFirst()
                    ? readRecipe(helper.getReadableDatabase(), cursor) : null;
        }
    }

    public List<Recipe> getSuggestedRecipes() {
        List<Ingredient> pantry = new PantryRepository(helper).getAllIngredients();
        List<Recipe> suggestions = new ArrayList<>();
        for (Recipe recipe : getAllRecipes()) {
            if (StrictRecipeMatcher.canMake(recipe, pantry)) {
                suggestions.add(recipe);
            }
        }
        return suggestions;
    }

    private Recipe readRecipe(SQLiteDatabase db, Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String instructions = cursor.getString(cursor.getColumnIndexOrThrow("instructions"));
        List<Ingredient> ingredients = new ArrayList<>();
        try (Cursor requirements = db.query(
                PantryDatabaseHelper.TABLE_RECIPE_INGREDIENTS, null, "recipe_id = ?",
                new String[]{String.valueOf(id)}, null, null, "_id ASC")) {
            while (requirements.moveToNext()) {
                ingredients.add(new Ingredient(
                        requirements.getLong(requirements.getColumnIndexOrThrow("_id")),
                        requirements.getString(requirements.getColumnIndexOrThrow("name")),
                        requirements.getString(requirements.getColumnIndexOrThrow("ingredient_key")),
                        requirements.getDouble(requirements.getColumnIndexOrThrow("quantity")),
                        requirements.getString(requirements.getColumnIndexOrThrow("unit"))));
            }
        }
        return new Recipe(id, name, instructions, ingredients);
    }
}
