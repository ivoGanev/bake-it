package android.ivo.bake_it.widget;

import android.content.Context;
import android.content.SharedPreferences;

class WidgetRecipe {

    public static final String PREFERENCE_NAME = "com.android.application.recipewidget";

    public static synchronized void save(Context context, int widgetId, int recipeIndex)
    {
        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        sharedPreferences.putInt(PREFERENCE_NAME + widgetId, recipeIndex);
        sharedPreferences.commit();
    }

    public static synchronized int load(Context context, int widgetId)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        return sharedPreferences.getInt(PREFERENCE_NAME + widgetId, 0);
    }

    public static synchronized void delete(Context context, int widgetId)
    {
        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        sharedPreferences.remove(PREFERENCE_NAME + widgetId);
        sharedPreferences.commit();
    }
}
