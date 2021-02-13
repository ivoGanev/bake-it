package android.ivo.bake_it.screen.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.ivo.bake_it.BakeItApplication;
import android.ivo.bake_it.R;
import android.ivo.bake_it.model.Recipe;
import android.ivo.bake_it.model.Step;
import android.ivo.bake_it.screen.main.MainActivity;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.widget.Toast;

import timber.log.Timber;

import static timber.log.Timber.d;

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
            inflateDetailsFragment(toStepBundle(0));
        }
    }

    private void inflateMasterFragment() {
        Bundle extras = getIntent().getExtras();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_recipe_fragment, RecipeMasterFragment.newInstance(extras))
                .commit();
    }

    private void inflateDetailsFragment(Bundle stepBundle) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_recipe_step_fragment, RecipeDetailFragment.newInstance(stepBundle))
                .commit();
    }

    @Override
    public void onClickNextStep(int position) {
        Recipe recipe = getNonNullRecipeFromBundle(getIntent().getExtras());
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
            Recipe recipe = getNonNullRecipeFromBundle(getIntent().getExtras());
            startStepActivity(recipe, position);
        } else {
            updateDetailFragment(position);
        }
    }

    private void startStepActivity(Recipe value, int position) {
        Intent intent = new Intent(this, StepActivity.class);

        intent.putExtra(MainActivity.RECIPE_BUNDLE_KEY, value);
        intent.putExtra(StepActivity.CURRENT_STEP_PAGE, position);

        startActivity(intent);
    }

    private void updateDetailFragment(int position) {
        RecipeDetailFragment detailFragment = (RecipeDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_recipe_step_fragment);
        if (detailFragment != null)
            detailFragment.updateUi(toStepBundle(position));
    }

    private Bundle toStepBundle(int recipePosition) {
        Bundle stepBundle = new Bundle();
        Recipe recipe = getNonNullRecipeFromBundle(getIntent().getExtras());
        stepBundle.putParcelable(StepActivity.STEP_BUNDLE_KEY, recipe.getSteps().get(recipePosition));
        stepBundle.putInt(StepActivity.CURRENT_STEP_PAGE, recipePosition);
        return stepBundle;
    }

    private Recipe getNonNullRecipeFromBundle(Bundle bundle) {
        if (bundle == null)
            throw new NullPointerException("The recipe recipeBundle can't be null.");
        Recipe recipe = bundle.getParcelable(MainActivity.RECIPE_BUNDLE_KEY);
        if (recipe == null)
            throw new NullPointerException("The recipe can't be null");
        return recipe;
    }
}