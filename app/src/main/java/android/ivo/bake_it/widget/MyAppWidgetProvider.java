package android.ivo.bake_it.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.ivo.bake_it.BakeItApplication;
import android.ivo.bake_it.api.ApiClient;
import android.ivo.bake_it.screen.main.MainActivity;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.ivo.bake_it.R;

/**
 * Implementation of App Widget functionality.
 */
public class MyAppWidgetProvider extends AppWidgetProvider {


    public static final String PARCELABLE_RECIPE_BUNDLE = "com.android.ivo.bake_it.parcelable.recipe.bundle";
    public static final String PARCELABLE_RECIPE_EXTRA = "com.android.ivo.bake_it.parcelable.recipe.extra";

    private static RemoteViews getRecipeRemoteView(Context context, int widgetId, String recipeName) {
        Intent launchMainActivity = new Intent(context, MainActivity.class);
        launchMainActivity.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, widgetId, launchMainActivity, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.my_app_widget);

        remoteViews.setOnClickPendingIntent(R.id.my_app_widget_click_to_change, pendingIntent);
        remoteViews.setTextViewText(R.id.my_app_widget_recipe_name, recipeName);
        remoteViews.setTextViewText(R.id.my_app_widget_click_to_change, "Click to change the recipe");
        remoteViews.setEmptyView(R.id.my_app_widget_lv, R.id.my_widget_list_empty);
        return remoteViews;
    }

    public static void updateAppWidget(Context context, int widgetId, int selectedRecipeIndex) {
        BakeItApplication application = (BakeItApplication)context.getApplicationContext();
        ApiClient apiClient = application.getApiClient();
        WidgetRecipe.save(context, widgetId, selectedRecipeIndex);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        updateAll(context, widgetId, apiClient, appWidgetManager);
    }

    private static void updateAll(Context context, int widgetId, ApiClient apiClient, AppWidgetManager appWidgetManager) {
        apiClient.getRecipe(recipe -> {
            RemoteViews remoteViews = getRecipeRemoteView(context, widgetId,  recipe.getName());
            remoteViews.setTextViewText(R.id.my_app_widget_recipe_name, recipe.getName());

            Bundle bundle = new Bundle();
            bundle.putParcelable(PARCELABLE_RECIPE_BUNDLE, recipe);

            Intent recipeList = new Intent(context, ListWidgetService.class);
            recipeList.putExtra(PARCELABLE_RECIPE_EXTRA, bundle);
            recipeList.setAction("update " + System.currentTimeMillis());
            remoteViews.setRemoteAdapter(R.id.my_app_widget_lv, recipeList);

            appWidgetManager.updateAppWidget(widgetId,remoteViews);
            appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.my_app_widget_lv);
        }, WidgetRecipe.load(context, widgetId));
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        BakeItApplication application = (BakeItApplication)context.getApplicationContext();
        ApiClient apiClient = application.getApiClient();

        for (int widgetId : appWidgetIds) {
            updateAll(context, widgetId, apiClient, appWidgetManager);
        }
    }
}

