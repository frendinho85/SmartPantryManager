package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private List<PantryItem> pantryList;
    private OnItemClickListener listener;

    // Interface for click events (we will use this for Edit/Delete later)
    public interface OnItemClickListener {
        void onItemClick(PantryItem item);
    }

    public PantryAdapter(List<PantryItem> pantryList, OnItemClickListener listener) {
        this.pantryList = pantryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem currentItem = pantryList.get(position);
        holder.textViewName.setText(currentItem.getName());

        // Format the quantity and unit nicely (e.g., "2.0 kg")
        String quantityText = currentItem.getQuantity() + " " + currentItem.getUnit();
        holder.textViewQuantity.setText(quantityText);

        // Handle clicks
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pantryList.size();
    }

    // ViewHolder Class
    public static class PantryViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewQuantity;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
        }
    }
}