package android.ivo.bake_it.screen.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.ivo.bake_it.BakeItApplication;
import android.ivo.bake_it.R;
import android.ivo.bake_it.model.Recipe;
import android.ivo.bake_it.screen.main.MainActivity;
import android.os.Bundle;

import timber.log.Timber;

public class RecipeActivity extends AppCompatActivity implements RecipeMasterFragment.OnStepClickedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Bundle extras = getIntent().getExtras();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_recipe_fragment, RecipeMasterFragment.newInstance(extras))
                .commit();
        BakeItApplication application = (BakeItApplication)getApplication();
        if(application.deviceIsTablet()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_recipe_step_fragment, new RecipeDetailFragment())
                    .commit();
        }
    }

    @Override
    public void onStepButtonClicked(int position) {
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            Recipe recipe = extras.getParcelable(MainActivity.RECIPE_BUNDLE_KEY);
            if(recipe!=null) {
                Timber.d(recipe.getSteps().get(position).toString());

                // load the activity for details if its the phone
                Intent intent = new Intent(this, StepActivity.class);
                startActivity(intent);
                // otherwise the layout will load the master list fragment and the
                // details fragment
            }
        }

    }
}