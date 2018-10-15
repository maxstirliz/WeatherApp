package lymansky.artem.weatherapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import lymansky.artem.weatherapp.db.AppDatabase;
import lymansky.artem.weatherapp.db.WeatherEntry;

public class WeatherDataViewModel extends AndroidViewModel {

    private AppDatabase mDb;
    private LiveData<List<WeatherEntry>> mAllDays;

    public WeatherDataViewModel(Application application) {
        super(application);
        mDb = AppDatabase.getInstance(application);
        mAllDays = mDb.weatherDao().getAll();
    }

    public LiveData<List<WeatherEntry>> getAll() {
        return mAllDays;
    }
}
