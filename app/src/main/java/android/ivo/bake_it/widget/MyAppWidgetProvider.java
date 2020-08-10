package android.ivo.bake_it.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.ivo.bake_it.BakeItApplication;
import android.ivo.bake_it.BundleKeys;
import android.ivo.bake_it.model.Recipe;
import android.ivo.bake_it.screen.main.MainActivity;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.ivo.bake_it.R;
import android.ivo.bake_it.widget.WidgetRecipe;
import java.util.concurrent.ExecutionException;

import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 */
public class MyAppWidgetProvider extends AppWidgetProvider {

    private static RemoteViews getRecipeRemoteView(Context context, int widgetId) {
        int recipeIndex = WidgetRecipe.load(context, widgetId);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.my_app_widget);

        Intent loadList = new Intent(context, ListWidgetService.class);
        loadList.putExtra(BundleKeys.RECIPE_SELECTED_INDEX_KEY, recipeIndex);
        remoteViews.setRemoteAdapter(R.id.my_app_widget_lv, loadList);

        Intent launchMainActivity = new Intent(context, MainActivity.class);
        launchMainActivity.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, widgetId, launchMainActivity, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.my_app_widget_recipe_name, pendingIntent);

        BakeItApplication bakeItApplication = (BakeItApplication) context.getApplicationContext();
        try {
            Recipe recipe = bakeItApplication.getApiClient().getRecipe(recipeIndex).get();
            remoteViews.setTextViewText(R.id.my_app_widget_recipe_name, recipe.getName());
            remoteViews.setTextViewText(R.id.my_app_widget_id, "Id: " + widgetId);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return remoteViews;
    }

    public static void updateAppWidget(Context context, int widgetId, int selectedRecipeIndex)
    {
        WidgetRecipe.save(context, widgetId, selectedRecipeIndex);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(widgetId, getRecipeRemoteView(context, widgetId));
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = getRecipeRemoteView(context, appWidgetId);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }
}

