package android.ivo.bake_it.api;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.ivo.bake_it.model.Recipe;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class ApiClientLocal extends ContextWrapper {

    public static final String FILE_NAME = "baking.json";

    public ApiClientLocal(Context base) {
        super(base);
    }

    public List<Recipe> fetchRecipes() throws JSONException {
        JSONArray recipeJsonArray = new JSONArray(jsonLocalResponse(FILE_NAME));
        ApiObjectMapper apiObjectMapper = new ApiObjectMapper();
        return apiObjectMapper.readRecipes(recipeJsonArray);
    }

    public Recipe fetchRecipe(int index) throws JSONException
    {
        JSONArray recipeJsonArray = new JSONArray(jsonLocalResponse(FILE_NAME));
        ApiObjectMapper apiObjectMapper = new ApiObjectMapper();
        return apiObjectMapper.readRecipe(recipeJsonArray.getJSONObject(index));
    }

    private String jsonLocalResponse(String file)
    {
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
