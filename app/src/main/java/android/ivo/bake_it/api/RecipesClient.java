package android.ivo.bake_it.api;

import android.content.Context;
import android.ivo.bake_it.model.Ingredient;
import android.ivo.bake_it.model.Recipe;
import android.ivo.bake_it.model.Step;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import timber.log.Timber;

public class RecipesClient {
    private static final int CONNECTION_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 5000;
    private static final String RECIPES_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private static OnConnectedListener onConnectedListener;
    private static ExecutorService executorService;
    private static WeakReference<Context> contextWeakReference;

    private RecipesClient() {
        executorService = Executors.newFixedThreadPool(3);
    }

    /**
     * Static factory method to create the client and connect it's web API
     */
    public static synchronized RecipesClient createClient(Context context, OnConnectedListener listener) {
        onConnectedListener = listener;
        contextWeakReference = new WeakReference<>(context);
        return new RecipesClient();
    }

    public static synchronized void connect()
    {
        // we are querying just a simple json from a static address so triggering onConnected() only
        // if the user has internet connection should be enough.
        if (isConnectedToNetwork()) {
            if (onConnectedListener != null)
                onConnectedListener.onConnected();
        }
    }
    private static boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) contextWeakReference.get()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo[] allNetworkInfo = connectivityManager.getAllNetworkInfo();
            for (NetworkInfo networkInfo : allNetworkInfo) {
                if (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    private static URL getHandledURL() {
        URL result = null;
        try {
            result = new URL(RECIPES_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Future<List<Recipe>> getAllRecipes() throws ExecutionException, InterruptedException {
        final URL url = getHandledURL();
        final List<Recipe> recipes = new ArrayList<>();
        Future<List<Recipe>> resultFuture = executorService.submit(new Callable<List<Recipe>>() {
            @Override
            public List<Recipe> call() {
                HttpURLConnection httpURLConnection = null;
                InputStream inputStream = null;
                String jsonToString = null;

                try {
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setReadTimeout(READ_TIMEOUT);

                    inputStream = httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    String line = "";
                    StringBuilder builder = new StringBuilder();
                    while (line!=null) {
                        line = bufferedReader.readLine();
                        builder.append(line);
                    }

                    jsonToString = builder.toString();

                    JSONArray mainJsonArray = null;
                    try {
                        mainJsonArray =new JSONArray(jsonToString);
                        for (int i = 0; i < mainJsonArray.length(); i++) {
                            JSONObject recipeJson = mainJsonArray.getJSONObject(i);

                            int id = recipeJson.getInt("id");
                            int servings = recipeJson.getInt("servings");
                            String name = recipeJson.getString("name");

                            JSONArray ingredientsJsonArray = recipeJson.getJSONArray("ingredients");
                            List<Ingredient> ingredients = new ArrayList<>();
                            for (int j = 0; j < ingredientsJsonArray.length(); j++) {
                                JSONObject ingredientJson = ingredientsJsonArray.getJSONObject(j);
                                Ingredient ingredient = new Ingredient(
                                        ingredientJson.getInt("quantity"),
                                        ingredientJson.getString("measure"),
                                        ingredientJson.getString("ingredient"));
                                ingredients.add(ingredient);
                            }

                            JSONArray stepsJsonArray = recipeJson.getJSONArray("steps");
                            List<Step> steps = new ArrayList<>();
                            for (int j = 0; j < stepsJsonArray.length(); j++) {
                                JSONObject stepJson = stepsJsonArray.getJSONObject(j);
                                Step.Builder stepBuilder = new Step.Builder()
                                        .id(stepJson.getInt("id"))
                                        .thumbnailURL(stepJson.getString("thumbnailURL"))
                                        .description(stepJson.getString("description"))
                                        .shortDescription(stepJson.getString("shortDescription"))
                                        .videoURL(stepJson.getString("videoURL"));
                                Step step = new Step(stepBuilder);
                                steps.add(step);
                            }

                            Recipe.Builder recipeBuilder = new Recipe.Builder()
                                    .id(id)
                                    .name(name)
                                    .ingredients(ingredients)
                                    .steps(steps)
                                    .servings(servings);

                            Recipe recipe = new Recipe(recipeBuilder);
                            recipes.add(recipe);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (inputStream != null)
                            inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return recipes;
            }

        });

        return resultFuture;
    }

    /**
     * Retrieves all the recipes from the server
     */
    public List<Recipe> getMockedRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            Recipe.Builder recipeBuilder = new Recipe.Builder()
                    .id(i)
                    .name("" + i)
                    .ingredients(null)
                    .steps(null)
                    .servings(i);

            Recipe recipe = new Recipe(recipeBuilder);
            recipes.add(recipe);
        }
        return recipes;
    }

    public interface OnConnectedListener {
        void onConnected();
    }
}
