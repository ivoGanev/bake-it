package android.ivo.bake_it.screen.recipe;

import android.ivo.bake_it.HttpMediaFormat;
import android.ivo.bake_it.R;
import android.ivo.bake_it.databinding.ItemStepBinding;
import android.ivo.bake_it.model.Step;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import timber.log.Timber;

class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

    private OnViewItemClickListener onViewItemClickListener;

    private List<Step> steps;

    public StepsAdapter(@NotNull List<Step> steps) {
        this.steps = steps;
    }

    public void setOnViewItemClickListener(OnViewItemClickListener onViewItemClickListener) {
        this.onViewItemClickListener = onViewItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_step, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Step step = steps.get(position);

        holder.binding.itemStepShortDescription.setText(position + ") " + step.getShortDescription());

        HttpMediaFormat httpMediaFormat = new HttpMediaFormat(step.getThumbnailURL());
        if (httpMediaFormat.getFormat() == HttpMediaFormat.MP4) {
            // load the movie
        } else {
            // media stays hidden
        }
    }

    @Override
    public int getItemCount() {
        if (steps != null)
            return steps.size();
        return 0;
    }

    public interface OnViewItemClickListener {
        void onRecipeClicked(View view, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemStepBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemStepBinding.bind(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onViewItemClickListener != null)
                onViewItemClickListener.onRecipeClicked(v, getAdapterPosition());
        }
    }
}
