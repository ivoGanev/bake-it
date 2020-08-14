package android.ivo.bake_it.api;

import android.content.Context;
import android.content.ContextWrapper;
import android.ivo.bake_it.idlingresource.SimpleIdlingResource;
import android.ivo.bake_it.model.Recipe;
import android.ivo.bake_it.threading.AppExecutors;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.PrecomputedText;

import androidx.test.espresso.IdlingResource;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ApiClientRemote extends ContextWrapper implements ApiClient {
    private static final int CONNECTION_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 5000;
    private static final String RECIPES_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private OnConnectedListener onConnectedListener;
    private AppExecutors appExecutors;
    private SimpleIdlingResource idlingResource;

    public ApiClientRemote(Context base, AppExecutors appExecutors, OnConnectedListener onConnectedListener, SimpleIdlingResource idlingResource) {
        super(base);
        this.appExecutors = appExecutors;
        this.onConnectedListener = onConnectedListener;
        this.idlingResource = idlingResource;
    }

    private static URL getCleanUrl() {
        URL result = null;
        try {
            result = new URL(RECIPES_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void getRecipes(OnRecipesRetrievedListener listener) {
        final URL url = getCleanUrl();

        Callable<List<Recipe>> callable = () -> readRecipes(url);
        Future<List<Recipe>> future = appExecutors.getNetworkExecutor().submit(callable);
        if(idlingResource!=null) {
            idlingResource.setIdle(false);
        }
        appExecutors.getNetworkExecutor().execute(() -> {
            if (listener != null) {
                try {
                    List<Recipe> recipes = future.get();

                    // IdlingResource - non-production code
                    if(idlingResource!=null) {
                        idlingResource.setIdle(true);
                    }
                    appExecutors.getMainThread().execute(() -> {
                        listener.onRecipesRetrieved(recipes);
                    });
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * This method posts the listener's result on the MainThread
     */
    @Override
    public void getRecipe(OnRecipeRetrievedListener listener, int position) {
        final URL url = getCleanUrl();

        Callable<List<Recipe>> callable = () -> readRecipes(url);
        Future<List<Recipe>> future = appExecutors.getNetworkExecutor().submit(callable);

        if(idlingResource!=null) {
            idlingResource.setIdle(false);
        }

        appExecutors.getNetworkExecutor().execute(() -> {
            if (listener != null) {
                try {
                    List<Recipe> recipes = future.get();
                    Recipe recipe = recipes.get(position);

                    // IdlingResource - non-production code
                    if(idlingResource!=null) {
                        idlingResource.setIdle(true);
                    }

                    appExecutors.getMainThread().execute(() -> {
                        listener.onRecipeRetrieved(recipe);
                    });
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * This method posts the listener's result on the MainThread
     */
    @NotNull
    private List<Recipe> readRecipes(URL url) {
        HttpURLConnection httpURLConnection;
        InputStream inputStream;
        String jsonString;

        final List<Recipe> recipes = new ArrayList<>();

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(READ_TIMEOUT);

            inputStream = httpURLConnection.getInputStream();
            jsonString = jsonStreamToString(inputStream);

            JSONArray recipeJsonArray = new JSONArray(jsonString);
            ApiObjectMapper apiObjectMapper = new ApiObjectMapper();
            recipes.addAll(apiObjectMapper.readRecipes(recipeJsonArray));

            if (inputStream != null)
                inputStream.close();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return recipes;
    }

    @NotNull
    private String jsonStreamToString(InputStream inputStream) throws IOException {
        String jsonToString;
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line = "";
        StringBuilder builder = new StringBuilder();
        while (line != null) {
            line = bufferedReader.readLine();
            builder.append(line);
        }

        jsonToString = builder.toString();
        return jsonToString;
    }

    public interface OnConnectedListener {
        void onConnect();
    }

}
