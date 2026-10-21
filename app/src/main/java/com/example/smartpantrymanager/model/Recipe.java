package com.example.smartpantrymanager.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Recipe details and an immutable list of its requirements. */
public class Recipe {
    private final long id;
    private final String name;
    private final String instructions;
    private final List<Ingredient> requiredIngredients;

    public Recipe(long id, String name, String instructions,
                  List<Ingredient> requiredIngredients) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.requiredIngredients = Collections.unmodifiableList(
                new ArrayList<>(requiredIngredients));
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getInstructions() { return instructions; }
    public List<Ingredient> getRequiredIngredients() {
        return requiredIngredients;
    }
}
