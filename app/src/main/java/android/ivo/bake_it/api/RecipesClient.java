package android.ivo.bake_it.api;

import android.content.Context;
import android.ivo.bake_it.model.Recipe;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

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

    public String getAllRecipes() throws ExecutionException, InterruptedException {
        final URL url = getHandledURL();

        Future<String> resultFuture = executorService.submit(new Callable<String>() {
            @Override
            public String call() {
                HttpURLConnection httpURLConnection = null;
                InputStream inputStream = null;
                String result = null;

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

                    result = builder.toString();
                    //TODO Parse JSON Objects

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

                return result;
            }

        });

        return resultFuture.get();
    }

    /**
     * Retrieves all the recipes from the server
     */
    public List<Recipe> getMockedRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            Recipe recipe = new Recipe();
            recipe.setName("Recipe " + i);
            recipes.add(recipe);
        }
        return recipes;
    }

    public interface OnConnectedListener {
        void onConnected();
    }
}
