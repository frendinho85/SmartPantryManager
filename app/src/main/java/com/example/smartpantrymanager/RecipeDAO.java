/**
 * Smart Pantry Manager - Recipe Data Access Object
 * Student: Mmametlafrederick Moloto (402411236)
 * Module: Mobile App Development 700
 *
 * This class handles retrieving recipes and their associated ingredients
 * from the recipes and recipe_ingredients tables.
 */
package com.example.smartpantrymanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class RecipeDAO {

    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public RecipeDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() {
        database = dbHelper.getReadableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Retrieves all recipes from the database and attaches their required ingredients
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        Cursor cursor = database.query(
                DBHelper.TABLE_RECIPES,
                null, null, null, null, null, null
        );

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Recipe recipe = new Recipe(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_RECIPE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_RECIPE_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_RECIPE_STEPS))
                );
                recipes.add(recipe);
                cursor.moveToNext();
            }
            cursor.close();
        }

        // Now attach the ingredients to each recipe
        for (Recipe r : recipes) {
            r.setIngredients(getIngredientsForRecipe(r.getId()));
        }

        return recipes;
    }

    // Retrieves a single recipe by its ID, including its full ingredient list
    public Recipe getRecipeById(int recipeId) {
        Cursor cursor = database.query(
                DBHelper.TABLE_RECIPES,
                null,
                DBHelper.COLUMN_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipeId)},
                null, null, null
        );

        Recipe recipe = null;
        if (cursor != null && cursor.moveToFirst()) {
            recipe = new Recipe(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_RECIPE_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_RECIPE_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_RECIPE_STEPS))
            );
            recipe.setIngredients(getIngredientsForRecipe(recipeId));
            cursor.close();
        }
        return recipe;
    }

    /**
     * Get all ingredients for a specific recipe.
     */
    public List<RecipeIngredient> getIngredientsForRecipe(int recipeId) {
        List<RecipeIngredient> ingredients = new ArrayList<>();

        Cursor cursor = database.query(
                DBHelper.TABLE_RECIPE_INGREDIENTS,
                null,
                DBHelper.COLUMN_ING_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipeId)},
                null, null, null
        );

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                RecipeIngredient ing = new RecipeIngredient(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ING_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ING_RECIPE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ING_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ING_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ING_UNIT))
                );
                ingredients.add(ing);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return ingredients;
    }

    /**
     * Get all recipes that contain a specific ingredient name.
     * Useful for the "Almost There" bonus feature.
     */
    public List<Recipe> getRecipesContainingIngredient(String ingredientName) {
        List<Recipe> matchingRecipes = new ArrayList<>();

        String query = "SELECT DISTINCT r." + DBHelper.COLUMN_RECIPE_ID + ", "
                + "r." + DBHelper.COLUMN_RECIPE_NAME + ", "
                + "r." + DBHelper.COLUMN_RECIPE_STEPS
                + " FROM " + DBHelper.TABLE_RECIPES + " r"
                + " INNER JOIN " + DBHelper.TABLE_RECIPE_INGREDIENTS + " i"
                + " ON r." + DBHelper.COLUMN_RECIPE_ID + " = i." + DBHelper.COLUMN_ING_RECIPE_ID
                + " WHERE LOWER(i." + DBHelper.COLUMN_ING_NAME + ") LIKE ?";

        Cursor cursor = database.rawQuery(query, new String[]{"%" + ingredientName.toLowerCase() + "%"});

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Recipe recipe = new Recipe(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                );
                matchingRecipes.add(recipe);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return matchingRecipes;
    }
}