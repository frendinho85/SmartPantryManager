/**
 * Smart Pantry Manager - Pantry Data Access Object
 * Student: Mmametlafrederick Moloto (402411236)
 * Module: Mobile App Development 700
 *
 * This class handles all CRUD (Create, Read, Update, Delete) operations
 * for the pantry_items table in the SQLite database.
 */
package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class PantryDAO {

    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public PantryDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Inserts a new ingredient into the pantry_items table
    public long addPantryItem(PantryItem item) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_PANTRY_NAME, item.getName());
        values.put(DBHelper.COLUMN_PANTRY_QUANTITY, item.getQuantity());
        values.put(DBHelper.COLUMN_PANTRY_UNIT, item.getUnit());
        values.put(DBHelper.COLUMN_PANTRY_EXPIRY, item.getExpiryDate());
        return database.insert(DBHelper.TABLE_PANTRY, null, values);
    }

    // Retrieves all ingredients currently stored in the pantry_items table
    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> items = new ArrayList<>();
        Cursor cursor = database.query(
                DBHelper.TABLE_PANTRY,
                null, null, null, null, null, null
        );

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                PantryItem item = new PantryItem(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PANTRY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PANTRY_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PANTRY_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PANTRY_UNIT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PANTRY_EXPIRY))
                );
                items.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return items;
    }

    // READ: Get a single pantry item by ID
    public PantryItem getPantryItemById(int id) {
        Cursor cursor = database.query(
                DBHelper.TABLE_PANTRY,
                null,
                DBHelper.COLUMN_PANTRY_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null
        );

        PantryItem item = null;
        if (cursor != null && cursor.moveToFirst()) {
            item = new PantryItem(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PANTRY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PANTRY_NAME)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PANTRY_QUANTITY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PANTRY_UNIT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PANTRY_EXPIRY))
            );
            cursor.close();
        }
        return item;
    }

    // UPDATE: Edit an existing item
    public int updatePantryItem(PantryItem item) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_PANTRY_NAME, item.getName());
        values.put(DBHelper.COLUMN_PANTRY_QUANTITY, item.getQuantity());
        values.put(DBHelper.COLUMN_PANTRY_UNIT, item.getUnit());
        values.put(DBHelper.COLUMN_PANTRY_EXPIRY, item.getExpiryDate());
        return database.update(
                DBHelper.TABLE_PANTRY,
                values,
                DBHelper.COLUMN_PANTRY_ID + " = ?",
                new String[]{String.valueOf(item.getId())}
        );
    }

    // Deletes a specific ingredient from the database using its ID
    public void deletePantryItem(PantryItem item) {
        database.delete(
                DBHelper.TABLE_PANTRY,
                DBHelper.COLUMN_PANTRY_ID + " = ?",
                new String[]{String.valueOf(item.getId())}
        );
    }

    // DELETE ALL: Removes every item from the pantry
    public void deleteAllPantryItems() {
        database.delete(DBHelper.TABLE_PANTRY, null, null);
    }
}