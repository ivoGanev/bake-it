package android.ivo.bake_it.idlingresource;

import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

public class SimpleIdlingResource implements IdlingResource {
    @Nullable
    private volatile ResourceCallback resourceCallback;

    private AtomicBoolean isIdleNow = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return isIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        resourceCallback = callback;
    }

    public void setIdle(boolean isIdleNow) {
        this.isIdleNow.set(isIdleNow);
        ResourceCallback resourceCallback = this.resourceCallback;
        if (resourceCallback != null && isIdleNow) {
            resourceCallback.onTransitionToIdle();
        }
    }
}
