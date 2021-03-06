package android.ivo.bake_it;

import android.app.Application;
import android.ivo.bake_it.api.ApiClient;
import android.ivo.bake_it.api.ApiClientLocal;
import android.ivo.bake_it.api.ApiClientRemote;
import android.ivo.bake_it.idlingresource.SimpleIdlingResource;
import android.ivo.bake_it.threading.AppExecutors;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.test.espresso.IdlingResource;

import timber.log.Timber;

public class BakeItApplication extends Application {

    private ApiClient apiClient;

    @Nullable
    private SimpleIdlingResource idlingResource;

    @VisibleForTesting
    public SimpleIdlingResource getIdlingResource() {
        if (idlingResource == null)
            idlingResource = new SimpleIdlingResource();
        return idlingResource;
    }

    @Override
    public void onCreate() {
        AppExecutors appExecutors = AppExecutors.getInstance();
        apiClient = new ApiClientRemote(this, appExecutors, () -> {
            // on connect
        }, null);

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

    public boolean deviceIsInLandscapeMode() {
        return getResources().getBoolean(R.bool.isLandscape);
    }

}
