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

class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

    List<Step> steps;

    public StepsAdapter(@NotNull List<Step> steps) {
        this.steps = steps;
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
        if (position != 0) {
            String longDescription = step.getDescription();
            if (longDescription.startsWith(position + ". "))
                longDescription = longDescription.replaceFirst(position + ". ", "");

            holder.binding.itemStepLongDescription.setText(longDescription);
        }


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

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemStepBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemStepBinding.bind(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            View hiddenGroup = binding.itemStepHiddenGroup;
            if (hiddenGroup.getVisibility() == View.GONE) {
                hiddenGroup.setVisibility(View.VISIBLE);
            } else {
                hiddenGroup.setVisibility(View.GONE);
            }
        }
    }
}
