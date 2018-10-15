package lymansky.artem.weatherapp.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.List;

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

public class ScheduledJobService extends JobService {

    private AsyncTask mUpdateTask;

    @Override
    public boolean onStartJob(final JobParameters job) {
        final SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        String cityName = sharedPreferences.getString(getString(R.string.city_name_key),
                getString(R.string.default_city_name));
        float lat = sharedPreferences.getFloat(getString(R.string.latitude_key), 47.8388F);
        float lon = sharedPreferences.getFloat(getString(R.string.longitude_key), 35.1396F);

        final AppDatabase database = AppDatabase.getInstance(this);
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

                            mUpdateTask = new AsyncTask() {

                                @Override
                                protected Object doInBackground(Object[] objects) {
                                    database.weatherDao().deleteOldItems(TimeUtils.getCurrentDayNumber());
                                    database.weatherDao().insertAll(entries);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putLong(getString(R.string.last_update_key),
                                            System.currentTimeMillis());
                                    editor.apply();
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Object o) {
                                    jobFinished(job, false);
                                }
                            };
                            mUpdateTask.execute();
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherData> call, Throwable t) {

                    }
                });

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mUpdateTask != null) {
            mUpdateTask.cancel(true);
        }
        return true;
    }
}
