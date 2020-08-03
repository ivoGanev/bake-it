package android.ivo.bake_it.screen.recipe;

import android.ivo.bake_it.R;
import android.ivo.bake_it.databinding.ItemIngredientBinding;
import android.ivo.bake_it.model.Ingredient;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {
    List<Ingredient> ingredients;

    public IngredientsAdapter(@NotNull List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.binding.ingredientRvItemName.setText(ingredient.getIngredient());
        holder.binding.ingredientRvItemQuantity.setText(ingredient.getQuantityToString());
        holder.binding.ingredientRvItemMeasure.setText(ingredient.getMeasure());
    }

    @Override
    public int getItemCount() {
        if (ingredients != null)
            return ingredients.size();
        else
            return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ItemIngredientBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemIngredientBinding.bind(itemView);
        }
    }
}
