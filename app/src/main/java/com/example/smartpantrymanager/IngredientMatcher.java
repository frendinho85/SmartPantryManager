package com.example.smartpantrymanager;

import java.util.List;

public class IngredientMatcher {

    // ---------- NAME NORMALIZATION ----------
    public static String normalizeName(String name) {
        if (name == null) return "";
        String n = name.toLowerCase().trim().replaceAll("[^a-z0-9 ]", "");

        // Simple plural handling
        if (n.endsWith("ies") && n.length() > 3) {
            n = n.substring(0, n.length() - 3) + "y";     // berries -> berry
        } else if (n.endsWith("es") && n.length() > 2) {
            n = n.substring(0, n.length() - 2);            // tomatoes -> tomato
        } else if (n.endsWith("s") && !n.endsWith("ss") && n.length() > 1) {
            n = n.substring(0, n.length() - 1);            // eggs -> egg
        }
        return n;
    }

    // ---------- UNIT CONVERSION ----------
    // Converts everything to g (solids) or ml (liquids)
    public static double toBase(double qty, String unit) {
        if (unit == null) return qty;
        switch (unit.toLowerCase().trim()) {
            case "g": case "gram": case "grams":            return qty;
            case "kg": case "kilogram": case "kilograms":   return qty * 1000;
            case "ml": case "milliliter": case "milliliters": return qty;
            case "l": case "liter": case "liters":          return qty * 1000;
            case "tsp": case "teaspoon": case "teaspoons":  return qty * 5;
            case "tbsp": case "tablespoon": case "tablespoons": return qty * 15;
            case "cup": case "cups":                        return qty * 240;
            case "piece": case "pieces": case "pcs": case "": return qty;
            default: return qty;
        }
    }

    // ---------- SINGLE INGREDIENT CHECK ----------
    private static boolean pantryHasEnough(PantryItem available, RecipeIngredient required) {
        if (!normalizeName(available.getName()).equals(normalizeName(required.getName()))) {
            return false;
        }
        double availBase = toBase(available.getQuantity(), available.getUnit());
        double reqBase = toBase(required.getQuantity(), required.getUnit());
        return availBase >= reqBase;
    }

    // ---------- STRICT-MATCHING RULE ----------
    /**
     * Returns TRUE only if EVERY ingredient in the recipe is present in the pantry
     * in at least the required quantity. Returns FALSE otherwise.
     */
    public static boolean canMakeRecipe(Recipe recipe, List<PantryItem> pantry) {
        if (recipe.getIngredients() == null || recipe.getIngredients().isEmpty()) {
            return false;
        }

        for (RecipeIngredient required : recipe.getIngredients()) {
            boolean found = false;
            for (PantryItem available : pantry) {
                if (pantryHasEnough(available, required)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;  // ANY missing ingredient => recipe fails
            }
        }
        return true;  // ALL ingredients were found
    }

    // ---------- OPTIONAL: ALMOST THERE (BONUS) ----------
    /**
     * Returns TRUE only if the recipe is missing exactly ONE ingredient.
     */
    public static boolean isAlmostThere(Recipe recipe, List<PantryItem> pantry) {
        if (recipe.getIngredients() == null || recipe.getIngredients().isEmpty()) {
            return false;
        }

        int missing = 0;
        for (RecipeIngredient required : recipe.getIngredients()) {
            boolean found = false;
            for (PantryItem available : pantry) {
                if (pantryHasEnough(available, required)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                missing++;
                if (missing > 1) return false;  // more than 1 missing → not "almost there"
            }
        }
        return missing == 1;
    }
}