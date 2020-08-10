package android.ivo.bake_it.utils;

public class StringUtils {
    private StringUtils() {

    }

    public static String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
