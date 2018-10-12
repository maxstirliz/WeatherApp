package lymansky.artem.weatherapp;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import lymansky.artem.weatherapp.data.WeatherData;
import lymansky.artem.weatherapp.utils.TimeUtils;

public class WeatherDetailFragment extends Fragment {

    private Context mContext;

    private TextView mCityName;
    private TextView mFullDate;
    private TextView mTemperature;
    private TextView mHumidity;
    private TextView mWindSpeed;
    private ImageView mWeatherPic;

    public WeatherDetailFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_detail_fragment, container, false);
        bindViews(view);


        //DUMMY DATA
        mCityName.setText("Zaporizhzhia");
        mFullDate.setText(TimeUtils.getFullDateFormat(System.currentTimeMillis()));
        String temp = getString(R.string.df_temperature_range, 27, 17);
        mTemperature.setText(temp);
        String humid = getString(R.string.df_humidity, 45);
        mHumidity.setText(humid);
        String windSp = getString(R.string.df_wind_speed, 10);
        mWindSpeed.setText(windSp);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void bindViews(View view) {
        mCityName = view.findViewById(R.id.city_name);
        mFullDate = view.findViewById(R.id.tv_full_date);
        mTemperature = view.findViewById(R.id.tv_temperature);
        mHumidity = view.findViewById(R.id.tv_humidity);
        mWindSpeed = view.findViewById(R.id.tv_wind_speed);
        mWeatherPic = view.findViewById(R.id.ic_weather_pic);
    }
}
