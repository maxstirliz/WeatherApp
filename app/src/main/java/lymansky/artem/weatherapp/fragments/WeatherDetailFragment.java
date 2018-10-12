package lymansky.artem.weatherapp.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import lymansky.artem.weatherapp.R;
import lymansky.artem.weatherapp.db.WeatherEntry;
import lymansky.artem.weatherapp.utils.IconUtils;
import lymansky.artem.weatherapp.utils.TimeUtils;

public class WeatherDetailFragment extends Fragment {

    private List<WeatherEntry> mEntries;

    private TextView mCityName;
    private TextView mFullDate;
    private TextView mTemperature;
    private TextView mHumidity;
    private TextView mWindSpeed;
    private ImageView mWeatherPic;
    private ImageView mWindDirectionPic;

    public WeatherDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_detail_fragment, container, false);
        initViews(view);
        bindData(mEntries);


        //TODO: city name, last update time, etc. to and from SharedPreferences;
        mCityName.setText("Zaporizhzhia");

        return view;
    }

    public void setEntries(List<WeatherEntry> entries) {
        mEntries = entries;
    }

    private void initViews(View view) {
        mCityName = view.findViewById(R.id.city_name);
        mFullDate = view.findViewById(R.id.tv_full_date);
        mTemperature = view.findViewById(R.id.tv_temperature);
        mHumidity = view.findViewById(R.id.tv_humidity);
        mWindSpeed = view.findViewById(R.id.tv_wind_speed);
        mWeatherPic = view.findViewById(R.id.ic_weather_pic);
        mWindDirectionPic = view.findViewById(R.id.ic_wind_direction);
    }

    private void bindData(List<WeatherEntry> entries) {
        WeatherEntry e = entries.get(21);

        mFullDate.setText(TimeUtils.getFullDateFormat(e.getTime()));
        String temp = getString(R.string.df_temperature_range, e.getTempMax(), e.getTempMin());
        mTemperature.setText(temp);
        mHumidity.setText(getString(R.string.df_humidity, e.getHumidity()));
        mWindSpeed.setText(getString(R.string.df_wind_speed, e.getWind()));
        mWindDirectionPic.setImageResource(IconUtils.getWindDirectionResource(e.getWindDirection()));
        mWeatherPic.setImageResource(IconUtils.getIconResource(e.getPic()));

    }
}
