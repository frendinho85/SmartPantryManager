package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // UI Components
    private RecyclerView recyclerView;
    private PantryAdapter adapter;
    private List<PantryItem> pantryList;
    private FloatingActionButton fabAddItem;
    private Button buttonShowRecipes;

    // Database
    private PantryDAO pantryDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Initialize UI Elements
        recyclerView = findViewById(R.id.recyclerViewPantry);
        fabAddItem = findViewById(R.id.fabAddItem);
        buttonShowRecipes = findViewById(R.id.buttonShowRecipes);

        // 2. Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        pantryList = new ArrayList<>();

        // 3. Set up the Adapter
        adapter = new PantryAdapter(pantryList, item -> {
            // TODO: Edit on click — we'll implement this later
        });
        recyclerView.setAdapter(adapter);

        // 4. Initialize Database
        pantryDAO = new PantryDAO(this);

        // 5. Floating Action Button → Opens Add/Edit screen
        fabAddItem.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditIngredientActivity.class);
            startActivity(intent);
        });

        // 6. "Recipes" Button → Opens Suggested Recipes screen
        buttonShowRecipes.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SuggestedRecipesActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload the pantry list every time this screen becomes visible
        loadPantryItems();
    }

    private void loadPantryItems() {
        pantryDAO.open();
        List<PantryItem> items = pantryDAO.getAllPantryItems();
        pantryDAO.close();

        // Clear the old list and add the new data
        pantryList.clear();
        pantryList.addAll(items);

        // Notify the adapter that the data has changed
        adapter.notifyDataSetChanged();
    }
}