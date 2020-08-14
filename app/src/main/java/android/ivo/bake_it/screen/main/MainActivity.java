package android.ivo.bake_it.screen.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.ivo.bake_it.BakeItApplication;
import android.ivo.bake_it.BundleKeys;
import android.ivo.bake_it.R;
import android.ivo.bake_it.api.ApiClient;
import android.ivo.bake_it.api.ApiClientLocal;
import android.ivo.bake_it.databinding.ActivityMainBinding;
import android.ivo.bake_it.model.Recipe;
import android.ivo.bake_it.screen.recipe.RecipeActivity;
import android.ivo.bake_it.threading.AppExecutors;
import android.ivo.bake_it.widget.MyAppWidgetProvider;
import android.os.Bundle;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements MainAdapter.OnViewItemClickedListener {

    ActivityMainBinding binding;

    MainAdapter mainAdapter;

    List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        recipes = new ArrayList<>();
        Timber.d("-------------------------------");
        Timber.d("Connected to remote host");
        Timber.d("-------------------------------");
//        try {
//            BakeItApplication bakeItApplication = (BakeItApplication) getApplication();
//            Future<List<Recipe>> allRecipes = bakeItApplication.getApiClient().getRecipes();
//            // TODO: run allRecipes.get() on another thread and then post in on the UI thread
//            recipes = allRecipes.get();
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
        BakeItApplication bakeItApplication = (BakeItApplication) getApplication();
        ApiClient localClient =  bakeItApplication.getApiClient();

        localClient.getRecipes(recipes -> {
            this.recipes.addAll(recipes);
            mainAdapter.update(this.recipes);
        });

        mainAdapter = new MainAdapter(new ArrayList<>(), this);
        initRecipeRecyclerView();
    }

    private void initRecipeRecyclerView() {
        RecyclerView recyclerView = binding.activityMainRv;
        BakeItApplication application = (BakeItApplication) getApplication();

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
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int widgetId = getWidgetId();
        Intent intent;

        boolean parentActivityIsWidget = getWidgetId() != AppWidgetManager.INVALID_APPWIDGET_ID;

        if (parentActivityIsWidget) {
            MyAppWidgetProvider.updateAppWidget(this, widgetId, position);
            finishAffinity();
        } else {
            intent = new Intent(this, RecipeActivity.class);
            Recipe recipe = recipes.get(position);
            intent.putExtra(BundleKeys.RECIPE_BUNDLE_KEY, recipe);
            startActivity(intent);
        }
    }

    private int getWidgetId() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            return extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        return AppWidgetManager.INVALID_APPWIDGET_ID;
    }

}