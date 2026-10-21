package com.example.smartpantrymanager.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;

import com.example.smartpantrymanager.model.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Pantry persistence. The caller owns and closes the supplied helper.
 * Duplicate ingredient/unit pairs are rejected rather than overwriting stock.
 */
public class PantryRepository {
    private final PantryDatabaseHelper helper;

    public PantryRepository(PantryDatabaseHelper helper) {
        this.helper = helper;
    }

    public long addIngredient(String name, double quantity, String unit) {
        ContentValues values = validatedValues(name, quantity, unit);
        try {
            return helper.getWritableDatabase().insertOrThrow(
                    PantryDatabaseHelper.TABLE_PANTRY, null, values);
        } catch (SQLiteConstraintException exception) {
            throw new IllegalArgumentException(
                    "This ingredient and unit already exist. Edit the existing item.",
                    exception);
        }
    }

    public List<Ingredient> getAllIngredients() {
        List<Ingredient> items = new ArrayList<>();
        try (Cursor cursor = helper.getReadableDatabase().query(
                PantryDatabaseHelper.TABLE_PANTRY, null, null, null,
                null, null, PantryDatabaseHelper.COL_NAME + " COLLATE NOCASE ASC")) {
            while (cursor.moveToNext()) {
                items.add(readIngredient(cursor));
            }
        }
        return items;
    }

    /** Returns null if the item has been deleted or does not exist. */
    public Ingredient getIngredient(long id) {
        requireId(id);
        try (Cursor cursor = helper.getReadableDatabase().query(
                PantryDatabaseHelper.TABLE_PANTRY, null,
                PantryDatabaseHelper.COL_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null)) {
            return cursor.moveToFirst() ? readIngredient(cursor) : null;
        }
    }

    /** Returns false if no row with this ID exists. */
    public boolean updateIngredient(long id, String name,
                                    double quantity, String unit) {
        requireId(id);
        ContentValues values = validatedValues(name, quantity, unit);
        try {
            return helper.getWritableDatabase().update(
                    PantryDatabaseHelper.TABLE_PANTRY, values,
                    PantryDatabaseHelper.COL_ID + " = ?",
                    new String[]{String.valueOf(id)}) == 1;
        } catch (SQLiteConstraintException exception) {
            throw new IllegalArgumentException(
                    "Another item already uses this ingredient and unit.", exception);
        }
    }

    public boolean deleteIngredient(long id) {
        requireId(id);
        return helper.getWritableDatabase().delete(
                PantryDatabaseHelper.TABLE_PANTRY,
                PantryDatabaseHelper.COL_ID + " = ?",
                new String[]{String.valueOf(id)}) == 1;
    }

    private static ContentValues validatedValues(String name,
                                                  double quantity, String unit) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Enter an ingredient name.");
        }
        if (Double.isNaN(quantity) || Double.isInfinite(quantity) || quantity < 0) {
            throw new IllegalArgumentException("Quantity must be a finite number of zero or more.");
        }
        if (unit == null || unit.trim().isEmpty()) {
            throw new IllegalArgumentException("Select a unit.");
        }
        String cleanName = name.trim().replaceAll("\\s+", " ");
        String cleanUnit = unit.trim().toLowerCase(Locale.ROOT);
        // Use a controlled unit list in the Add/Edit screen.
        if (!cleanUnit.equals("g") && !cleanUnit.equals("kg")
                && !cleanUnit.equals("ml") && !cleanUnit.equals("l")
                && !cleanUnit.equals("piece")) {
            throw new IllegalArgumentException("Use g, kg, ml, l or piece.");
        }

        ContentValues values = new ContentValues();
        values.put(PantryDatabaseHelper.COL_NAME, cleanName);
        values.put(PantryDatabaseHelper.COL_INGREDIENT_KEY,
                ingredientKey(cleanName));
        values.put(PantryDatabaseHelper.COL_QUANTITY, quantity);
        values.put(PantryDatabaseHelper.COL_UNIT, cleanUnit);
        return values;
    }

    /** Conservative aliases: do not blindly remove 's' from every word. */
    public static String ingredientKey(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Enter an ingredient name.");
        }
        String key = name.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
        switch (key) {
            case "tomatoes": return "tomato";
            case "potatoes": return "potato";
            case "onions": return "onion";
            case "eggs": return "egg";
            case "carrots": return "carrot";
            case "bananas": return "banana";
            case "apples": return "apple";
            case "peppers": return "pepper";
            case "mushrooms": return "mushroom";
            default: return key;
        }
    }

    private static void requireId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid ingredient ID.");
        }
    }

    private static Ingredient readIngredient(Cursor cursor) {
        return new Ingredient(
                cursor.getLong(cursor.getColumnIndexOrThrow(PantryDatabaseHelper.COL_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(PantryDatabaseHelper.COL_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(PantryDatabaseHelper.COL_INGREDIENT_KEY)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(PantryDatabaseHelper.COL_QUANTITY)),
                cursor.getString(cursor.getColumnIndexOrThrow(PantryDatabaseHelper.COL_UNIT)));
    }
}

