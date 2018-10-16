package lymansky.artem.weatherapp.fragments;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import lymansky.artem.weatherapp.R;
import lymansky.artem.weatherapp.WeatherDataViewModel;
import lymansky.artem.weatherapp.adapters.HourlyWeatherAdapter;
import lymansky.artem.weatherapp.db.WeatherEntry;
import lymansky.artem.weatherapp.utils.IconUtils;
import lymansky.artem.weatherapp.utils.TimeUtils;
import lymansky.artem.weatherapp.utils.WeatherDataUtils;

public class WeatherDetailFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String DATABASE_STATE = "database-state";

    public static final int BUTTON_LOCATION = 0;
    public static final int BUTTON_SYNC = 1;

    private TextView mCityName;
    private TextView mFullDate;
    private TextView mTemperature;
    private TextView mHumidity;
    private TextView mWindSpeed;
    private ImageView mWeatherPic;
    private ImageView mWindDirectionPic;
    private ImageView mPlaceIcon;

    private ImageView mTempIcon;
    private ImageView mHumidIcon;
    private ImageView mWindIcon;
    private ConstraintLayout mNoInternetBanner;
    private ImageView mSyncIcon;
    private boolean mConnectedToInternet;
    private boolean mDatabaseEmpty;

    private int mDayToShow = -1;
    private RecyclerView mRv;
    private WeatherDataViewModel mViewModel;
    private HourlyWeatherAdapter mAdapter;


    private DetailFragmentButtonListener mListener;
    private SharedPreferences mSharedPreferences;
    private List<WeatherEntry> mEntries;

    public WeatherDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_detail_fragment, container, false);
        initViews(view);

        if (savedInstanceState != null) {
            mDatabaseEmpty = savedInstanceState.getBoolean(DATABASE_STATE);
        }


        mViewModel = ViewModelProviders.of(getActivity()).get(WeatherDataViewModel.class);
        mViewModel.getAll().observe(this, new Observer<List<WeatherEntry>>() {
            @Override
            public void onChanged(@Nullable List<WeatherEntry> entries) {
                if (entries != null && entries.size() > 0) {
                    mEntries = entries;
                    if (mDayToShow < 0) {
                        mDayToShow = TimeUtils.getDayNumber(System.currentTimeMillis());
                    }
                    List<WeatherEntry> dayEntries = WeatherDataUtils.getWeatherOfDay(entries, mDayToShow);
                    if (dayEntries.size() == 0) {
                        mDayToShow++;
                        dayEntries = WeatherDataUtils.getWeatherOfDay(entries, mDayToShow);
                    }
                    bindData(dayEntries);
                    hideBanner();
                    hideSync();
                    showViews();
                    if (mAdapter == null) {
                        mAdapter = new HourlyWeatherAdapter(dayEntries);
                        mRv.setAdapter(mAdapter);
                    } else {
                        mAdapter.setNewData(dayEntries);
                    }
                } else {
                    mDatabaseEmpty = true;
                    if (!mConnectedToInternet) {
                        hideViews();
                        showBanner();
                        showSync();
                    }
                }
            }
        });
        mViewModel.getDayToShow().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer != null && integer != 0) {
                    mDayToShow = integer;
                    if(mEntries == null) {
                        return;
                    }
                    List<WeatherEntry> dayEntries = WeatherDataUtils.getWeatherOfDay(mEntries, mDayToShow);
                    if (dayEntries.size() == 0) {
                        mViewModel.selectDay(++mDayToShow);
                        dayEntries = WeatherDataUtils.getWeatherOfDay(mEntries, mDayToShow);
                    }
                    bindData(dayEntries);
                    if (mAdapter == null) {
                        mAdapter = new HourlyWeatherAdapter(dayEntries);
                        mRv.setAdapter(mAdapter);
                    } else {
                        mAdapter.setNewData(dayEntries);
                    }
                }
            }
        });

        String cityName = mSharedPreferences.getString(getString(R.string.city_name_key), "");
        mCityName.setText(cityName);

        mPlaceIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonPressed(BUTTON_LOCATION);
            }
        });

        mSyncIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonPressed(BUTTON_SYNC);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSharedPreferences = context.getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        mListener = (DetailFragmentButtonListener) context;
        mConnectedToInternet = isNetworkAvailable((Activity) context);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        if (isNetworkAvailable(getActivity())) {
            hideBanner();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(DATABASE_STATE, mDatabaseEmpty);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.city_name_key))) {
            String cityName = sharedPreferences.getString(getString(R.string.city_name_key), "");
            mCityName.setText(cityName);
        }
    }

    private void initViews(View view) {
        mCityName = view.findViewById(R.id.city_name);
        mFullDate = view.findViewById(R.id.tv_full_date);
        mTemperature = view.findViewById(R.id.tv_temperature);
        mHumidity = view.findViewById(R.id.tv_humidity);
        mWindSpeed = view.findViewById(R.id.tv_wind_speed);
        mWeatherPic = view.findViewById(R.id.ic_weather_pic);
        mWindDirectionPic = view.findViewById(R.id.ic_wind_direction);
        mPlaceIcon = view.findViewById(R.id.ic_place_picker);

        mTempIcon = view.findViewById(R.id.ic_temperature);
        mHumidIcon = view.findViewById(R.id.ic_humidity);
        mWindIcon = view.findViewById(R.id.ic_wind);
        mNoInternetBanner = view.findViewById(R.id.cl_no_internet_banner);
        mSyncIcon = view.findViewById(R.id.ic_sync);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRv = view.findViewById(R.id.rv_hourly_fragment);
        mRv.setHasFixedSize(true);
        mRv.setLayoutManager(layoutManager);
    }

    private void bindData(List<WeatherEntry> entries) {
        WeatherEntry e = entries.get(WeatherDataUtils.getRecentHourIndex(entries));

        mFullDate.setText(TimeUtils.getFullDateFormat(e.getTime()));
        String temp = getString(R.string.df_temperature_range,
                WeatherDataUtils.getMaxTemp(entries),
                WeatherDataUtils.getMinTemp(entries));
        mTemperature.setText(temp);
        mHumidity.setText(getString(R.string.df_humidity, e.getHumidity()));
        mWindSpeed.setText(getString(R.string.df_wind_speed, e.getWind()));
        mWindDirectionPic.setImageResource(IconUtils.getWindDirectionResource(e.getWindDirection()));
        mWeatherPic.setImageResource(IconUtils.getIconResource(e.getPic()));
    }

    private boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void hideViews() {
        mTempIcon.setVisibility(View.INVISIBLE);
        mHumidIcon.setVisibility(View.INVISIBLE);
        mWindIcon.setVisibility(View.INVISIBLE);
    }

    private void showViews() {
        mTempIcon.setVisibility(View.VISIBLE);
        mHumidIcon.setVisibility(View.VISIBLE);
        mWindIcon.setVisibility(View.VISIBLE);
    }

    private void hideBanner() {
        mNoInternetBanner.setVisibility(View.INVISIBLE);
    }

    private void showBanner() {
        mNoInternetBanner.setVisibility(View.VISIBLE);
    }

    private void showSync() {
        mSyncIcon.setVisibility(View.VISIBLE);
    }

    private void hideSync() {
        mSyncIcon.setVisibility(View.GONE);
    }


    public interface DetailFragmentButtonListener {
        void onButtonPressed(int action);
    }
}
