package com.example.smartpantrymanager.model;

/** A pantry item or a required recipe ingredient. */
public class Ingredient {
    private final long id;
    private final String name;
    private final String ingredientKey;
    private final double quantity;
    private final String unit;

    public Ingredient(long id, String name, String ingredientKey,
                      double quantity, String unit) {
        this.id = id;
        this.name = name;
        this.ingredientKey = ingredientKey;
        this.quantity = quantity;
        this.unit = unit;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getIngredientKey() { return ingredientKey; }
    public double getQuantity() { return quantity; }
    public String getUnit() { return unit; }
}
