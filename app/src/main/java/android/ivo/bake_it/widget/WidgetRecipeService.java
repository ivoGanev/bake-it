package android.ivo.bake_it.widget;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

class WidgetRecipeService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public WidgetRecipeService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
