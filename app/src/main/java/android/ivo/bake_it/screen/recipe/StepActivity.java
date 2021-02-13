package android.ivo.bake_it.screen.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.ivo.bake_it.BakeItApplication;
import android.ivo.bake_it.R;
import android.ivo.bake_it.model.Recipe;
import android.ivo.bake_it.screen.main.MainActivity;
import android.os.Bundle;
import android.widget.Toast;

import timber.log.Timber;

public class StepActivity extends AppCompatActivity implements RecipeDetailFragment.OnStepNavigationListener {

    public static final String STEP_BUNDLE_KEY = "android.ivo.bake_it.bundle_keys.step";
    public static final String CURRENT_STEP_PAGE = "android.ivo.bake_it.bundle_keys.current_step_index";
    Bundle extras;
    Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        BakeItApplication bakeItApplication = (BakeItApplication) getApplication();

        extras = getIntent().getExtras();
        recipe = getNonNullRecipeFromBundle(extras);
        int currentStepPage = extras.getInt(CURRENT_STEP_PAGE);

        if (extras != null) {
            for (String s : extras.keySet()) {
                Timber.d(s);
            }
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_step_fragment, RecipeDetailFragment.newInstance(toStepBundle(currentStepPage)))
                .commit();
    }

    @Override
    public void onClickNextStep(int position) {
        position++;
        if (position >= recipe.getSteps().size()) {
            Toast.makeText(this, "You are currently viewing the last step.", Toast.LENGTH_SHORT).show();
            return;
        }
        updateDetailFragment(position);
    }

    @Override
    public void onClickPreviousStep(int position) {
        position--;
        if (position < 0) {
            Toast.makeText(this, "You are currently viewing the first step.", Toast.LENGTH_SHORT).show();
            return;
        }
        updateDetailFragment(position);
    }

    /*
     * Sends STEP_BUNDLE_KEY and CURRENT_PAGE to details fragment and updates the details fragment UI.
     * */
    private void updateDetailFragment(int position) {
        RecipeDetailFragment detailFragment = (RecipeDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_step_fragment);
        if (detailFragment != null)
            detailFragment.updateUi(toStepBundle(position));
    }

    private Bundle toStepBundle(int recipePosition) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STEP_BUNDLE_KEY, recipe.getSteps().get(recipePosition));
        bundle.putInt(CURRENT_STEP_PAGE, recipePosition);
        return bundle;
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