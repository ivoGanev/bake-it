package android.ivo.bake_it.api;

import android.ivo.bake_it.model.Recipe;

import java.util.List;

public interface OnRecipesRetrievedListener {
    void onRecipesRetrieved(List<Recipe> recipes);
}
