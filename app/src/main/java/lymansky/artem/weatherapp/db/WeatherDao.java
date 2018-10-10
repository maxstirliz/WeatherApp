package lymansky.artem.weatherapp.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface WeatherDao {

    @Query("SELECT * FROM weather ORDER BY time ASC")
    List<WeatherEntry> getAll();

    @Query("SELECT * FROM weather WHERE day_number LIKE :day ORDER BY time ASC")
    List<WeatherEntry> getDayWeather(int day);

    @Insert
    void insertAll(WeatherEntry... weatherEntity);

    @Delete
    void delete(WeatherEntry weatherEntity);
}
