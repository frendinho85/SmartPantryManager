/**
 * Smart Pantry Manager - Database Helper
 * Student: Mmametlafrederick Moloto (402411236)
 * Module: Mobile App Development 700
 *
 * This class manages the creation, upgrading, and seeding of the SQLite database.
 */
package com.example.smartpantrymanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 2;

    // Pantry Table
    public static final String TABLE_PANTRY = "pantry_items";
    public static final String COLUMN_PANTRY_ID = "id";
    public static final String COLUMN_PANTRY_NAME = "name";
    public static final String COLUMN_PANTRY_QUANTITY = "quantity";
    public static final String COLUMN_PANTRY_UNIT = "unit";
    public static final String COLUMN_PANTRY_EXPIRY = "expiry_date";

    // Recipes Table
    public static final String TABLE_RECIPES = "recipes";
    public static final String COLUMN_RECIPE_ID = "id";
    public static final String COLUMN_RECIPE_NAME = "name";
    public static final String COLUMN_RECIPE_STEPS = "steps";

    // Recipe Ingredients Table
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";
    public static final String COLUMN_ING_ID = "id";
    public static final String COLUMN_ING_RECIPE_ID = "recipe_id";
    public static final String COLUMN_ING_NAME = "name";
    public static final String COLUMN_ING_QUANTITY = "quantity";
    public static final String COLUMN_ING_UNIT = "unit";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creates the database tables when the app is launched for the first time.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PANTRY_TABLE = "CREATE TABLE " + TABLE_PANTRY + "("
                + COLUMN_PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PANTRY_NAME + " TEXT,"
                + COLUMN_PANTRY_QUANTITY + " REAL,"
                + COLUMN_PANTRY_UNIT + " TEXT,"
                + COLUMN_PANTRY_EXPIRY + " TEXT" + ")";
        db.execSQL(CREATE_PANTRY_TABLE);

        String CREATE_RECIPES_TABLE = "CREATE TABLE " + TABLE_RECIPES + "("
                + COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_RECIPE_NAME + " TEXT,"
                + COLUMN_RECIPE_STEPS + " TEXT" + ")";
        db.execSQL(CREATE_RECIPES_TABLE);

        String CREATE_INGREDIENTS_TABLE = "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + "("
                + COLUMN_ING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ING_RECIPE_ID + " INTEGER,"
                + COLUMN_ING_NAME + " TEXT,"
                + COLUMN_ING_QUANTITY + " REAL,"
                + COLUMN_ING_UNIT + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_ING_RECIPE_ID + ") REFERENCES "
                + TABLE_RECIPES + "(" + COLUMN_RECIPE_ID + "))";
        db.execSQL(CREATE_INGREDIENTS_TABLE);

        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        onCreate(db);
    }

    // Seeds 18 pre-loaded recipes into the database so users have data to match against.
    private void seedRecipes(SQLiteDatabase db) {

        insertRecipe(db, "Scrambled Eggs",
                "1. Whisk eggs with milk. 2. Melt butter in a pan. 3. Cook eggs until firm. 4. Serve hot.",
                new String[]{"eggs", "milk", "butter"},
                new double[]{3, 50, 10},
                new String[]{"piece", "ml", "g"});

        insertRecipe(db, "Tomato Pasta",
                "1. Boil pasta. 2. Fry garlic in olive oil. 3. Add tomatoes and simmer. 4. Toss with pasta.",
                new String[]{"pasta", "tomatoes", "garlic", "olive oil"},
                new double[]{200, 4, 2, 15},
                new String[]{"g", "piece", "piece", "ml"});

        insertRecipe(db, "Pancakes",
                "1. Mix flour, sugar, milk, and eggs. 2. Heat butter in a pan. 3. Pour batter and cook both sides.",
                new String[]{"flour", "sugar", "milk", "eggs", "butter"},
                new double[]{200, 30, 250, 2, 20},
                new String[]{"g", "g", "ml", "piece", "g"});

        insertRecipe(db, "Cheese Omelette",
                "1. Beat eggs. 2. Pour into hot pan with butter. 3. Add cheese, fold, and serve.",
                new String[]{"eggs", "cheese", "butter", "milk"},
                new double[]{3, 50, 10, 20},
                new String[]{"piece", "g", "g", "ml"});

        insertRecipe(db, "Garlic Bread",
                "1. Slice bread. 2. Mix butter with garlic. 3. Spread and bake at 180C for 10 min.",
                new String[]{"bread", "butter", "garlic"},
                new double[]{4, 50, 3},
                new String[]{"piece", "g", "piece"});

        insertRecipe(db, "Vegetable Soup",
                "1. Chop vegetables. 2. Boil in stock for 25 min. 3. Season and serve.",
                new String[]{"carrots", "potatoes", "onions", "stock"},
                new double[]{3, 4, 2, 500},
                new String[]{"piece", "piece", "piece", "ml"});

        insertRecipe(db, "Chicken Rice",
                "1. Cook rice. 2. Fry chicken with onions. 3. Mix together and season.",
                new String[]{"rice", "chicken", "onions", "olive oil"},
                new double[]{200, 300, 1, 15},
                new String[]{"g", "g", "piece", "ml"});

        insertRecipe(db, "Greek Salad",
                "1. Chop tomatoes, cucumber, and onion. 2. Add feta and olives. 3. Drizzle olive oil.",
                new String[]{"tomatoes", "cucumber", "onions", "feta cheese", "olives", "olive oil"},
                new double[]{3, 1, 1, 100, 50, 20},
                new String[]{"piece", "piece", "piece", "g", "g", "ml"});

        insertRecipe(db, "Banana Smoothie",
                "1. Blend bananas, milk, and honey. 2. Serve cold.",
                new String[]{"bananas", "milk", "honey"},
                new double[]{2, 250, 20},
                new String[]{"piece", "ml", "g"});

        insertRecipe(db, "Cheese Toastie",
                "1. Butter the bread. 2. Add cheese. 3. Grill until golden.",
                new String[]{"bread", "cheese", "butter"},
                new double[]{2, 60, 15},
                new String[]{"piece", "g", "g"});

        insertRecipe(db, "Fried Rice",
                "1. Cook rice. 2. Fry eggs, then add rice and soy sauce. 3. Stir-fry for 3 min.",
                new String[]{"rice", "eggs", "soy sauce", "olive oil", "onions"},
                new double[]{200, 2, 30, 15, 1},
                new String[]{"g", "piece", "ml", "ml", "piece"});

        insertRecipe(db, "Tuna Sandwich",
                "1. Mix tuna with mayonnaise. 2. Spread on bread. 3. Add lettuce and serve.",
                new String[]{"tuna", "mayonnaise", "bread", "lettuce"},
                new double[]{150, 30, 2, 3},
                new String[]{"g", "g", "piece", "piece"});

        insertRecipe(db, "Pumpkin Soup",
                "1. Chop pumpkin and onion. 2. Boil in stock. 3. Blend smooth. 4. Add cream.",
                new String[]{"pumpkin", "onions", "stock", "cream"},
                new double[]{500, 1, 500, 100},
                new String[]{"g", "piece", "ml", "ml"});

        insertRecipe(db, "Mashed Potatoes",
                "1. Boil potatoes. 2. Mash with butter and milk. 3. Season to taste.",
                new String[]{"potatoes", "butter", "milk"},
                new double[]{4, 30, 100},
                new String[]{"piece", "g", "ml"});

        insertRecipe(db, "French Toast",
                "1. Beat eggs with milk. 2. Dip bread. 3. Fry in butter until golden.",
                new String[]{"bread", "eggs", "milk", "butter"},
                new double[]{4, 2, 100, 20},
                new String[]{"piece", "piece", "ml", "g"});

        insertRecipe(db, "Tomato Soup",
                "1. Boil tomatoes with onions. 2. Blend. 3. Add stock and cream.",
                new String[]{"tomatoes", "onions", "stock", "cream"},
                new double[]{5, 1, 400, 80},
                new String[]{"piece", "piece", "ml", "ml"});

        insertRecipe(db, "Rice Pudding",
                "1. Boil rice in milk. 2. Add sugar and stir. 3. Serve with cinnamon.",
                new String[]{"rice", "milk", "sugar"},
                new double[]{100, 500, 50},
                new String[]{"g", "ml", "g"});

        insertRecipe(db, "Omelette with Onion",
                "1. Beat eggs. 2. Fry onions in butter. 3. Add eggs and cook. 4. Fold and serve.",
                new String[]{"eggs", "onions", "butter"},
                new double[]{3, 1, 10},
                new String[]{"piece", "piece", "g"});
    }

    private void insertRecipe(SQLiteDatabase db, String name, String steps,
                              String[] ingNames, double[] ingQty, String[] ingUnits) {

        String insertRecipeSQL = "INSERT INTO " + TABLE_RECIPES
                + " (" + COLUMN_RECIPE_NAME + ", " + COLUMN_RECIPE_STEPS + ") VALUES (?, ?)";
        db.execSQL(insertRecipeSQL, new Object[]{name, steps});

        Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
        cursor.moveToFirst();
        int recipeId = cursor.getInt(0);
        cursor.close();

        for (int i = 0; i < ingNames.length; i++) {
            String insertIng = "INSERT INTO " + TABLE_RECIPE_INGREDIENTS
                    + " (" + COLUMN_ING_RECIPE_ID + ", " + COLUMN_ING_NAME + ", "
                    + COLUMN_ING_QUANTITY + ", " + COLUMN_ING_UNIT + ") VALUES (?, ?, ?, ?)";
            db.execSQL(insertIng, new Object[]{recipeId, ingNames[i], ingQty[i], ingUnits[i]});
        }
    }
}