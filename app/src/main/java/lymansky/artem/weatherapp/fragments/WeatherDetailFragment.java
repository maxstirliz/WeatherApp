package lymansky.artem.weatherapp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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

public class WeatherDetailFragment extends Fragment {

    private TextView mCityName;
    private TextView mFullDate;
    private TextView mTemperature;
    private TextView mHumidity;
    private TextView mWindSpeed;
    private ImageView mWeatherPic;
    private ImageView mWindDirectionPic;

    private int mDayToShow = -1;
    private RecyclerView mRv;
    HourlyWeatherAdapter mAdapter;
    LinearLayoutManager mLayoutManger;

    public WeatherDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_detail_fragment, container, false);
        initViews(view);

        WeatherDataViewModel viewModel = ViewModelProviders.of(getActivity()).get(WeatherDataViewModel.class);
        viewModel.getAllDays().observe(this, new Observer<List<WeatherEntry>>() {
            @Override
            public void onChanged(@Nullable List<WeatherEntry> entries) {
                if (entries != null && entries.size() > 0) {
                    if(mDayToShow < 0) {
                        mDayToShow = entries.get(0).getDayNumber();
                    }
                    bindData(entries);
                    if(mAdapter == null) {
                        mAdapter = new HourlyWeatherAdapter(WeatherDataUtils.getWeatherOfDay(entries, mDayToShow));
                        mRv.setAdapter(mAdapter);
                    } else {
                        mAdapter.setNewData(WeatherDataUtils.getWeatherOfDay(entries, mDayToShow));
                    }
                }
            }
        });


        //TODO: city name, last update time, etc. to and from SharedPreferences
        mCityName.setText("Zaporizhzhia");

        return view;
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
        mLayoutManger = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRv = view.findViewById(R.id.rv_hourly_fragment);
        mRv.setHasFixedSize(true);
        mRv.setLayoutManager(mLayoutManger);
    }

    private void bindData(List<WeatherEntry> entries) {
        WeatherEntry e;
        List<WeatherEntry> dayEntries = WeatherDataUtils.getWeatherOfDay(entries, mDayToShow);
        if(dayEntries.get(0).getTime() > System.currentTimeMillis()) {
            e = dayEntries.get(0);
        } else {
            int index = WeatherDataUtils.getRecentDayTimeIndex(entries, mDayToShow);
            e = entries.get(index);
        }

        mFullDate.setText(TimeUtils.getFullDateFormat(e.getTime()));
        String temp = getString(R.string.df_temperature_range,
                WeatherDataUtils.getMaxTempByDay(entries, mDayToShow),
                WeatherDataUtils.getMinTempByDay(entries, mDayToShow));
        mTemperature.setText(temp);
        mHumidity.setText(getString(R.string.df_humidity, e.getHumidity()));
        mWindSpeed.setText(getString(R.string.df_wind_speed, e.getWind()));
        mWindDirectionPic.setImageResource(IconUtils.getWindDirectionResource(e.getWindDirection()));
        mWeatherPic.setImageResource(IconUtils.getIconResource(e.getPic()));
    }
}
