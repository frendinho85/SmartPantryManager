package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;
    private OnRecipeClickListener listener;

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public RecipeAdapter(List<Recipe> recipeList, OnRecipeClickListener listener) {
        this.recipeList = recipeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe currentRecipe = recipeList.get(position);
        holder.textViewRecipeName.setText(currentRecipe.getName());

        int ingredientCount = currentRecipe.getIngredients() != null
                ? currentRecipe.getIngredients().size() : 0;
        holder.textViewRecipeIngredients.setText(ingredientCount + " ingredients required");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRecipeClick(currentRecipe);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewRecipeName;
        public TextView textViewRecipeIngredients;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRecipeName = itemView.findViewById(R.id.textViewRecipeName);
            textViewRecipeIngredients = itemView.findViewById(R.id.textViewRecipeIngredients);
        }
    }
}