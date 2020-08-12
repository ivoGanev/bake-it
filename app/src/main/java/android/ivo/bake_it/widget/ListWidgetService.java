package android.ivo.bake_it.widget;

import android.content.Context;
import android.content.Intent;
import android.ivo.bake_it.R;
import android.ivo.bake_it.model.Ingredient;
import android.ivo.bake_it.model.Recipe;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import static android.ivo.bake_it.utils.StringUtils.*;

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private List<Ingredient> ingredients;

    private Context context;

    private Recipe recipe;

    public ListRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Bundle recipeExtraBundle = bundle.getBundle(MyAppWidgetProvider.PARCELABLE_RECIPE_EXTRA);
            if (recipeExtraBundle != null) {
                recipe = recipeExtraBundle.getParcelable(MyAppWidgetProvider.PARCELABLE_RECIPE_BUNDLE);
                if (recipe != null) {
                    ingredients = recipe.getIngredients();
                }
            }
        }
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

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
