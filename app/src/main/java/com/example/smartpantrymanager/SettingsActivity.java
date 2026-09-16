package com.example.smartpantrymanager;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "SmartPantryPrefs";
    private static final String KEY_EXPIRY_ALERTS = "expiry_alerts";
    private static final String KEY_DEFAULT_UNIT = "default_unit";

    private Switch switchExpiryAlerts;
    private Spinner spinnerDefaultUnit;
    private Button buttonSaveSettings;
    private Button buttonClearPantry;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Set ActionBar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Settings");
        }

        // Link UI
        switchExpiryAlerts = findViewById(R.id.switchExpiryAlerts);
        spinnerDefaultUnit = findViewById(R.id.spinnerDefaultUnit);
        buttonSaveSettings = findViewById(R.id.buttonSaveSettings);
        buttonClearPantry = findViewById(R.id.buttonClearPantry);

        // Set up unit spinner
        String[] units = {"piece", "g", "kg", "ml", "L", "tsp", "tbsp", "cup"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                units
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDefaultUnit.setAdapter(adapter);

        // Load saved preferences
        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        loadPreferences();

        // Save button
        buttonSaveSettings.setOnClickListener(v -> savePreferences());

        // Clear pantry button
        buttonClearPantry.setOnClickListener(v -> confirmClearPantry());
    }

    private void loadPreferences() {
        // Expiry alerts (default ON)
        boolean expiryEnabled = preferences.getBoolean(KEY_EXPIRY_ALERTS, true);
        switchExpiryAlerts.setChecked(expiryEnabled);

        // Default unit (default "piece")
        String savedUnit = preferences.getString(KEY_DEFAULT_UNIT, "piece");
        String[] units = {"piece", "g", "kg", "ml", "L", "tsp", "tbsp", "cup"};
        for (int i = 0; i < units.length; i++) {
            if (units[i].equals(savedUnit)) {
                spinnerDefaultUnit.setSelection(i);
                break;
            }
        }
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_EXPIRY_ALERTS, switchExpiryAlerts.isChecked());
        editor.putString(KEY_DEFAULT_UNIT, spinnerDefaultUnit.getSelectedItem().toString());
        editor.apply();

        Toast.makeText(this, "Settings saved!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void confirmClearPantry() {
        new AlertDialog.Builder(this)
                .setTitle("Clear All Pantry Items?")
                .setMessage("This will permanently delete every item in your pantry. Are you sure?")
                .setPositiveButton("Yes, Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearPantry();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void clearPantry() {
        PantryDAO pantryDAO = new PantryDAO(this);
        pantryDAO.open();
        pantryDAO.deleteAllPantryItems();
        pantryDAO.close();

        Toast.makeText(this, "All pantry items deleted.", Toast.LENGTH_SHORT).show();
    }
}