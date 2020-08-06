package android.ivo.bake_it.screen.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.ivo.bake_it.BakeItApplication;
import android.ivo.bake_it.Bundles;
import android.ivo.bake_it.api.ApiClientRemote;
import android.ivo.bake_it.databinding.ActivityMainBinding;
import android.ivo.bake_it.model.Recipe;
import android.ivo.bake_it.screen.recipe.RecipeActivity;
import android.os.Bundle;

import org.json.JSONException;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements MainAdapter.OnViewItemClickedListener, ApiClientRemote.OnConnectedListener {

    ActivityMainBinding binding;

    MainAdapter mainAdapter;

    ApiClientRemote apiClientRemote;

    List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apiClientRemote = ApiClientRemote.createClient(this, this);
        ApiClientRemote.connect();
    }

    private void initRecipeRecyclerView() {
        RecyclerView recyclerView = binding.activityMainRv;
        BakeItApplication application = (BakeItApplication)getApplication();

        if (application.deviceIsTablet()) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mainAdapter);
    }

    @Override
    public void onRecipeClicked(int position) {
        Intent callingIntent = getIntent();
        if(callingIntent!=null) {
            Bundle extras = callingIntent.getExtras();
            if(extras!=null) {
                int widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
                Timber.d(""+widgetId);
            }
        }
        Intent intent = new Intent(this, RecipeActivity.class);
        Recipe recipe = recipes.get(position);
        intent.putExtra(Bundles.RECIPE_BUNDLE_KEY, recipe);
        startActivity(intent);
    }

    @Override
    public void onConnected() {
        Timber.d("-------------------------------");
        Timber.d("Connected to remote host");
        Timber.d("-------------------------------");
        try {
            Future<List<Recipe>> allRecipes = apiClientRemote.fetchRecipes();
            // TODO: run allRecipes.get() on another thread and then post in on the UI thread
            recipes = allRecipes.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        mainAdapter = new MainAdapter(recipes, this);
        initRecipeRecyclerView();
    }
}