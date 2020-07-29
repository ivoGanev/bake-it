package android.ivo.bake_it;

import android.app.Application;

import timber.log.Timber;

public class BakeItApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if(BuildConfig.DEBUG)
            Timber.plant(new Timber.DebugTree());
    }
}
