package android.ivo.bake_it;

import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.IntDef;

import com.squareup.picasso.Picasso;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class HttpMediaFormat {
    public static final int UNKNOWN = 0;
    public static final int MP4 = 1;
    public static final int JPEG = 2;

    private final @MediaFormat
    int mediaFormat;
    private final String url;

    public HttpMediaFormat(String url) {
        this.url = url;
        mediaFormat = parseFormat();
    }

    private int parseFormat() {
        if (url.endsWith(".mp4"))
            return MP4;
        else if (url.endsWith(".jpg") || url.endsWith(".jpeg"))
            return JPEG;
        return UNKNOWN;
    }

    public @MediaFormat
    int getFormat() {
        return mediaFormat;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MP4, JPEG})
    public @interface MediaFormat {
    }
}
