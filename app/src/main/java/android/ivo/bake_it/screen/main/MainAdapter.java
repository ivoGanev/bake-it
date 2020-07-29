package android.ivo.bake_it.screen.main;

import android.ivo.bake_it.R;
import android.ivo.bake_it.databinding.ItemRecipeBinding;
import android.ivo.bake_it.model.Recipe;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private List<Recipe> recipes;
    private OnViewItemClickedListener onViewItemClickedListener;

    public MainAdapter(List<Recipe> recipes, OnViewItemClickedListener onItemClickListener) {
        this.recipes = recipes;
        this.onViewItemClickedListener = onItemClickListener;
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ItemRecipeBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemRecipeBinding.bind(itemView);
            binding.itemRecipeName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(onViewItemClickedListener!=null)
                onViewItemClickedListener.onRecipeClicked(getAdapterPosition());
        }
    }

    public interface OnViewItemClickedListener
    {
        void onRecipeClicked(int position);
    }
}