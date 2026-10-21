package com.example.smartpantrymanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.model.Ingredient;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.Holder> {
    public interface Listener {
        void onEdit(Ingredient ingredient);
        void onDelete(Ingredient ingredient);
    }
    private final List<Ingredient> items = new ArrayList<>();
    private final Listener listener;
    private final java.util.Set<Long> appearedIds = new java.util.HashSet<>();


    public PantryAdapter(Listener listener) {
        this.listener = listener;
        setHasStableIds(true);
    }

    public void submitItems(List<Ingredient> updated) {
        int oldCount = items.size();
        items.clear();
        if (oldCount > 0) notifyItemRangeRemoved(0, oldCount);
        items.addAll(updated);
        if (!items.isEmpty()) notifyItemRangeInserted(0, items.size());
    }

    @Override public long getItemId(int position) { return items.get(position).getId(); }
    @Override public int getItemCount() { return items.size(); }

    @NonNull @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        return new Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false));
    }

    @Override public void onBindViewHolder(@NonNull Holder holder, int position) {
        Ingredient item = items.get(position);
        boolean firstAppearance = appearedIds.add(item.getId());
        com.example.smartpantrymanager.ui.RowMotion.bind(
                holder.itemView, firstAppearance && position < 8, position);

        holder.name.setText(item.getName());
        holder.amount.setText(holder.itemView.getContext().getString(R.string.stock_amount,
                NumberFormat.getNumberInstance().format(item.getQuantity()), item.getUnit()));
        holder.itemView.findViewById(R.id.editIngredient)
                .setOnClickListener(v -> listener.onEdit(item));
        holder.itemView.findViewById(R.id.deleteIngredient)
                .setOnClickListener(v -> listener.onDelete(item));
    }

    @Override public void onViewRecycled(@NonNull Holder holder) {
        com.example.smartpantrymanager.ui.RowMotion.reset(holder.itemView);
        super.onViewRecycled(holder);
    }

    static class Holder extends RecyclerView.ViewHolder {
        final TextView name, amount;
        Holder(View view) {
            super(view);
            name = view.findViewById(R.id.ingredientName);
            amount = view.findViewById(R.id.ingredientAmount);
        }
    }
}
