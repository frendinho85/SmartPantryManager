package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private List<Recipe> matchedRecipes;
    private TextView textViewNoMatches;

    private PantryDAO pantryDAO;
    private RecipeDAO recipeDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        recyclerView = findViewById(R.id.recyclerViewRecipes);
        textViewNoMatches = findViewById(R.id.textViewNoMatches);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        matchedRecipes = new ArrayList<>();

        adapter = new RecipeAdapter(matchedRecipes, recipe -> {
            // TODO: Open RecipeDetailActivity in Step 10
            Toast.makeText(this, "Selected: " + recipe.getName(), Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(adapter);

        pantryDAO = new PantryDAO(this);
        recipeDAO = new RecipeDAO(this);

        loadMatchingRecipes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload every time user comes back — pantry may have changed
        loadMatchingRecipes();
    }

    private void loadMatchingRecipes() {
        // 1. Get current pantry items
        pantryDAO.open();
        List<PantryItem> pantryItems = pantryDAO.getAllPantryItems();
        pantryDAO.close();

        // 2. Get all recipes
        recipeDAO.open();
        List<Recipe> allRecipes = recipeDAO.getAllRecipes();
        recipeDAO.close();

        // 3. Apply strict-matching
        List<Recipe> matches = new ArrayList<>();
        for (Recipe recipe : allRecipes) {
            if (IngredientMatcher.canMakeRecipe(recipe, pantryItems)) {
                matches.add(recipe);
            }
        }

        // 4. Update the list
        matchedRecipes.clear();
        matchedRecipes.addAll(matches);
        adapter.notifyDataSetChanged();

        // 5. Show the "No matches" message if empty
        if (matchedRecipes.isEmpty()) {
            textViewNoMatches.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textViewNoMatches.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}