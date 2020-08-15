package android.ivo.bake_it.screen.main;

import androidx.annotation.IntDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.ivo.bake_it.BakeItApplication;
import android.ivo.bake_it.R;
import android.ivo.bake_it.api.ApiClient;
import android.ivo.bake_it.api.NetworkUtils;
import android.ivo.bake_it.databinding.ActivityMainBinding;
import android.ivo.bake_it.model.Recipe;
import android.ivo.bake_it.screen.recipe.RecipeActivity;
import android.ivo.bake_it.widget.MyAppWidgetProvider;
import android.os.Bundle;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MainAdapter.OnViewItemClickedListener {

    public static final int VISIBILITY_NO_NETWORK = 1;
    public static final int VISIBILITY_LOADING_API_DATA = 2;
    public static final int VISIBILITY_API_DATA_LOADED = 3;
    public static final int VISIBILITY_WIDGET_UI = 4;

    public static final String RECIPE_BUNDLE_KEY = "android.ivo.bake_it.bundle_keys.recipe";
    ActivityMainBinding binding;
    MainAdapter mainAdapter;
    List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        recipes = new ArrayList<>();
        setContentView(binding.getRoot());

        BakeItApplication bakeItApplication = (BakeItApplication) getApplication();
        ApiClient localClient = bakeItApplication.getApiClient();

        if (!NetworkUtils.isConnectedToNetwork(this)) {
            setUiVisibilityState(VISIBILITY_NO_NETWORK);
        } else {
            setUiVisibilityState(VISIBILITY_LOADING_API_DATA);
        }

        // results automatically comes in the MainThread
        localClient.getRecipes(recipes -> {
            this.recipes.addAll(recipes);
            mainAdapter.update(this.recipes);

            setUiVisibilityState(VISIBILITY_API_DATA_LOADED);
        });

        boolean parentActivityIsWidget = getWidgetId() != AppWidgetManager.INVALID_APPWIDGET_ID;
        if (parentActivityIsWidget) {
            setUiVisibilityState(VISIBILITY_WIDGET_UI);
        }

        mainAdapter = new MainAdapter(new ArrayList<>(), this);
        initRecipeRecyclerView();
    }

    private void setUiVisibilityState(@UiVisibilityState int state) {
        if (state == VISIBILITY_NO_NETWORK) {
            binding.activityMainTvNoNetwork.setVisibility(View.VISIBLE);
            binding.activityMainPb.setVisibility(View.GONE);
            binding.activityMainRv.setVisibility(View.GONE);
        } else if (state == VISIBILITY_LOADING_API_DATA) {
            binding.activityMainPb.setVisibility(View.VISIBLE);
            binding.activityMainTvNoNetwork.setVisibility(View.GONE);
            binding.activityMainRv.setVisibility(View.GONE);
        } else if (state == VISIBILITY_API_DATA_LOADED) {
            binding.activityMainRv.setVisibility(View.VISIBLE);
            binding.activityMainPb.setVisibility(View.GONE);
            binding.activityMainTvNoNetwork.setVisibility(View.GONE);
        } else if (state == VISIBILITY_WIDGET_UI) {
            binding.activityMainWidgetSelectTv.setVisibility(View.VISIBLE);
            binding.activityMainRv.setVisibility(View.VISIBLE);
            binding.activityMainPb.setVisibility(View.GONE);
            binding.activityMainTvNoNetwork.setVisibility(View.GONE);
        }
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
        boolean parentActivityIsWidget = getWidgetId() != AppWidgetManager.INVALID_APPWIDGET_ID;
        if (parentActivityIsWidget) {
            MyAppWidgetProvider.updateAppWidget(this, getWidgetId(), position);
            finishAffinity();
        } else {
            Intent intent = new Intent(this, RecipeActivity.class);
            Recipe recipe = recipes.get(position);
            intent.putExtra(RECIPE_BUNDLE_KEY, recipe);
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

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            VISIBILITY_NO_NETWORK,
            VISIBILITY_LOADING_API_DATA,
            VISIBILITY_API_DATA_LOADED,
            VISIBILITY_WIDGET_UI})
    @interface UiVisibilityState {
    }
}