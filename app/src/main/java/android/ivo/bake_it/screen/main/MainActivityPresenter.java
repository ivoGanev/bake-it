package android.ivo.bake_it.screen.main;

import android.ivo.bake_it.api.RecipesClient;
import android.ivo.bake_it.model.Recipe;

public class MainActivityPresenter {
    private RecipesClient recipesClient;
    private View view;
    private Recipe recipe;

    public MainActivityPresenter(View view) {
        this.view = view;
    }

    interface View {

    }
}
