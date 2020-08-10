package android.ivo.bake_it.database;

import android.content.Context;
import android.ivo.bake_it.model.Ingredient;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@androidx.room.Database(version = 1, exportSchema = false, entities = Ingredient.class)
public abstract class WidgetDatabaseCache extends RoomDatabase {
    public static final String APP_DATABASE_NAME = "widget database cache";
    private static WidgetDatabaseCache instance;

    private WidgetDatabaseCache() {
    }

    public static synchronized WidgetDatabaseCache getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, WidgetDatabaseCache.class, APP_DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public IngredientDAO ingredientDAO;
}
