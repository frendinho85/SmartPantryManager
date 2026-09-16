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

    // UI Elements
    private EditText editTextName;
    private EditText editTextQuantity;
    private EditText editTextExpiry;
    private Spinner spinnerUnit;
    private Button buttonSave;

    // Database
    private PantryDAO pantryDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        // 1. Initialize the Database
        pantryDAO = new PantryDAO(this);

        // 2. Link UI elements to the XML views
        editTextName = findViewById(R.id.editTextName);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        editTextExpiry = findViewById(R.id.editTextExpiry);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        buttonSave = findViewById(R.id.buttonSave);

        // 3. Set up the Spinner (dropdown) with units
        // 3. Set up the Spinner (dropdown) with units
        String[] units = {"piece", "g", "kg", "ml", "L", "tsp", "tbsp", "cup"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                units
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(adapter);

        // 3b. Pre-select the default unit from Settings
        android.content.SharedPreferences prefs = getSharedPreferences("SmartPantryPrefs", MODE_PRIVATE);
        String defaultUnit = prefs.getString("default_unit", "piece");
        for (int i = 0; i < units.length; i++) {
            if (units[i].equals(defaultUnit)) {
                spinnerUnit.setSelection(i);
                break;
            }
        }
        // 4. Set up the Save button click listener
        buttonSave.setOnClickListener(v -> saveItem());
    }

    private void saveItem() {
        // Get data from the UI
        String name = editTextName.getText().toString().trim();
        String quantityStr = editTextQuantity.getText().toString().trim();
        String unit = spinnerUnit.getSelectedItem().toString();
        String expiry = editTextExpiry.getText().toString().trim();

        // --- 1. Validate Input ---
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

        // If expiry is empty, set it to null or "N/A"
        if (TextUtils.isEmpty(expiry)) {
            expiry = null;
        }

        // --- 2. Save to Database ---
        PantryItem newItem = new PantryItem(name, quantity, unit, expiry);

        pantryDAO.open();
        long id = pantryDAO.addPantryItem(newItem);
        pantryDAO.close();

        // --- 3. Show result and close screen ---
        if (id > 0) {
            Toast.makeText(this, "Item saved successfully!", Toast.LENGTH_SHORT).show();
            finish(); // This closes the activity and goes back to MainActivity
        } else {
            Toast.makeText(this, "Error saving item. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}