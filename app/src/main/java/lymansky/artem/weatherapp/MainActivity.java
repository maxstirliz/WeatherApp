package lymansky.artem.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import java.util.concurrent.TimeUnit;

import lymansky.artem.weatherapp.adapters.DailyWeatherAdapter;
import lymansky.artem.weatherapp.fragments.DailyWeatherFragment;
import lymansky.artem.weatherapp.fragments.WeatherDetailFragment;
import lymansky.artem.weatherapp.services.UpdateRoomService;
import lymansky.artem.weatherapp.utils.AutoUpdateUtils;

public class MainActivity extends AppCompatActivity implements DailyWeatherAdapter.OnItemClick,
        WeatherDetailFragment.OnLocationPickerListener {

    public static final String EXTRA_CITY_NAME = "city-name-extra";
    public static final String EXTRA_CITY_LAT = "city-latitude-extra";
    public static final String EXTRA_CITY_LON = "city-longitude-extra";

    private static final int CITY_AUTOCOMPLETE_REQUEST_CODE = 1;

    private static final long UPDATE_HOURS_LIMIT = 1L;
    private static final long UPDATE_DATABASE_MILLISECONDS =
            TimeUnit.HOURS.toMillis(UPDATE_HOURS_LIMIT);

    private SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (null != getSupportActionBar()) {
            getSupportActionBar().hide();
        }
        mSharedPreferences =
                getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);


        DailyWeatherAdapter.setOnItemClickListener(this);


        if (savedInstanceState == null) {
            WeatherDetailFragment detailFragment = new WeatherDetailFragment();
            DailyWeatherFragment dailyFragment = new DailyWeatherFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.detail_fragment_container, detailFragment)
                    .add(R.id.daily_fragment_container, dailyFragment)
                    .commit();

            AutoUpdateUtils.scheduleWeatherUpdater(this);
        }


        long lastUpdateTime = mSharedPreferences.getLong(getString(R.string.last_update_key), 0L);
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdateTime > UPDATE_DATABASE_MILLISECONDS) {
            startUpdateService();
        }
    }


    @Override
    public void onItemClick(int day) {
        WeatherDetailFragment fragment = new WeatherDetailFragment();
        fragment.setDayToShow(day);
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.detail_fragment_container, fragment)
                .commit();
    }

    @Override
    public void onButtonPressed(int button) {
        switch (button) {
            case WeatherDetailFragment.BUTTON_LOCATION:
                try {
                    AutocompleteFilter cityFilter = new AutocompleteFilter.Builder()
                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                            .build();
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setFilter(cityFilter)
                            .build(this);
                    startActivityForResult(intent, CITY_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    Toast.makeText(this, getString(R.string.google_ps_not_working), Toast.LENGTH_SHORT).show();
                } catch (GooglePlayServicesNotAvailableException e) {
                    Toast.makeText(this, getString(R.string.google_ps_not_available), Toast.LENGTH_SHORT).show();
                }
                break;
            case WeatherDetailFragment.BUTTON_SYNC:
                startUpdateService();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CITY_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);

                LatLng latLng = place.getLatLng();
                String cityName = place.getName().toString();

                Intent intent = new Intent(this, UpdateRoomService.class);
                intent.putExtra(EXTRA_CITY_NAME, cityName);
                intent.putExtra(EXTRA_CITY_LAT, (float) latLng.latitude);
                intent.putExtra(EXTRA_CITY_LON, (float) latLng.longitude);
                startService(intent);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Toast.makeText(this, getString(R.string.error_picking_up_city), Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, getString(R.string.result_canceled), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startUpdateService() {
        Intent intent = new Intent(this, UpdateRoomService.class);
        String cityName = mSharedPreferences.getString(getString(R.string.city_name_key),
                getString(R.string.default_city_name));
        float lat = mSharedPreferences.getFloat(getString(R.string.latitude_key),
                47.8388F);
        float lon = mSharedPreferences.getFloat(getString(R.string.longitude_key),
                35.1396F);
        intent.putExtra(EXTRA_CITY_NAME, cityName);
        intent.putExtra(EXTRA_CITY_LAT, lat);
        intent.putExtra(EXTRA_CITY_LON, lon);
        startService(intent);
    }
}
