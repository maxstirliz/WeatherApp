package lymansky.artem.weatherapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import lymansky.artem.weatherapp.db.AppDatabase;
import lymansky.artem.weatherapp.db.WeatherEntry;
import lymansky.artem.weatherapp.utils.TimeUtils;

public class WeatherDataViewModel extends AndroidViewModel {

    private AppDatabase mDb;
    private LiveData<List<WeatherEntry>> mAllDays;
    private MutableLiveData<Integer> mDayToShow = new MutableLiveData<>();


    public WeatherDataViewModel(Application application) {
        super(application);
        mDb = AppDatabase.getInstance(application);
        mAllDays = mDb.weatherDao().getAll();
        mDayToShow.setValue(TimeUtils.getCurrentDayNumber());
    }

    public LiveData<List<WeatherEntry>> getAll() {
        return mAllDays;
    }

    public void selectDay(int dayToShow) {
        mDayToShow.setValue(dayToShow);
    }

    public LiveData<Integer> getDayToShow() {
        return mDayToShow;
    }
}
