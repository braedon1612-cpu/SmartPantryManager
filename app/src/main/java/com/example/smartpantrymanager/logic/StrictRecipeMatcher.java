package com.example.smartpantrymanager.logic;

import com.example.smartpantrymanager.model.Ingredient;
import com.example.smartpantrymanager.model.Recipe;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** All requirements must be satisfied. No implicit pantry staples or partial matches. */
public final class StrictRecipeMatcher {
    private StrictRecipeMatcher() { }

    public static boolean canMake(Recipe recipe, List<Ingredient> pantry) {
        if (recipe.getRequiredIngredients().isEmpty()) {
            return false; // An incomplete recipe must not qualify.
        }
        Map<String, BigDecimal> available = new HashMap<>();
        for (Ingredient item : pantry) {
            if (!validQuantity(item.getQuantity()) || item.getQuantity() == 0) {
                continue;
            }
            String key = stockKey(item);
            if (key != null) {
                available.put(key, available.getOrDefault(key, BigDecimal.ZERO)
                        .add(baseQuantity(item)));
            }
        }

        // Aggregate requirements too, so duplicate lines cannot reuse the same stock.
        Map<String, BigDecimal> required = new HashMap<>();
        for (Ingredient item : recipe.getRequiredIngredients()) {
            if (!validQuantity(item.getQuantity()) || item.getQuantity() <= 0) {
                return false;
            }
            String key = stockKey(item);
            if (key == null) {
                return false;
            }
            required.put(key, required.getOrDefault(key, BigDecimal.ZERO)
                    .add(baseQuantity(item)));
        }
        for (Map.Entry<String, BigDecimal> requirement : required.entrySet()) {
            BigDecimal stock = available.get(requirement.getKey());
            if (stock == null || stock.compareTo(requirement.getValue()) < 0) {
                return false;
            }
        }
        return true;
    }

    private static boolean validQuantity(double quantity) {
        return !Double.isNaN(quantity) && !Double.isInfinite(quantity) && quantity >= 0;
    }

    private static String stockKey(Ingredient item) {
        if (item.getIngredientKey() == null || item.getIngredientKey().trim().isEmpty()
                || item.getUnit() == null) {
            return null;
        }
        String dimension;
        switch (item.getUnit().trim().toLowerCase(Locale.ROOT)) {
            case "g": case "kg": dimension = "mass"; break;
            case "ml": case "l": dimension = "volume"; break;
            case "piece": dimension = "count"; break;
            default: return null;
        }
        return IngredientNames.canonicalKey(item.getIngredientKey()) + "|" + dimension;
    }

    private static BigDecimal baseQuantity(Ingredient item) {
        BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
        String unit = item.getUnit().trim().toLowerCase(Locale.ROOT);
        return unit.equals("kg") || unit.equals("l")
                ? quantity.multiply(BigDecimal.valueOf(1000)) : quantity;
    }
}
