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
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ApiClientLocal extends  ApiClient {
    public static final String FILE_NAME = "baking.json";

    public ApiClientLocal(OnConnectedListener onConnectedListener, Context contextWeakReference) {
        super(onConnectedListener, contextWeakReference);
    }

    private List<Recipe> fetchRecipes() throws JSONException {
        JSONArray recipeJsonArray = new JSONArray(jsonLocalResponse(FILE_NAME));
        ApiObjectMapper apiObjectMapper = new ApiObjectMapper();
        return apiObjectMapper.readRecipes(recipeJsonArray);
    }

    private Recipe fetchRecipe(int index) throws JSONException {
        JSONArray recipeJsonArray = new JSONArray(jsonLocalResponse(FILE_NAME));
        ApiObjectMapper apiObjectMapper = new ApiObjectMapper();
        return apiObjectMapper.readRecipe(recipeJsonArray.getJSONObject(index));
    }


    @Override
    public Future<List<Recipe>> getRecipes() {
        AppExecutors appExecutors = AppExecutors.getInstance();
        Callable<List<Recipe>> callable = new Callable<List<Recipe>>() {
            @Override
            public List<Recipe> call() throws Exception {
                return fetchRecipes();
            }
        };
        return appExecutors.getNetworkExecutor().submit(callable);
    }

    @Override
    public Future<Recipe> getRecipe(final int index) {
        AppExecutors appExecutors = AppExecutors.getInstance();
        Callable<Recipe> callable = new Callable<Recipe>() {
            @Override
            public Recipe call() throws Exception {
                return fetchRecipe(index);
            }
        };
        return appExecutors.getNetworkExecutor().submit(callable);
    }

    private String jsonLocalResponse(String file) {
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
