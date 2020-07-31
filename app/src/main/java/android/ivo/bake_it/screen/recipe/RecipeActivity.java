package android.ivo.bake_it.screen.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.ivo.bake_it.R;
import android.ivo.bake_it.model.Recipe;
import android.ivo.bake_it.screen.main.MainActivity;
import android.os.Bundle;
import android.os.Parcelable;

import java.util.ArrayList;

import timber.log.Timber;

public class RecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        Recipe recipe = extras.getParcelable(MainActivity.RECIPE_BUNDLE_KEY);
        Timber.d(recipe.toString());
    }
}