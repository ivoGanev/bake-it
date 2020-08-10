package android.ivo.bake_it.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.ivo.bake_it.BundleKeys;
import android.ivo.bake_it.screen.main.MainActivity;
import android.widget.RemoteViews;
import android.ivo.bake_it.R;

/**
 * Implementation of App Widget functionality.
 */
public class MyAppWidget extends AppWidgetProvider {

    public static void u()
    {

    }
    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String recipeName, int selectedIndex) {
        RemoteViews rv = getRecipeRemoteView(context, appWidgetId, recipeName, selectedIndex);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    private static RemoteViews getRecipeRemoteView(Context context, int appWidgetId, String recipeName, int selectedIndex)
    {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_app_widget);

        Intent openMainActivityIntent = new Intent(context, MainActivity.class);
        openMainActivityIntent.setAction("DO");
        openMainActivityIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openMainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.my_app_widget_recipe_name, pendingIntent);

        views.setTextViewText(R.id.my_app_widget_recipe_name, recipeName);
        bindIngredientsAdapter(context, views, selectedIndex);

        return views;
    }

    private static void bindIngredientsAdapter(Context context, RemoteViews views,int selectedIndex) {
        if(selectedIndex==-1) {
            // TODO load an empty list layout to show the user that he needs to select a recipe
        }
        else {
            Intent loadListViewIntent = new Intent(context, ListWidgetService.class);
            loadListViewIntent.putExtra(BundleKeys.RECIPE_SELECTED_INDEX_KEY, selectedIndex);
            views.setRemoteAdapter(R.id.my_app_widget_lv, loadListViewIntent);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, "recipeName", -1);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

