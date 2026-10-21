package com.example.smartpantrymanager.logic;

import com.example.smartpantrymanager.model.Ingredient;
import com.example.smartpantrymanager.model.Recipe;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.assertEquals;

/** Regression tests for the assignment's core business rule. No emulator required. */
public class StrictRecipeMatcherTest {
    private static Ingredient item(String key, double quantity, String unit) {
        return new Ingredient(1, key, key, quantity, unit);
    }

    private static List<Ingredient> standardRequirements() {
        return Arrays.asList(item("egg", 2, "piece"), item("butter", 10, "g"));
    }

    private static void check(boolean expected, List<Ingredient> requirements,
                              Ingredient... pantry) {
        Recipe recipe = new Recipe(1, "Test recipe", "Test method", requirements);
        assertEquals(expected, StrictRecipeMatcher.canMake(recipe, Arrays.asList(pantry)));
    }

    @Test public void emptyPantryDoesNotMatch() {
        check(false, standardRequirements());
    }

    @Test public void missingIngredientDoesNotMatch() {
        check(false, standardRequirements(), item("egg", 2, "piece"));
    }

    @Test public void insufficientQuantityDoesNotMatch() {
        check(false, standardRequirements(), item("egg", 1, "piece"), item("butter", 10, "g"));
    }

    @Test public void exactQuantitiesMatch() {
        check(true, standardRequirements(), item("egg", 2, "piece"), item("butter", 10, "g"));
    }

    @Test public void extraStockDoesNotPreventMatch() {
        check(true, standardRequirements(), item("egg", 4, "piece"), item("butter", 20, "g"), item("rice", 100, "g"));
    }

    @Test public void kilogramsConvertToGrams() {
        check(true, standardRequirements(), item("egg", 2, "piece"), item("butter", 0.01, "kg"));
    }

    @Test public void zeroQuantityDoesNotMatch() {
        check(false, standardRequirements(), item("egg", 0, "piece"), item("butter", 10, "g"));
    }

    @Test public void pluralAndWhitespaceAliasesMatch() {
        check(true, standardRequirements(), item(" Eggs ", 2, "piece"), item("butter", 10, "g"));
    }

    @Test public void volumeCannotReplaceMass() {
        check(false, standardRequirements(), item("egg", 2, "piece"), item("butter", 10, "ml"));
    }

    @Test public void emptyRecipeIsRejected() {
        check(false, Collections.emptyList());
    }

    @Test public void litresConvertToMillilitres() {
        check(true, Arrays.asList(item("milk", 250, "ml")), item("milk", 0.25, "l"));
    }

    @Test public void stockInCompatibleUnitsIsCombined() {
        check(true, Arrays.asList(item("rice", 1500, "g")), item("rice", 1, "kg"), item("rice", 500, "g"));
    }

    @Test public void duplicateRequirementsCannotReuseStock() {
        check(false, Arrays.asList(item("rice", 100, "g"), item("rice", 100, "g")), item("rice", 150, "g"));
    }

    @Test public void tinyShortfallIsRejected() {
        check(false, Arrays.asList(item("rice", 100, "g")), item("rice", 99.99999, "g"));
    }

    @Test public void decimalStockAddsExactly() {
        check(true, Arrays.asList(item("rice", 0.3, "g")), item("rice", 0.1, "g"), item("rice", 0.2, "g"));
    }

    @Test public void nanStockIsRejected() {
        check(false, Arrays.asList(item("rice", 100, "g")), item("rice", Double.NaN, "g"));
    }

    @Test public void infiniteStockIsRejected() {
        check(false, Arrays.asList(item("rice", 100, "g")), item("rice", Double.POSITIVE_INFINITY, "g"));
    }

    @Test public void negativeStockIsRejected() {
        check(false, Arrays.asList(item("rice", 100, "g")), item("rice", -100, "g"));
    }

    @Test public void unknownUnitIsRejected() {
        check(false, Arrays.asList(item("rice", 100, "g")), item("rice", 100, "cup"));
    }

    @Test public void zeroRequirementIsRejected() {
        check(false, Arrays.asList(item("rice", 0, "g")), item("rice", 100, "g"));
    }

    @Test public void gramsConvertToKilograms() {
        check(true, Arrays.asList(item("rice", 1, "kg")), item("rice", 1000, "g"));
    }

    @Test public void countCannotReplaceMass() {
        check(false, Arrays.asList(item("potato", 100, "g")), item("potato", 100, "piece"));
    }

    @Test public void tomatoesAliasMatches() {
        check(true, Arrays.asList(item("tomato", 2, "piece")), item("tomatoes", 2, "piece"));
    }

    @Test public void removingIngredientRemovesMatch() {
        check(false, standardRequirements(), item("butter", 10, "g"));
    }

}
