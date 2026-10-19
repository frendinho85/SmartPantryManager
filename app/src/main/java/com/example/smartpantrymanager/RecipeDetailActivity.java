/**
 * Smart Pantry Manager - Recipe Detail Activity
 * Student: Mmametlafrederick Moloto (402411236)
 * Module: Mobile App Development 700
 * This activity displays the full ingredient list and preparation steps
 * for a selected recipe.
 */
package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView textViewRecipeName;
    private TextView textViewIngredientsList;
    private TextView textViewRecipeSteps;
    private Button buttonBack;

    private RecipeDAO recipeDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // 1. Link UI elements
        textViewRecipeName = findViewById(R.id.textViewRecipeName);
        textViewIngredientsList = findViewById(R.id.textViewIngredientsList);
        textViewRecipeSteps = findViewById(R.id.textViewRecipeSteps);
        buttonBack = findViewById(R.id.buttonBack);

        // 2. Get recipe ID from Intent
        int recipeId = getIntent().getIntExtra("recipe_id", -1);

        if (recipeId == -1) {
            Toast.makeText(this, "Error: Recipe not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 3. Load the recipe from the database
        recipeDAO = new RecipeDAO(this);
        recipeDAO.open();
        Recipe recipe = recipeDAO.getRecipeById(recipeId);
        recipeDAO.close();

        if (recipe == null) {
            Toast.makeText(this, "Error: Recipe not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 4. Display the recipe
        displayRecipe(recipe);

        // 5. Back button
        buttonBack.setOnClickListener(v -> finish());
    }

    // Populates the screen with the recipe name, ingredients, and method
    private void displayRecipe(Recipe recipe) {
        // Name
        textViewRecipeName.setText(recipe.getName());

        // Ingredients — build a bulleted list
        StringBuilder ingredientsBuilder = new StringBuilder();
        if (recipe.getIngredients() != null) {
            for (RecipeIngredient ing : recipe.getIngredients()) {
                ingredientsBuilder.append("• ")
                        .append(ing.getName())
                        .append(" — ")
                        .append(formatQuantity(ing.getQuantity()))
                        .append(" ")
                        .append(ing.getUnit())
                        .append("\n");
            }
        }
        textViewIngredientsList.setText(ingredientsBuilder.toString().trim());

        // Steps
        textViewRecipeSteps.setText(recipe.getSteps());
    }

    // Formats 2.0 as "2", 1.5 as "1.5" for nicer display
    private String formatQuantity(double qty) {
        if (qty == (int) qty) {
            return String.valueOf((int) qty);
        }
        return String.valueOf(qty);
    }
}