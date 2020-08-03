package android.ivo.bake_it;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MediaLoaderFactory {
    public static final int MP4 = 0;
    public static final int JPG = 1;

    public <T extends HttpMediaFormat>  T get(Class<T> mediaLoaderClass) {
        try {
            return (T)mediaLoaderClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @IntDef({MP4, JPG})
    @Retention(RetentionPolicy.SOURCE)
    @interface MediaType {
    }
}
