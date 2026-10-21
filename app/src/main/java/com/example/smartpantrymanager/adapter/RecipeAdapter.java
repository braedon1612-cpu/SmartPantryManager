package com.example.smartpantrymanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.model.Recipe;
import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.Holder> {
    public interface Listener { void onRecipeSelected(Recipe recipe); }
    private final List<Recipe> items = new ArrayList<>();
    private final Listener listener;
    private final java.util.Set<Long> appearedIds = new java.util.HashSet<>();


    public RecipeAdapter(Listener listener) {
        this.listener = listener;
        setHasStableIds(true);
    }

    public void submitItems(List<Recipe> updated) {
        int oldCount = items.size();
        items.clear();
        if (oldCount > 0) notifyItemRangeRemoved(0, oldCount);
        items.addAll(updated);
        if (!items.isEmpty()) notifyItemRangeInserted(0, items.size());
    }

    @Override public long getItemId(int position) { return items.get(position).getId(); }
    @Override public int getItemCount() { return items.size(); }

    @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        return new Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false));
    }

    @Override public void onBindViewHolder(@NonNull Holder holder, int position) {
        Recipe recipe = items.get(position);
        boolean firstAppearance = appearedIds.add(recipe.getId());
        com.example.smartpantrymanager.ui.RowMotion.bind(
                holder.itemView, firstAppearance && position < 8, position);

        holder.name.setText(recipe.getName());
        holder.count.setText(holder.itemView.getContext().getString(
                R.string.recipe_count, recipe.getRequiredIngredients().size()));
        holder.itemView.findViewById(R.id.viewRecipe)
                .setOnClickListener(v -> listener.onRecipeSelected(recipe));
    }

    @Override public void onViewRecycled(@NonNull Holder holder) {
        com.example.smartpantrymanager.ui.RowMotion.reset(holder.itemView);
        super.onViewRecycled(holder);
    }

    static class Holder extends RecyclerView.ViewHolder {
        final TextView name, count;
        Holder(View view) {
            super(view);
            name = view.findViewById(R.id.recipeName);
            count = view.findViewById(R.id.recipeCount);
        }
    }
}
