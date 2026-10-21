package com.example.smartpantrymanager.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/** Default recipes. Called only inside SQLiteOpenHelper's creation/upgrade transaction. */
final class RecipeSeeder {
    private RecipeSeeder() { }

    static void seed(SQLiteDatabase db) {
        addRecipe(db, "Scrambled Eggs",
                "1. Beat the eggs.\n2. Melt the butter in a pan over low heat.\n3. Add eggs and stir until fully set.",
                new Requirement("egg", 2, "piece"), new Requirement("butter", 10, "g"));
        addRecipe(db, "Cheese Omelette",
                "1. Beat the eggs.\n2. Melt butter in a pan and add eggs.\n3. Add grated cheese, fold and cook until the egg is fully set.",
                new Requirement("egg", 2, "piece"), new Requirement("cheese", 30, "g"), new Requirement("butter", 10, "g"));
        addRecipe(db, "Tomato Omelette",
                "1. Chop the tomato and beat the eggs.\n2. Heat oil and soften the tomato.\n3. Add eggs and cook until fully set.",
                new Requirement("egg", 2, "piece"), new Requirement("tomato", 1, "piece"), new Requirement("oil", 10, "ml"));
        addRecipe(db, "Fried Egg Toast",
                "1. Toast the bread in a dry pan.\n2. Heat the oil and fry the egg until the white and yolk are set.\n3. Serve on the toast.",
                new Requirement("bread", 2, "piece"), new Requirement("egg", 1, "piece"), new Requirement("oil", 5, "ml"));
        addRecipe(db, "Grilled Cheese Sandwich",
                "1. Butter the outside of the bread.\n2. Place grated cheese between the slices.\n3. Cook in a pan on both sides until golden and melted.",
                new Requirement("bread", 2, "piece"), new Requirement("cheese", 40, "g"), new Requirement("butter", 10, "g"));
        addRecipe(db, "Tomato Cheese Toast",
                "1. Slice the tomato and grate the cheese.\n2. Place both on the bread.\n3. Grill until the cheese melts and the bread is toasted.",
                new Requirement("bread", 2, "piece"), new Requirement("tomato", 1, "piece"), new Requirement("cheese", 30, "g"));
        addRecipe(db, "Peanut Butter Banana Toast",
                "1. Toast the bread.\n2. Spread with peanut butter.\n3. Slice the peeled banana and arrange on top.",
                new Requirement("bread", 2, "piece"), new Requirement("peanut butter", 30, "g"), new Requirement("banana", 1, "piece"));
        addRecipe(db, "Banana Pancakes",
                "1. Mash the peeled banana and beat in the eggs.\n2. Heat oil in a non-stick pan.\n3. Cook small pancakes on both sides until fully set.",
                new Requirement("banana", 1, "piece"), new Requirement("egg", 2, "piece"), new Requirement("oil", 5, "ml"));
        addRecipe(db, "Milk Porridge",
                "1. Combine oats and milk in a saucepan.\n2. Simmer gently, stirring until oats are soft.\n3. Stir in the sugar and serve.",
                new Requirement("oats", 50, "g"), new Requirement("milk", 250, "ml"), new Requirement("sugar", 10, "g"));
        addRecipe(db, "Banana Porridge",
                "1. Combine oats and milk in a saucepan.\n2. Simmer, stirring until oats are soft.\n3. Mash the peeled banana and stir it through.",
                new Requirement("oats", 50, "g"), new Requirement("milk", 250, "ml"), new Requirement("banana", 1, "piece"));
        addRecipe(db, "Apple Yoghurt Bowl",
                "1. Wash, core and chop the apple.\n2. Spoon yoghurt into a bowl.\n3. Add apple and oats, stir and serve.",
                new Requirement("apple", 1, "piece"), new Requirement("yoghurt", 150, "g"), new Requirement("oats", 30, "g"));
        addRecipe(db, "Banana Smoothie",
                "1. Peel and slice the banana.\n2. Blend banana, milk and yoghurt until smooth.\n3. Serve immediately.",
                new Requirement("banana", 1, "piece"), new Requirement("milk", 200, "ml"), new Requirement("yoghurt", 100, "g"));
        addRecipe(db, "Tomato Cucumber Salad",
                "1. Wash and chop tomato and cucumber.\n2. Toss with oil and salt.\n3. Serve immediately.",
                new Requirement("tomato", 2, "piece"), new Requirement("cucumber", 1, "piece"), new Requirement("oil", 10, "ml"), new Requirement("salt", 1, "g"));
        addRecipe(db, "Carrot Cabbage Slaw",
                "1. Wash and grate carrot and finely shred cabbage.\n2. Toss with yoghurt and salt.\n3. Serve immediately or refrigerate.",
                new Requirement("carrot", 1, "piece"), new Requirement("cabbage", 150, "g"), new Requirement("yoghurt", 60, "g"), new Requirement("salt", 1, "g"));
        addRecipe(db, "Sauteed Mushrooms",
                "1. Clean and slice mushrooms.\n2. Melt butter in a pan.\n3. Add mushrooms and salt, then cook until tender.",
                new Requirement("mushroom", 200, "g"), new Requirement("butter", 15, "g"), new Requirement("salt", 1, "g"));
        addRecipe(db, "Garlic Spinach",
                "1. Wash spinach and peel and chop garlic.\n2. Warm oil and gently cook the garlic.\n3. Add spinach and salt and stir until wilted.",
                new Requirement("spinach", 150, "g"), new Requirement("garlic", 5, "g"), new Requirement("oil", 10, "ml"), new Requirement("salt", 1, "g"));
        addRecipe(db, "Roasted Potatoes",
                "1. Heat the oven to 200 C.\n2. Wash and cut potatoes into small wedges.\n3. Toss with oil and salt and roast for 35-45 minutes until tender.",
                new Requirement("potato", 3, "piece"), new Requirement("oil", 15, "ml"), new Requirement("salt", 2, "g"));
        addRecipe(db, "Mashed Potatoes",
                "1. Peel and chop potatoes.\n2. Boil in the measured water until tender and drain.\n3. Mash with milk, butter and salt.",
                new Requirement("potato", 3, "piece"), new Requirement("water", 1000, "ml"), new Requirement("milk", 100, "ml"), new Requirement("butter", 20, "g"), new Requirement("salt", 2, "g"));
        addRecipe(db, "Tomato Pasta",
                "1. Boil water and cook pasta until tender, then drain.\n2. Chop tomato and peeled garlic.\n3. Heat oil, cook garlic and tomato until soft, add salt and toss with pasta.",
                new Requirement("pasta", 150, "g"), new Requirement("water", 1500, "ml"), new Requirement("tomato", 2, "piece"), new Requirement("garlic", 5, "g"), new Requirement("oil", 10, "ml"), new Requirement("salt", 2, "g"));
        addRecipe(db, "Egg Fried Rice",
                "1. Simmer rice in the measured water until cooked and absorbed.\n2. Chop onion and soften it in oil in a pan.\n3. Add beaten eggs and stir until fully set, then add rice and salt and stir until hot.",
                new Requirement("rice", 150, "g"), new Requirement("water", 300, "ml"), new Requirement("egg", 2, "piece"), new Requirement("onion", 1, "piece"), new Requirement("oil", 10, "ml"), new Requirement("salt", 2, "g"));
    }

    private static void addRecipe(SQLiteDatabase db, String name,
                                  String instructions, Requirement... requirements) {
        ContentValues recipe = new ContentValues();
        recipe.put(PantryDatabaseHelper.COL_NAME, name);
        recipe.put(PantryDatabaseHelper.COL_INSTRUCTIONS, instructions);
        long recipeId = db.insertOrThrow(PantryDatabaseHelper.TABLE_RECIPES, null, recipe);
        for (Requirement requirement : requirements) {
            ContentValues values = new ContentValues();
            values.put(PantryDatabaseHelper.COL_RECIPE_ID, recipeId);
            values.put(PantryDatabaseHelper.COL_NAME, requirement.name);
            values.put(PantryDatabaseHelper.COL_INGREDIENT_KEY,
                    PantryRepository.ingredientKey(requirement.name));
            values.put(PantryDatabaseHelper.COL_QUANTITY, requirement.quantity);
            values.put(PantryDatabaseHelper.COL_UNIT, requirement.unit);
            db.insertOrThrow(PantryDatabaseHelper.TABLE_RECIPE_INGREDIENTS, null, values);
        }
    }

    private static final class Requirement {
        final String name;
        final double quantity;
        final String unit;

        Requirement(String name, double quantity, String unit) {
            this.name = name;
            this.quantity = quantity;
            this.unit = unit;
        }
    }
}
