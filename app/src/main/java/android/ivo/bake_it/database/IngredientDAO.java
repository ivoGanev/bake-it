package android.ivo.bake_it.database;

import android.ivo.bake_it.model.Ingredient;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomDatabase;
import androidx.room.Update;

import java.util.List;

@Dao
public interface IngredientDAO {

    @Query("select * from ingredient")
    public List<Ingredient> getAll();

    @Query("select * from ingredient where id = :id")
    public Ingredient get(int id);

    @Delete
    public void delete(Ingredient ingredient);

    @Update
    public  void update(Ingredient ingredient);

    @Insert
    public void insert(Ingredient ingredient);
}
