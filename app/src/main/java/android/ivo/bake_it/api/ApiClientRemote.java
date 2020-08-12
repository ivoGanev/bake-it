package android.ivo.bake_it.api;

import android.content.Context;
import android.ivo.bake_it.model.Recipe;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ApiClientRemote {
    private static final int CONNECTION_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 5000;
    private static final String RECIPES_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private static OnConnectedListener onConnectedListener;
    private static ExecutorService executorService;
    private static WeakReference<Context> contextWeakReference;

    private ApiClientRemote() {
        executorService = Executors.newFixedThreadPool(3);
    }

    /**
     * Static factory method to create the client and connect it's web API
     */
    public static synchronized ApiClientRemote createClient(Context context, OnConnectedListener listener) {
        onConnectedListener = listener;
        contextWeakReference = new WeakReference<>(context);
        return new ApiClientRemote();
    }

    public static synchronized void connect()
    {
        // we are querying just a simple json from a static address so triggering onConnected() only
        // if the user has internet connection should be enough.
        if (isConnectedToNetwork()) {
            if (onConnectedListener != null)
                onConnectedListener.onConnect();
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

    private static URL getCleanUrl() {
        URL result = null;
        try {
            result = new URL(RECIPES_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Future<List<Recipe>> fetchRecipes() {
        final URL url = getCleanUrl();
        final List<Recipe> recipes = new ArrayList<>();

        return executorService.submit(new Callable<List<Recipe>>() {
            @Override
            public List<Recipe> call() {
                HttpURLConnection httpURLConnection = null;
                InputStream inputStream = null;
                String jsonString = null;

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

        });
    }

    @NotNull
    private String jsonStreamToString(InputStream inputStream) throws IOException {
        String jsonToString;
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line = "";
        StringBuilder builder = new StringBuilder();
        while (line!=null) {
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
