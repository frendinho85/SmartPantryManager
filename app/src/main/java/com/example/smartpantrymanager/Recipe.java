package com.example.smartpantrymanager;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

    private int id;
    private String name;
    private String steps;
    private List<RecipeIngredient> ingredients;

    // Empty constructor
    public Recipe() {
        this.ingredients = new ArrayList<>();
    }

    // Constructor without ID (for inserting)
    public Recipe(String name, String steps) {
        this.name = name;
        this.steps = steps;
        this.ingredients = new ArrayList<>();
    }

    // Constructor with ID (for reading from DB)
    public Recipe(int id, String name, String steps) {
        this.id = id;
        this.name = name;
        this.steps = steps;
        this.ingredients = new ArrayList<>();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSteps() { return steps; }
    public void setSteps(String steps) { this.steps = steps; }

    public List<RecipeIngredient> getIngredients() { return ingredients; }
    public void setIngredients(List<RecipeIngredient> ingredients) { this.ingredients = ingredients; }

    // Convenience method for adding a single ingredient
    public void addIngredient(RecipeIngredient ingredient) {
        this.ingredients.add(ingredient);
    }
}