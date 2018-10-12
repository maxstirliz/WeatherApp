package lymansky.artem.weatherapp.fragments;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import lymansky.artem.weatherapp.db.AppDatabase;
import lymansky.artem.weatherapp.db.WeatherEntry;

public class WeatherDataViewModel extends AndroidViewModel {

    private AppDatabase mDb;

    private LiveData<List<WeatherEntry>> mAllDays;
    private LiveData<List<WeatherEntry>> mWeatherOfDay;
    private LiveData<List<Integer>> mDayNumbers;

    public WeatherDataViewModel(Application application) {
        super(application);
        mDb = AppDatabase.getInstance(application);
        mAllDays = mDb.weatherDao().getAll();
        mDayNumbers = mDb.weatherDao().getDayNumbers();
    }

    public LiveData<List<WeatherEntry>> getAllDays() {
        return mAllDays;
    }

    public LiveData<List<WeatherEntry>> getWeatherOfDay() {
        return mWeatherOfDay;
    }

    public void updateWeatherOfDay(int day) {
        mWeatherOfDay = mDb.weatherDao().getWeatherOfDay(day);
    }

    public void insertAll(List<WeatherEntry> entries) {
        mDb.weatherDao().insertAll(entries);
    }
}
