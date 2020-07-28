package android.ivo.bake_it.screen.main;

import android.ivo.bake_it.R;
import android.ivo.bake_it.databinding.ItemRecipeBinding;
import android.ivo.bake_it.model.Recipe;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private List<Recipe> recipes;

    public MainAdapter(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.itemRecipeName.setText(recipes.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        ItemRecipeBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemRecipeBinding.bind(itemView);
        }
    }

}
