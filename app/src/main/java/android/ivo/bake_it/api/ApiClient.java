package android.ivo.bake_it.api;

import android.content.Context;
import android.ivo.bake_it.model.Recipe;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Future;

public interface ApiClient {
    void getRecipes(OnRecipesRetrievedListener listener);
    void getRecipe(OnRecipeRetrievedListener listener, int position);
}
