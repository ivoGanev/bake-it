package android.ivo.bake_it;

import android.app.Application;
import android.ivo.bake_it.api.ApiClient;
import android.ivo.bake_it.api.ApiClientLocal;

import timber.log.Timber;

public class BakeItApplication extends Application implements ApiClient.OnConnectedListener {

    private ApiClient apiClient;

    @Override
    public void onConnect() {

    }

    @Override
    public void onCreate() {
        apiClient = new ApiClientLocal(this, getBaseContext());
        apiClient.connect();

        super.onCreate();
        if (BuildConfig.DEBUG)
            Timber.plant(new Timber.DebugTree());
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public boolean deviceIsTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }

}
