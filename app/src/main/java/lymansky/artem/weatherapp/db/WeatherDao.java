package lymansky.artem.weatherapp.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface WeatherDao {

    @Query("SELECT * FROM weather ORDER BY time ASC")
    LiveData<List<WeatherEntry>> getAll();

    @Query("SELECT * FROM weather WHERE day_number LIKE :day ORDER BY time ASC")
    LiveData<List<WeatherEntry>> getWeatherOfDay(int day);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<WeatherEntry> entries);

    @Delete
    void delete(List<WeatherEntry> weatherEntries);

    @Query("DELETE FROM weather")
    void deleteAll();
}
