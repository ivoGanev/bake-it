package android.ivo.bake_it.screen.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.ivo.bake_it.R;
import android.ivo.bake_it.api.RecipesClient;
import android.ivo.bake_it.databinding.ActivityMainBinding;
import android.ivo.bake_it.model.Recipe;
import android.os.Bundle;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements MainAdapter.OnViewItemClickedListener, RecipesClient.OnConnectedListener {

    ActivityMainBinding binding;

    MainAdapter mainAdapter;

    RecipesClient recipesClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recipesClient = RecipesClient.createClient(this, this);
        RecipesClient.connect();

        mainAdapter = new MainAdapter(recipesClient.getMockedRecipes(), this);

        initRecipeRecyclerView();
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

    }

    @Override
    public void onConnected() {
        Timber.d("-------------------------------");
        Timber.d("Connected to web service");
        Timber.d("-------------------------------");
        try {
            Future<List<Recipe>> allRecipes = recipesClient.getAllRecipes();
            Timber.d(allRecipes.get().toString());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}