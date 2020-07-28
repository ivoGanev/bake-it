package android.ivo.bake_it.screen.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.ivo.bake_it.R;
import android.ivo.bake_it.databinding.ActivityMainBinding;
import android.ivo.bake_it.model.Recipe;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<Recipe> recipes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Recipe recipe = new Recipe();
            recipe.setName("Recipe " + i);
            recipes.add(recipe);
        }

        mainAdapter = new MainAdapter(recipes);

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
}