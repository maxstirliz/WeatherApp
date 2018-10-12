package lymansky.artem.weatherapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

public class DailyWeatherAdapter extends RecyclerView.Adapter<DailyWeatherAdapter.DailyWeatherViewHolder> {

    private Context mContext;
    private List<WeatherEntry> mDailyWeather;
    private boolean mIsDayTime;

    public DailyWeatherAdapter(List<WeatherEntry> dailyWeather) {
        mDailyWeather = dailyWeather;
    }

    @NonNull
    @Override
    public DailyWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_daily, viewGroup, false);
        return new DailyWeatherViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mDailyWeather.size();
    }

    @Override
    public void onBindViewHolder(@NonNull DailyWeatherViewHolder dailyWeatherViewHolder, int i) {
        dailyWeatherViewHolder.bindTo(i);
    }

    public void updateData(List<WeatherEntry> newData) {
        mDailyWeather = newData;
        notifyDataSetChanged();
    }

    class DailyWeatherViewHolder extends RecyclerView.ViewHolder {

        private boolean mIsSelected;

        TextView textViewWeekDay;
        TextView textViewTempRange;
        ImageView imageViewWeatherIcon;

        public DailyWeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewWeekDay = itemView.findViewById(R.id.tv_daily_week);
            textViewTempRange = itemView.findViewById(R.id.tv_temp_daily);
            imageViewWeatherIcon = itemView.findViewById(R.id.ic_weather_daily);
        }

        void bindTo(int i) {
            String day = TimeUtils.getWeekDayFormat(mDailyWeather.get(i).getTime());
            textViewWeekDay.setText(day);
            String range = mContext.getString(R.string.df_temperature_range,
                    mDailyWeather.get(i).getTempMax(),
                    mDailyWeather.get(i).getTempMin());
            textViewTempRange.setText(range);
            int res = IconUtils.getIconResource(mDailyWeather.get(i).getPic(), mIsSelected);
            imageViewWeatherIcon.setImageResource(res);
        }
    }
}
