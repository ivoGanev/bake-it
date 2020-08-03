package android.ivo.bake_it.screen.recipe;

import android.ivo.bake_it.databinding.ActivityRecipeMasterBinding;
import android.ivo.bake_it.model.Recipe;
import android.ivo.bake_it.screen.main.MainActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeMasterFragment extends Fragment {

    ActivityRecipeMasterBinding binding;

    IngredientsAdapter ingredientsAdapter;

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment = new RecipeMasterFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityRecipeMasterBinding.inflate(inflater, container, false);
        Bundle extras = getArguments();
        if (extras != null) {
            Recipe recipe = extras.getParcelable(MainActivity.RECIPE_BUNDLE_KEY);
            if(recipe!=null) {
                binding.activityRecipeTitle.setText(recipe.getName());
                ingredientsAdapter = new IngredientsAdapter(recipe.getIngredients());
                binding.activityRecipeRvIngredients.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
                binding.activityRecipeRvIngredients.setHasFixedSize(true);
                binding.activityRecipeRvIngredients.setAdapter(ingredientsAdapter);
            }
        }
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        binding = null;
        ingredientsAdapter = null;
        super.onDestroy();
    }
}
