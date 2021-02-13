package android.ivo.bake_it.threading;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppExecutors {
    private final ExecutorService mDiskIOExecutor;
    private final ExecutorService mNetworkExecutor;
    private final Executor mMainThread;

    public ExecutorService getDiskIOExecutor() {
        return mDiskIOExecutor;
    }

    public ExecutorService getNetworkExecutor() {
        return mNetworkExecutor;
    }

    public Executor getMainThread() {
        return mMainThread;
    }

    private AppExecutors() {
        mDiskIOExecutor = Executors.newSingleThreadExecutor();
        mNetworkExecutor = Executors.newFixedThreadPool(3);
        mMainThread = new MainThreadExecutor();
    }

    public static AppExecutors getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final AppExecutors INSTANCE = new AppExecutors();
    }

    public static class MainThreadExecutor implements Executor {
        private final Handler mHandler = new android.os.Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mHandler.post(command);
        }
    }
}
