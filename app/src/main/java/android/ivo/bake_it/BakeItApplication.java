package android.ivo.bake_it;

import android.app.Application;
import android.ivo.bake_it.api.ApiClient;
import android.ivo.bake_it.api.ApiClientLocal;
import android.ivo.bake_it.api.ApiClientRemote;
import android.ivo.bake_it.threading.AppExecutors;

import timber.log.Timber;

public class BakeItApplication extends Application {

    private ApiClient apiClient;

    @Override
    public void onCreate() {
        AppExecutors appExecutors = AppExecutors.getInstance();
        apiClient = new ApiClientRemote(this, appExecutors, () -> {
            // on connect
        });

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        super.onCreate();
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public boolean deviceIsTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }

}
