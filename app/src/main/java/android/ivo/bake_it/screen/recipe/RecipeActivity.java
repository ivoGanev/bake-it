package android.ivo.bake_it.screen.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.ivo.bake_it.R;
import android.ivo.bake_it.databinding.ActivityRecipeBinding;
import android.ivo.bake_it.model.Recipe;
import android.ivo.bake_it.screen.main.MainActivity;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

import timber.log.Timber;

public class RecipeActivity extends AppCompatActivity {
    ActivityRecipeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            Recipe recipe = extras.getParcelable(MainActivity.RECIPE_BUNDLE_KEY);
            binding.activityRecipeTitle.setText(recipe.getName());
        }
      //  Timber.d(recipe1.toString());
    }
}