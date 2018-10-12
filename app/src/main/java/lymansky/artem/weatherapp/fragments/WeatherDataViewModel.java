package lymansky.artem.weatherapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import lymansky.artem.weatherapp.db.AppDatabase;
import lymansky.artem.weatherapp.db.WeatherEntry;
import lymansky.artem.weatherapp.utils.TimeUtils;

public class WeatherDataViewModel extends AndroidViewModel {

    private AppDatabase mDb;

    private LiveData<List<WeatherEntry>> mUniqueDays;
    private LiveData<List<WeatherEntry>> mWeatherOfDay;

    public WeatherDataViewModel(Application application) {
        super(application);
        mDb = AppDatabase.getInstance(this.getApplication());
        mUniqueDays = mDb.weatherDao().getUniqueDayEntries();
        mWeatherOfDay = mDb.weatherDao().getWeatherOfDay(TimeUtils.getCurrentDayNumber());
    }

    public void updateWeatherOfDay(int day) {
        mWeatherOfDay = mDb.weatherDao().getWeatherOfDay(day);
    }

    public LiveData<List<WeatherEntry>> getUniqueDayEntries() {
        return mUniqueDays;
    }

    public LiveData<List<WeatherEntry>> getWeatherOfDay(int day) {
        return mWeatherOfDay;
    }
}
