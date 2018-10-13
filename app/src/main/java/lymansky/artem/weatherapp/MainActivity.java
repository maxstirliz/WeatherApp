package lymansky.artem.weatherapp;

import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.List;

import lymansky.artem.weatherapp.adapters.DailyWeatherAdapter;
import lymansky.artem.weatherapp.data.WeatherData;
import lymansky.artem.weatherapp.db.WeatherEntry;
import lymansky.artem.weatherapp.fragments.DailyWeatherFragment;
import lymansky.artem.weatherapp.fragments.WeatherDataViewModel;
import lymansky.artem.weatherapp.fragments.WeatherDetailFragment;
import lymansky.artem.weatherapp.network.Client;
import lymansky.artem.weatherapp.network.Service;
import lymansky.artem.weatherapp.utils.Constants;
import lymansky.artem.weatherapp.utils.WeatherDataUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements DailyWeatherAdapter.OnItemClick {

    private WeatherDataViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (null != getSupportActionBar()) {
            getSupportActionBar().hide();
        }
        DailyWeatherAdapter.setOnItemClickListener(this);

        mViewModel = ViewModelProviders.of(this).get(WeatherDataViewModel.class);
        WeatherDetailFragment detailFragment = new WeatherDetailFragment();
        DailyWeatherFragment dailyFragment = new DailyWeatherFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.detail_fragment_container, detailFragment)
                .add(R.id.daily_fragment_container, dailyFragment)
                .commit();


        //TODO: put this request into some networking class
        Client.getService(Service.class).getWeatherByCityName(
                "Zaporizhzhia",
                Constants.METRICS_VALUE,
                Constants.WEATHER_API_KEY)
                .enqueue(new Callback<WeatherData>() {
                    @Override
                    public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                        if (response.body() != null) {

                            final List<WeatherEntry> entries = WeatherDataUtils.convertDataToEntry(response.body());
                            final List<WeatherEntry> entriesToRemove = WeatherDataUtils.selectLessThan(entries);
                            //TODO: Probably, IntentService is less memory-leak here
                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... voids) {
                                    mViewModel.deleteAllSelected(entriesToRemove);
                                    mViewModel.insertAll(entries);
                                    return null;
                                }
                            }.execute();
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherData> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                        String throwable = t.getClass().getSimpleName();
                    }
                });
    }

    @Override
    public void onItemClick(int day) {
        WeatherDetailFragment fragment = new WeatherDetailFragment();
        fragment.setDayToShow(day);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.detail_fragment_container, fragment)
                .commit();
    }
}
