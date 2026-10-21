package com.example.smartpantrymanager.logic;

import java.util.Locale;

/** Shared canonical ingredient names for storage and recipe matching. */
public final class IngredientNames {
    private IngredientNames() { }

    /** Conservative aliases: do not blindly remove 's' from every word. */
    public static String canonicalKey(String name) {
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

}
