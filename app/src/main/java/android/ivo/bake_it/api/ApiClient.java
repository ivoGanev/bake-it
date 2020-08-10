package android.ivo.bake_it.api;

import android.content.Context;
import android.ivo.bake_it.model.Recipe;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Future;

public abstract class ApiClient {

    public abstract Future<List<Recipe>> getRecipes();
    public abstract Future<Recipe> getRecipe(int index);

    private OnConnectedListener onConnectedListener;
    private WeakReference<Context> contextWeakReference;

    public ApiClient(OnConnectedListener onConnectedListener, Context context) {
        this.onConnectedListener = onConnectedListener;
        this.contextWeakReference = new WeakReference<>(context);
    }

    public Context getBaseContext()
    {
        return contextWeakReference.get();
    }

    public void connect()
    {
        if(onConnectedListener!=null)
            onConnectedListener.onConnect();
    }

    public interface OnConnectedListener {
        void onConnect();
    }
}
