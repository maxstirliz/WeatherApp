package lymansky.artem.weatherapp.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface WeatherDao {

    //GET
    @Query("SELECT * FROM weather ORDER BY time ASC")
    LiveData<List<WeatherEntry>> getAll();

    @Query("SELECT * FROM weather WHERE day_number = :day")
    LiveData<List<WeatherEntry>> getWeatherOfDay(int day);

    @Query("SELECT COUNT(*) FROM weather")
    int getTableCount();

    //INSERT
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<WeatherEntry> entries);

    //DELETE
    @Query("DELETE FROM weather WHERE day_number < :day")
    void deleteOldItems(int day);
}
