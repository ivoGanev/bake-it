package android.ivo.bake_it.screen.recipe;

import android.ivo.bake_it.Bundles;
import android.ivo.bake_it.R;
import android.ivo.bake_it.databinding.FragmentRecipeDetailBinding;
import android.ivo.bake_it.model.Recipe;
import android.ivo.bake_it.model.Step;
import android.ivo.bake_it.screen.main.MainActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RecipeDetailFragment extends Fragment {
    FragmentRecipeDetailBinding binding;

    Step step;

    public static Fragment newInstance(Bundle bundle) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle extras = getArguments();
        binding = FragmentRecipeDetailBinding.inflate(inflater, container, false);
        if (extras != null) {
            Step step = extras.getParcelable(Bundles.STEP_BUNDLE_KEY);
            if(step!=null) {
                binding.fragmentRecipeDetailDescription.setText(step.getDescription());
            }
        }
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        binding = null;
        step = null;
        super.onDestroy();
    }
}

