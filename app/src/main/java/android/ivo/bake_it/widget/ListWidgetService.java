package android.ivo.bake_it.widget;

import android.content.Context;
import android.content.Intent;
import android.ivo.bake_it.R;
import android.ivo.bake_it.model.Ingredient;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private List<Ingredient> ingredients;

    private Context context;

    public ListRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        ingredients = new ArrayList<>();
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
        rv.setTextViewText(R.id.my_widget_item_name, ingredients.get(position).getIngredient());
        rv.setTextViewText(R.id.my_widget_item_quantity, ingredients.get(position).getQuantityToString());
        rv.setTextViewText(R.id.my_widget_item_measure, ingredients.get(position).getMeasure());
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
