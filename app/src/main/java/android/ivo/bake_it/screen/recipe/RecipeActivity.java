package android.ivo.bake_it.screen.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.ivo.bake_it.BakeItApplication;
import android.ivo.bake_it.BundleKeys;
import android.ivo.bake_it.R;
import android.ivo.bake_it.model.Recipe;
import android.os.Bundle;

import org.jetbrains.annotations.NotNull;

public class RecipeActivity extends AppCompatActivity
        implements RecipeMasterFragment.OnStepClickedListener,
        RecipeDetailFragment.OnStepNavigationListener {

    BakeItApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        application = (BakeItApplication) getApplication();

        initializeMasterFragment();

        if (application.deviceIsTablet()) {
            inflateDetailsFragment(getStepBundle(0));
        }
    }

    private Recipe getNonNullRecipe(Bundle bundle) {
        if (bundle == null)
            throw new NullPointerException("The recipe recipeBundle can't be null.");
        Recipe recipe = bundle.getParcelable(BundleKeys.RECIPE_BUNDLE_KEY);
        if (recipe == null)
            throw new NullPointerException("The recipe can't be null");
        return recipe;
    }

    private void initializeMasterFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_recipe_fragment, RecipeMasterFragment.newInstance(getIntent().getExtras()))
                .commit();
    }

    private void inflateDetailsFragment(Bundle stepBundle) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_recipe_step_fragment, RecipeDetailFragment.newInstance(stepBundle))
                .commit();
    }

    @NotNull
    private Bundle getStepBundle(int position) {
        Bundle stepBundle = new Bundle();
        Recipe recipe = getNonNullRecipe(getIntent().getExtras());
        stepBundle.putParcelable(BundleKeys.STEP_BUNDLE_KEY, recipe.getSteps().get(position));
        return stepBundle;
    }

    @Override
    public void onClickNextStep() {

    }

    @Override
    public void onClickPreviousStep() {

    }

    @Override
    public void onStepButtonClicked(int position) {
        Recipe recipe = getNonNullRecipe(getIntent().getExtras());

        if (!application.deviceIsTablet()) {
            Intent intent = new Intent(this, StepActivity.class);
            intent.putExtra(BundleKeys.STEP_BUNDLE_KEY, recipe.getSteps().get(position));
            startActivity(intent);
        } else {
            RecipeDetailFragment detailFragment = (RecipeDetailFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.activity_recipe_step_fragment);
            if (detailFragment != null)
                detailFragment.updateUi(getStepBundle(position));
        }

    }
}