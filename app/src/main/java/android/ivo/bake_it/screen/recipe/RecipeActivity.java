package android.ivo.bake_it.screen.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.ivo.bake_it.BakeItApplication;
import android.ivo.bake_it.Bundles;
import android.ivo.bake_it.R;
import android.ivo.bake_it.model.Recipe;
import android.os.Bundle;

public class RecipeActivity extends AppCompatActivity implements RecipeMasterFragment.OnStepClickedListener {

    boolean deviceIsTablet;

    Recipe recipe;

    Bundle recipeBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        BakeItApplication application = (BakeItApplication) getApplication();
        deviceIsTablet = application.deviceIsTablet();
        recipeBundle = getIntent().getExtras();
        recipe = recipeBundle.getParcelable(Bundles.RECIPE_BUNDLE_KEY);

        initializeFragments();
    }

    private void initializeFragments() {
        initializeRecipeMasterFragment();
        if (deviceIsTablet) {
          initializeRecipeDetailsFragment();
        }
    }

    private void initializeRecipeMasterFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_recipe_fragment, RecipeMasterFragment.newInstance(recipeBundle))
                .commit();
    }

    private void initializeRecipeDetailsFragment() {
        initRecipeDetailFragmentWithPosition(0);
    }

    private void initRecipeDetailFragmentWithPosition(int position) {
        // initialize the first recipe on the list
        Bundle stepBundle = new Bundle();
        stepBundle.putParcelable(Bundles.STEP_BUNDLE_KEY, recipe.getSteps().get(position));

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_recipe_step_fragment, RecipeDetailFragment.newInstance(stepBundle))
                .commit();
    }

    @Override
    public void onStepButtonClicked(int position) {
        if (recipeBundle != null && recipe != null) {
            if (!deviceIsTablet) {
                Intent intent = new Intent(this, StepActivity.class);
                intent.putExtra(Bundles.STEP_BUNDLE_KEY,  recipe.getSteps().get(position));
                startActivity(intent);
            } else {
                initRecipeDetailFragmentWithPosition(position);
            }
        }
    }
}