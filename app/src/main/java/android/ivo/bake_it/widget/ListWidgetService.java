package android.ivo.bake_it.widget;

import android.content.Context;
import android.content.Intent;
import android.ivo.bake_it.BakeItApplication;
import android.ivo.bake_it.BundleKeys;
import android.ivo.bake_it.R;
import android.ivo.bake_it.model.Ingredient;
import android.ivo.bake_it.model.Recipe;
import android.ivo.bake_it.utils.StringUtils;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import androidx.room.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.ivo.bake_it.utils.StringUtils.*;

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private List<Ingredient> ingredients;

    private Recipe recipe;
    private Context context;

    private int recipeIndex;

    public ListRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        Bundle extras = intent.getExtras();
        if(extras!=null) {
            recipeIndex = extras.getInt(BundleKeys.RECIPE_SELECTED_INDEX_KEY);
        }
    }

    @Override
    public void onCreate() {
        ingredients = new ArrayList<>();
    }

    @Override
    public void onDataSetChanged() {
        BakeItApplication bakeItApplication = (BakeItApplication) context.getApplicationContext();
        // call web service
        try {
            recipe = bakeItApplication.getApiClient().getRecipe(recipeIndex).get();
            ingredients = recipe.getIngredients();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       // Toast.makeText(context, "Recipe Selected: " + recipe.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.my_widget_item);
        rv.setTextViewText(R.id.my_widget_item_name, capitalize(ingredients.get(position).getIngredient()));
        rv.setTextViewText(R.id.my_widget_item_quantity, capitalize(ingredients.get(position).getQuantityToString()));
        rv.setTextViewText(R.id.my_widget_item_measure, capitalize(ingredients.get(position).getMeasure()));
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
