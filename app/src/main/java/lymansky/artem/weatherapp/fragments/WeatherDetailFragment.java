package lymansky.artem.weatherapp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import lymansky.artem.weatherapp.adapters.HourlyWeatherAdapter;
import lymansky.artem.weatherapp.db.WeatherEntry;
import lymansky.artem.weatherapp.utils.IconUtils;
import lymansky.artem.weatherapp.utils.TimeUtils;
import lymansky.artem.weatherapp.utils.WeatherDataUtils;

public class WeatherDetailFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private TextView mCityName;
    private TextView mFullDate;
    private TextView mTemperature;
    private TextView mHumidity;
    private TextView mWindSpeed;
    private ImageView mWeatherPic;
    private ImageView mWindDirectionPic;
    private ImageView mPlaceIcon;

    private int mDayToShow = -1;
    private RecyclerView mRv;
    private WeatherDataViewModel mViewModel;
    private HourlyWeatherAdapter mAdapter;
    private LinearLayoutManager mLayoutManger;

    private OnLocationPickerListener mListener;
    private SharedPreferences mSharedPreferences;

    public WeatherDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_detail_fragment, container, false);
        initViews(view);


        mViewModel = ViewModelProviders.of(getActivity()).get(WeatherDataViewModel.class);
        mViewModel.getAll().observe(this, new Observer<List<WeatherEntry>>() {
            @Override
            public void onChanged(@Nullable List<WeatherEntry> entries) {
                if (entries != null && entries.size() > 0) {
                    if (mDayToShow < 0) {
                        mDayToShow = TimeUtils.getDayNumber(System.currentTimeMillis());
                    }
                    List<WeatherEntry> dayEntries = WeatherDataUtils.getWeatherOfDay(entries, mDayToShow);
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
                mListener.onLocationPressed();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSharedPreferences = context.getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        if (context instanceof OnLocationPickerListener) {
            mListener = (OnLocationPickerListener) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.city_name_key))) {
            String cityName = sharedPreferences.getString(getString(R.string.city_name_key), "");
            mCityName.setText(cityName);
        }
    }

    public void setDayToShow(int day) {
        mDayToShow = day;
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

        mLayoutManger = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRv = view.findViewById(R.id.rv_hourly_fragment);
        mRv.setHasFixedSize(true);
        mRv.setLayoutManager(mLayoutManger);
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

    public interface OnLocationPickerListener {
        void onLocationPressed();
    }
}
