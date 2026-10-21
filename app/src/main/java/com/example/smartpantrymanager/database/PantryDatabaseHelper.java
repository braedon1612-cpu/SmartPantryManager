package com.example.smartpantrymanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/** Owns schema creation and non-destructive version migrations. */
public class PantryDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "smart_pantry.db";
    public static final int DATABASE_VERSION = 2;
    public static final String TABLE_PANTRY = "Pantry";
    public static final String TABLE_RECIPES = "Recipes";
    public static final String TABLE_RECIPE_INGREDIENTS = "RecipeIngredients";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_INGREDIENT_KEY = "ingredient_key";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_UNIT = "unit";
    public static final String COL_INSTRUCTIONS = "instructions";
    public static final String COL_RECIPE_ID = "recipe_id";

    public PantryDatabaseHelper(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Pantry (" +
                "_id INTEGER PRIMARY KEY, " +
                "name TEXT NOT NULL CHECK(length(trim(name)) > 0), " +
                "ingredient_key TEXT NOT NULL CHECK(length(trim(ingredient_key)) > 0), " +
                "quantity REAL NOT NULL CHECK(quantity >= 0), " +
                "unit TEXT NOT NULL CHECK(length(trim(unit)) > 0), " +
                "UNIQUE(ingredient_key, unit))");

        db.execSQL("CREATE TABLE Recipes (" +
                "_id INTEGER PRIMARY KEY, " +
                "name TEXT NOT NULL UNIQUE CHECK(length(trim(name)) > 0), " +
                "instructions TEXT NOT NULL CHECK(length(trim(instructions)) > 0))");

        db.execSQL("CREATE TABLE RecipeIngredients (" +
                "_id INTEGER PRIMARY KEY, " +
                "recipe_id INTEGER NOT NULL, " +
                "name TEXT NOT NULL CHECK(length(trim(name)) > 0), " +
                "ingredient_key TEXT NOT NULL CHECK(length(trim(ingredient_key)) > 0), " +
                "quantity REAL NOT NULL CHECK(quantity > 0), " +
                "unit TEXT NOT NULL CHECK(length(trim(unit)) > 0), " +
                "FOREIGN KEY(recipe_id) REFERENCES Recipes(_id) ON DELETE CASCADE, " +
                "UNIQUE(recipe_id, ingredient_key))");

        RecipeSeeder.seed(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Version 1 had empty recipe tables. Pantry rows are preserved.
            // SQLiteOpenHelper wraps this upgrade in a transaction.
            RecipeSeeder.seed(db);
        }
    }
}
