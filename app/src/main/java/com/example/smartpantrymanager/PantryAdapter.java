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
    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

    // Tap listener (for Edit)
    public interface OnItemClickListener {
        void onItemClick(PantryItem item);
    }

    // Long-press listener (for Delete)
    public interface OnItemLongClickListener {
        void onItemLongClick(PantryItem item);
    }

    public PantryAdapter(List<PantryItem> pantryList,
                         OnItemClickListener clickListener,
                         OnItemLongClickListener longClickListener) {
        this.pantryList = pantryList;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
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

        String quantityText = currentItem.getQuantity() + " " + currentItem.getUnit();
        holder.textViewQuantity.setText(quantityText);

        // Tap → Edit
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onItemClick(currentItem);
            }
        });

        // Long-press → Delete
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(currentItem);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return pantryList.size();
    }

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