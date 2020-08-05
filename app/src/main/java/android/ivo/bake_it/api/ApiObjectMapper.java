package android.ivo.bake_it.api;

import android.ivo.bake_it.model.Ingredient;
import android.ivo.bake_it.model.Recipe;
import android.ivo.bake_it.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApiObjectMapper {

    public Recipe readRecipe(JSONObject jsonRecipe) throws JSONException {
        int id = jsonRecipe.getInt("id");
        int servings = jsonRecipe.getInt("servings");
        String name = jsonRecipe.getString("name");

        JSONArray ingredientsJsonArray = jsonRecipe.getJSONArray("ingredients");
        JSONArray stepJsonArray = jsonRecipe.getJSONArray("steps");

        return new Recipe(new Recipe.Builder()
                .id(id)
                .name(name)
                .ingredients(readIngredients(ingredientsJsonArray))
                .steps(readSteps(stepJsonArray))
                .servings(servings));
    }

    public Ingredient readIngredient(JSONObject ingredientJson) throws JSONException {
        return new Ingredient(
                ingredientJson.getDouble("quantity"),
                ingredientJson.getString("measure"),
                ingredientJson.getString("ingredient"));
    }

    public Step readStep(JSONObject stepJson) throws JSONException {
        return new Step(new Step.Builder()
                .id(stepJson.getInt("id"))
                .thumbnailURL(stepJson.getString("thumbnailURL"))
                .description(stepJson.getString("description"))
                .shortDescription(stepJson.getString("shortDescription"))
                .videoURL(stepJson.getString("videoURL")));
    }

    public List<Recipe> readRecipes(JSONArray recipeJsonArray) throws JSONException {
        List<Recipe> recipes = new ArrayList<>();
        for (int i = 0; i < recipeJsonArray.length(); i++) {
            Recipe recipe = readRecipe(recipeJsonArray.getJSONObject(i));
            recipes.add(recipe);
        }
        return recipes;
    }

    public List<Ingredient> readIngredients(JSONArray ingredientJsonArray) throws JSONException {
        List<Ingredient> ingredients = new ArrayList<>();
        for (int i = 0; i < ingredientJsonArray.length(); i++) {
            Ingredient ingredient = readIngredient(ingredientJsonArray.getJSONObject(i));
            ingredients.add(ingredient);
        }
        return ingredients;
    }

    public List<Step> readSteps(JSONArray stepJsonArray) throws JSONException {
        List<Step> steps = new ArrayList<>();
        for (int i = 0; i < stepJsonArray.length(); i++) {
            JSONObject stepJson = stepJsonArray.getJSONObject(i);
            Step step = readStep(stepJson);
            steps.add(step);
        }
        return steps;
    }
}
