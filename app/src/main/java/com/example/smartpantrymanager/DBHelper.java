package com.example.smartpantrymanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // Database Constants
    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 1;

    // --- Pantry Table ---
    public static final String TABLE_PANTRY = "pantry_items";
    public static final String COLUMN_PANTRY_ID = "id";
    public static final String COLUMN_PANTRY_NAME = "name";
    public static final String COLUMN_PANTRY_QUANTITY = "quantity";
    public static final String COLUMN_PANTRY_UNIT = "unit";
    public static final String COLUMN_PANTRY_EXPIRY = "expiry_date";

    // --- Recipes Table ---
    public static final String TABLE_RECIPES = "recipes";
    public static final String COLUMN_RECIPE_ID = "id";
    public static final String COLUMN_RECIPE_NAME = "name";
    public static final String COLUMN_RECIPE_STEPS = "steps";

    // --- Recipe Ingredients Table ---
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";
    public static final String COLUMN_ING_ID = "id";
    public static final String COLUMN_ING_RECIPE_ID = "recipe_id";
    public static final String COLUMN_ING_NAME = "name";
    public static final String COLUMN_ING_QUANTITY = "quantity";
    public static final String COLUMN_ING_UNIT = "unit";

    // Constructor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Create Pantry Table
        String CREATE_PANTRY_TABLE = "CREATE TABLE " + TABLE_PANTRY + "("
                + COLUMN_PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PANTRY_NAME + " TEXT,"
                + COLUMN_PANTRY_QUANTITY + " REAL,"
                + COLUMN_PANTRY_UNIT + " TEXT,"
                + COLUMN_PANTRY_EXPIRY + " TEXT" + ")";
        db.execSQL(CREATE_PANTRY_TABLE);

        // 2. Create Recipes Table
        String CREATE_RECIPES_TABLE = "CREATE TABLE " + TABLE_RECIPES + "("
                + COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_RECIPE_NAME + " TEXT,"
                + COLUMN_RECIPE_STEPS + " TEXT" + ")";
        db.execSQL(CREATE_RECIPES_TABLE);

        // 3. Create Recipe Ingredients Table
        String CREATE_INGREDIENTS_TABLE = "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + "("
                + COLUMN_ING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ING_RECIPE_ID + " INTEGER,"
                + COLUMN_ING_NAME + " TEXT,"
                + COLUMN_ING_QUANTITY + " REAL,"
                + COLUMN_ING_UNIT + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_ING_RECIPE_ID + ") REFERENCES " + TABLE_RECIPES + "(" + COLUMN_RECIPE_ID + "))";
        db.execSQL(CREATE_INGREDIENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);

        // Create tables again
        onCreate(db);
    }
}