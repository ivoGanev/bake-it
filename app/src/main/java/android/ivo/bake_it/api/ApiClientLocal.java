package android.ivo.bake_it.api;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.ivo.bake_it.model.Recipe;
import android.ivo.bake_it.threading.AppExecutors;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import timber.log.Timber;

public class ApiClientLocal extends ContextWrapper implements ApiClient {
    public static final String FILE_NAME = "baking.json";
    private AppExecutors executors;

    public ApiClientLocal(Context base, AppExecutors executors) {
        super(base);
        this.executors = executors;
    }

    private List<Recipe> readRecipes() throws JSONException {
        JSONArray recipeJsonArray = new JSONArray(jsonLocalResponse());
        ApiObjectMapper apiObjectMapper = new ApiObjectMapper();
        return apiObjectMapper.readRecipes(recipeJsonArray);
    }

    private Recipe readRecipe(int index) throws JSONException {
        JSONArray recipeJsonArray = new JSONArray(jsonLocalResponse());
        ApiObjectMapper apiObjectMapper = new ApiObjectMapper();
        return apiObjectMapper.readRecipe(recipeJsonArray.getJSONObject(index));
    }

    /**
     * This method posts the listener's result on the MainThread
     * */
    @Override
    public void getRecipes(OnRecipesRetrievedListener listener) {
        executors.getDiskIOExecutor().execute(() -> {
            List<Recipe> recipes;
            try {
                recipes = readRecipes();
                executors.getMainThread().execute(() ->
                {
                    listener.onRecipesRetrieved(recipes);
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * This method posts the listener's result on the MainThread
     * */
    @Override
    public void getRecipe(OnRecipeRetrievedListener listener, final int position) {
        executors.getDiskIOExecutor().execute(() -> {
            Recipe recipe;
            try {
                recipe = readRecipe(position);
                executors.getMainThread().execute(() ->
                {
                    listener.onRecipeRetrieved(recipe);
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private String jsonLocalResponse() {
        AssetManager assets = getBaseContext().getAssets();
        StringBuilder jsonLocalResponse = new StringBuilder();
        try {
            InputStream assetFile = assets.open(FILE_NAME);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(assetFile));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                jsonLocalResponse.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonLocalResponse.toString();
    }
}
