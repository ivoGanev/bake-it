package android.ivo.bake_it.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.ivo.bake_it.screen.main.MainActivity;
import android.widget.RemoteViews;
import android.ivo.bake_it.R;

/**
 * Implementation of App Widget functionality.
 */
public class MyAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_app_widget);

        setOnClickListenerForIngredient(context, views, appWidgetId);

        bindIngredientsAdapter(context, views);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static void setOnClickListenerForIngredient(Context context, RemoteViews views, int appWidgetId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction("DO");
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.my_app_widget_recipe_name, pendingIntent);
    }

    private static void bindIngredientsAdapter(Context context, RemoteViews views) {
        Intent listViewService = new Intent(context, ListWidgetService.class);
        views.setRemoteAdapter(R.id.my_app_widget_lv, listViewService);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
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

