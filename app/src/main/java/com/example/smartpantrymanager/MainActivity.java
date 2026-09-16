package com.example.smartpantrymanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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

        recyclerView = findViewById(R.id.recyclerViewPantry);
        fabAddItem = findViewById(R.id.fabAddItem);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        pantryList = new ArrayList<>();

        // Set up adapter with BOTH click and long-click handlers
        adapter = new PantryAdapter(pantryList,
                item -> openEditScreen(item),          // Tap → Edit
                item -> confirmDelete(item));          // Long-press → Delete
        recyclerView.setAdapter(adapter);

        pantryDAO = new PantryDAO(this);

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

    // ---- TAP → Edit ----
    private void openEditScreen(PantryItem item) {
        Intent intent = new Intent(MainActivity.this, AddEditIngredientActivity.class);
        intent.putExtra("pantry_item_id", item.getId());
        startActivity(intent);
    }

    // ---- LONG-PRESS → Delete ----
    private void confirmDelete(PantryItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Delete " + item.getName() + "?")
                .setMessage("This will remove it from your pantry.")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pantryDAO.open();
                        pantryDAO.deletePantryItem(item);
                        pantryDAO.close();
                        loadPantryItems();
                        Toast.makeText(MainActivity.this,
                                item.getName() + " deleted",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // ---- TOOLBAR MENU ----
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