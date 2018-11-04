package lymansky.artem.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import lymansky.artem.weatherapp.fragments.DailyWeatherFragment;
import lymansky.artem.weatherapp.fragments.LocationDialog;
import lymansky.artem.weatherapp.fragments.WeatherDetailFragment;
import lymansky.artem.weatherapp.services.UpdateRoomService;
import lymansky.artem.weatherapp.utils.AutoUpdateUtils;

public class MainActivity extends AppCompatActivity implements WeatherDetailFragment.DetailFragmentButtonListener,
        LocationDialog.DialogButtonsListener {

    public static final String EXTRA_CITY_NAME = "city-name-extra";
    public static final String EXTRA_CITY_LAT = "city-latitude-extra";
    public static final String EXTRA_CITY_LON = "city-longitude-extra";

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 555;

    private static final int CITY_AUTOCOMPLETE_REQUEST_CODE = 1;

    private static final long UPDATE_HOURS_LIMIT = 1L;
    private static final long UPDATE_DATABASE_MILLISECONDS =
            TimeUnit.HOURS.toMillis(UPDATE_HOURS_LIMIT);

    private SharedPreferences mSharedPreferences;
    private FusedLocationProviderClient mFusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (null != getSupportActionBar()) {
            getSupportActionBar().hide();
        }
        mSharedPreferences =
                getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);


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

    //WeatherDetailFragment buttons handling
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
            case WeatherDetailFragment.BUTTON_PLACE:
                startDialog();
                break;
        }
    }


    //CITY SELECTION RESULT HANDLING
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

    //DIALOG HANDLING
    @Override
    public void onDialogButtonClick(int button) {
        switch (button) {
            case DialogInterface.BUTTON_POSITIVE:
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        Toast.makeText(this, R.string.access_needed, Toast.LENGTH_SHORT).show();
                    } else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                } else {
                    processDeviceLocation();
                }
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                Toast.makeText(this, R.string.using_current_location_settings, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    processDeviceLocation();
                } else {
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
        }
    }

    //CUSTOM METHODS
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

    private void startDialog() {
        DialogFragment dialog = new LocationDialog();
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    //Use last saved approximate geodata to load the weather
    private void processDeviceLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (null != location) {
                            String placeName;
                            double lat = location.getLatitude();
                            double lon = location.getLongitude();

                            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
                                if (addresses.size() > 0) {
                                    placeName = addresses.get(0).getLocality();
                                    Intent intent = new Intent(MainActivity.this, UpdateRoomService.class);
                                    intent.putExtra(EXTRA_CITY_NAME, placeName);
                                    intent.putExtra(EXTRA_CITY_LAT, (float) lat);
                                    intent.putExtra(EXTRA_CITY_LON, (float) lon);
                                    startService(intent);
                                }
                            } catch (IOException e) {
                                Toast.makeText(MainActivity.this, R.string.problems_to_get_location, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
