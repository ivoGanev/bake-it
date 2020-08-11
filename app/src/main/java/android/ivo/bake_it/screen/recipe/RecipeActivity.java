package android.ivo.bake_it.screen.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.ivo.bake_it.BakeItApplication;
import android.ivo.bake_it.BundleKeys;
import android.ivo.bake_it.R;
import android.ivo.bake_it.model.Recipe;
import android.ivo.bake_it.model.Step;
import android.os.Bundle;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class RecipeActivity extends AppCompatActivity
        implements RecipeMasterFragment.OnStepClickedListener,
        RecipeDetailFragment.OnStepNavigationListener {

    BakeItApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        application = (BakeItApplication) getApplication();

        inflateMasterFragment();

        if (application.deviceIsTablet()) {
            inflateDetailsFragment(getStepBundle(0));
        }
    }

    private void inflateMasterFragment() {
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
        Recipe recipe = getNonNullRecipe();
        stepBundle.putParcelable(BundleKeys.STEP_BUNDLE_KEY, recipe.getSteps().get(position));
        stepBundle.putInt(BundleKeys.STEP_DETAILS_CURRENT_PAGE, position);
        return stepBundle;
    }

    @Override
    public void onClickNextStep(int position) {
        Recipe recipe = getNonNullRecipe();
        position++;
        if(position >= recipe.getSteps().size()) {
            Toast.makeText(application, "You are currently viewing the last step.", Toast.LENGTH_SHORT).show();
            return;
        }
        updateDetailFragment(position);
    }

    @Override
    public void onClickPreviousStep(int position) {
        position--;
        if(position < 0) {
            Toast.makeText(application, "You are currently viewing the first step.", Toast.LENGTH_SHORT).show();
            return;
        }
        updateDetailFragment(position);
    }

    @Override
    public void onStepButtonClicked(int position) {
        if (!application.deviceIsTablet()) {
            Recipe recipe = getNonNullRecipe();
            Step value = recipe.getSteps().get(position);
            startStepActivity(value);
        } else {
            updateDetailFragment(position);
        }
    }

    private void updateDetailFragment(int position) {
        RecipeDetailFragment detailFragment = (RecipeDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_recipe_step_fragment);
        if (detailFragment != null)
            detailFragment.updateUi(getStepBundle(position));
    }

    private void startStepActivity(Step value) {
        Intent intent = new Intent(this, StepActivity.class);
        intent.putExtra(BundleKeys.STEP_BUNDLE_KEY, value);
        startActivity(intent);
    }

    private Recipe getNonNullRecipe() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            throw new NullPointerException("The recipe recipeBundle can't be null.");
        Recipe recipe = bundle.getParcelable(BundleKeys.RECIPE_BUNDLE_KEY);
        if (recipe == null)
            throw new NullPointerException("The recipe can't be null");
        return recipe;
    }
}