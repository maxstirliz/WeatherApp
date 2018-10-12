package lymansky.artem.weatherapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import lymansky.artem.weatherapp.data.WeatherData;
import lymansky.artem.weatherapp.db.AppDatabase;
import lymansky.artem.weatherapp.db.WeatherEntry;
import lymansky.artem.weatherapp.fragments.DailyWeatherFragment;
import lymansky.artem.weatherapp.fragments.HourlyWeatherFragment;
import lymansky.artem.weatherapp.fragments.WeatherDataViewModel;
import lymansky.artem.weatherapp.fragments.WeatherDetailFragment;
import lymansky.artem.weatherapp.network.Client;
import lymansky.artem.weatherapp.network.Service;
import lymansky.artem.weatherapp.utils.Constants;
import lymansky.artem.weatherapp.utils.WeatherDataToEntry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private WeatherDataViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (null != getSupportActionBar()) {
            getSupportActionBar().hide();
        }

        mViewModel = ViewModelProviders.of(this).get(WeatherDataViewModel.class);

        Client.getService(Service.class).getWeatherByCityName(
                "Zaporizhzhia",
                Constants.METRICS_VALUE,
                Constants.WEATHER_API_KEY)
                .enqueue(new Callback<WeatherData>() {
                    @Override
                    public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                        if (response.body() != null) {

                            final List<WeatherEntry> entries = WeatherDataToEntry.dataToEntryConverter(response.body());
                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... voids) {
                                    mViewModel.insertAll(entries);
                                    return null;
                                }
                            }.execute();
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherData> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Connection Error, check logs", Toast.LENGTH_SHORT).show();
                        String throwable = t.getClass().getSimpleName();
                        Log.e("onFailure", throwable);
                    }
                });
        mViewModel.getAllDays().observe(this, new Observer<List<WeatherEntry>>() {
            @Override
            public void onChanged(@Nullable List<WeatherEntry> entries) {
                WeatherDetailFragment fragment = new WeatherDetailFragment();
                fragment.setEntries(entries);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.detail_fragment_container, fragment)
                        .commit();
            }
        });
    }
}
