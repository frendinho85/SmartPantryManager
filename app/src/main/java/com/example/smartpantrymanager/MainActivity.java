package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PantryAdapter adapter;
    private List<PantryItem> pantryList;
    private FloatingActionButton fabAddItem;
    private PantryDAO pantryDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Initialize UI
        recyclerView = findViewById(R.id.recyclerViewPantry);
        fabAddItem = findViewById(R.id.fabAddItem);

        // 2. Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        pantryList = new ArrayList<>();

        // 3. Adapter
        adapter = new PantryAdapter(pantryList, item -> {
            // TODO: Edit on click
        });
        recyclerView.setAdapter(adapter);

        // 4. Database
        pantryDAO = new PantryDAO(this);

        // 5. FAB → Add/Edit
        fabAddItem.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditIngredientActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPantryItems();
    }

    private void loadPantryItems() {
        pantryDAO.open();
        List<PantryItem> items = pantryDAO.getAllPantryItems();
        pantryDAO.close();

        pantryList.clear();
        pantryList.addAll(items);
        adapter.notifyDataSetChanged();
    }

    // TOOLBAR MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_recipes) {
            startActivity(new Intent(MainActivity.this, SuggestedRecipesActivity.class));
            return true;
        }
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}