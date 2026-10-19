/**
 * Smart Pantry Manager - Add/Edit Ingredient Activity
 * Student: Mmametlafrederick Moloto (402411236)
 * Module: Mobile App Development 700
 *
 * This activity provides a form for the user to add a new pantry item
 * or edit an existing one. It validates input before saving to the database.
 */
package com.example.smartpantrymanager;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextQuantity;
    private EditText editTextExpiry;
    private Spinner spinnerUnit;
    private Button buttonSave;

    private PantryDAO pantryDAO;

    // -1 means ADD mode; otherwise it's the ID of the item being edited
    private int editItemId = -1;

    private static final String[] UNITS = {"piece", "g", "kg", "ml", "L", "tsp", "tbsp", "cup"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        pantryDAO = new PantryDAO(this);

        // Link UI
        editTextName = findViewById(R.id.editTextName);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        editTextExpiry = findViewById(R.id.editTextExpiry);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        buttonSave = findViewById(R.id.buttonSave);

        // Set up spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, UNITS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(adapter);

        // Check if we're editing an existing item
        editItemId = getIntent().getIntExtra("pantry_item_id", -1);

        if (editItemId != -1) {
            // ---- EDIT MODE ----
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Edit Ingredient");
            }
            buttonSave.setText("Update Item");
            loadExistingItem();
        } else {
            // ---- ADD MODE ----
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Add New Item");
            }
            applyDefaultUnit();
        }

        buttonSave.setOnClickListener(v -> saveItem());
    }

    // Pre-fills the form fields when the user is editing an existing item
    private void loadExistingItem() {
        pantryDAO.open();
        PantryItem existing = pantryDAO.getPantryItemById(editItemId);
        pantryDAO.close();

        if (existing == null) {
            Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        editTextName.setText(existing.getName());
        editTextQuantity.setText(String.valueOf(existing.getQuantity()));

        // Select the correct unit in the spinner
        for (int i = 0; i < UNITS.length; i++) {
            if (UNITS[i].equals(existing.getUnit())) {
                spinnerUnit.setSelection(i);
                break;
            }
        }

        if (existing.getExpiryDate() != null) {
            editTextExpiry.setText(existing.getExpiryDate());
        }
    }

    // Pre-select the user's preferred default unit (from Settings)
    private void applyDefaultUnit() {
        android.content.SharedPreferences prefs =
                getSharedPreferences("SmartPantryPrefs", MODE_PRIVATE);
        String defaultUnit = prefs.getString("default_unit", "piece");
        for (int i = 0; i < UNITS.length; i++) {
            if (UNITS[i].equals(defaultUnit)) {
                spinnerUnit.setSelection(i);
                break;
            }
        }
    }

    // Validates user input and either inserts a new item or updates an existing one
    private void saveItem() {
        String name = editTextName.getText().toString().trim();
        String quantityStr = editTextQuantity.getText().toString().trim();
        String unit = spinnerUnit.getSelectedItem().toString();
        String expiry = editTextExpiry.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter an ingredient name", Toast.LENGTH_SHORT).show();
            editTextName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(quantityStr)) {
            Toast.makeText(this, "Please enter a quantity", Toast.LENGTH_SHORT).show();
            editTextQuantity.requestFocus();
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(quantityStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Quantity must be a valid number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (quantity <= 0) {
            Toast.makeText(this, "Quantity must be greater than 0", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(expiry)) {
            expiry = null;
        }

        // Save
        if (editItemId != -1) {
            // UPDATE existing
            PantryItem updated = new PantryItem(editItemId, name, quantity, unit, expiry);
            pantryDAO.open();
            int rows = pantryDAO.updatePantryItem(updated);
            pantryDAO.close();

            if (rows > 0) {
                Toast.makeText(this, "Item updated!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error updating item", Toast.LENGTH_SHORT).show();
            }
        } else {
            // INSERT new
            PantryItem newItem = new PantryItem(name, quantity, unit, expiry);
            pantryDAO.open();
            long id = pantryDAO.addPantryItem(newItem);
            pantryDAO.close();

            if (id > 0) {
                Toast.makeText(this, "Item saved!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error saving item", Toast.LENGTH_SHORT).show();
            }
        }
    }
}