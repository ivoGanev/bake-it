package android.ivo.bake_it.screen.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.ivo.bake_it.R;
import android.ivo.bake_it.api.RecipesClient;
import android.ivo.bake_it.databinding.ActivityMainBinding;
import android.ivo.bake_it.model.Recipe;
import android.ivo.bake_it.screen.recipe.RecipeActivity;
import android.os.Bundle;
import android.os.Parcel;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements MainAdapter.OnViewItemClickedListener, RecipesClient.OnConnectedListener {

    public static final String RECIPE_BUNDLE_KEY = "recipe bundle";

    ActivityMainBinding binding;

    MainAdapter mainAdapter;

    RecipesClient recipesClient;

    List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recipesClient = RecipesClient.createClient(this, this);
        RecipesClient.connect();
    }

    private void initRecipeRecyclerView() {
        RecyclerView recyclerView = binding.activityMainRv;
        if (deviceIsTablet()) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        }
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mainAdapter);
    }

    private boolean deviceIsTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }

    @Override
    public void onRecipeClicked(int position) {
        Intent intent = new Intent(this, RecipeActivity.class);
        Bundle bundle = new Bundle();
        Recipe recipe = recipes.get(position);
        intent.putExtra(RECIPE_BUNDLE_KEY, recipes.get(position));
        startActivity(intent);
    }

    @Override
    public void onConnected() {
        Timber.d("-------------------------------");
        Timber.d("Connected to web service");
        Timber.d("-------------------------------");
        try {
            Future<List<Recipe>> allRecipes = recipesClient.getAllRecipes();
            // TODO: run allRecipes.get() on another thread and then post in on the UI thread
            recipes = allRecipes.get();
            mainAdapter = new MainAdapter(recipes, this);
            initRecipeRecyclerView();

            Timber.d(allRecipes.get().toString());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}