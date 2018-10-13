package lymansky.artem.weatherapp.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import lymansky.artem.weatherapp.MainActivity;
import lymansky.artem.weatherapp.R;
import lymansky.artem.weatherapp.data.WeatherData;
import lymansky.artem.weatherapp.db.AppDatabase;
import lymansky.artem.weatherapp.db.WeatherEntry;
import lymansky.artem.weatherapp.networking.Client;
import lymansky.artem.weatherapp.networking.Service;
import lymansky.artem.weatherapp.utils.Constants;
import lymansky.artem.weatherapp.utils.TimeUtils;
import lymansky.artem.weatherapp.utils.WeatherDataUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateRoomService extends IntentService {

    private static final String LOG_TAG = UpdateRoomService.class.getSimpleName();


    public UpdateRoomService() {
        super("UpdateRoomService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String cityName = getString(R.string.default_city_name);
        float lat = 47.8388F;
        float lon = 35.1396F;

        final AppDatabase database = AppDatabase.getInstance(this);
        if (intent != null) {
            cityName = intent.getStringExtra(MainActivity.EXTRA_CITY_NAME);
            lat = intent.getFloatExtra(MainActivity.EXTRA_CITY_LAT, 47.8388F);
            lon = intent.getFloatExtra(MainActivity.EXTRA_CITY_LON, 35.1396F);
        }

        Client.getService(Service.class).getWeatherByCoord(
                lat,
                lon,
                Constants.METRICS_VALUE,
                Constants.WEATHER_API_KEY)
                .enqueue(new Callback<WeatherData>() {
                    @Override
                    public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                        if (response.body() != null) {

                            final List<WeatherEntry> entries = WeatherDataUtils.convertDataToEntry(response.body());

                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... voids) {
                                    database.weatherDao().deleteOldItems(TimeUtils.getCurrentDayNumber());
                                    database.weatherDao().insertAll(entries);
                                    return null;
                                }
                            }.execute();
                        }

                    }

                    @Override
                    public void onFailure(Call<WeatherData> call, Throwable t) {
                        String error = t.getClass().getSimpleName();
                        Log.e(LOG_TAG, getString(R.string.download_error) + error);
                    }
                });
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key)
                , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.city_name_key),
                cityName);
        editor.putLong(getString(R.string.last_update_key),
                System.currentTimeMillis());
        editor.putFloat(getString(R.string.latitude_key), lat);
        editor.putFloat(getString(R.string.longitude_key), lon);
        editor.apply();
    }
}
