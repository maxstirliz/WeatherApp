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

public class HourlyWeatherAdapter extends RecyclerView.Adapter<HourlyWeatherAdapter.HourlyWeatherViewHolder> {

    private List<WeatherEntry> mWeatherOfDay;
    private Context mContext;

    public HourlyWeatherAdapter(List<WeatherEntry> weatherOfDay) {
        mWeatherOfDay = weatherOfDay;
    }

    @NonNull
    @Override
    public HourlyWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.list_item_hourly, viewGroup, false);
        return new HourlyWeatherViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mWeatherOfDay.size();
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyWeatherViewHolder hourlyWeatherViewHolder, int i) {
        hourlyWeatherViewHolder.bindTo(i);
    }

    public void setNewData(List<WeatherEntry> newData) {
        mWeatherOfDay = newData;
        notifyDataSetChanged();
    }

    class HourlyWeatherViewHolder extends RecyclerView.ViewHolder {

        TextView textViewHours;
        TextView textViewTemp;
        ImageView imageViewWeatherIcon;

        public HourlyWeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHours = itemView.findViewById(R.id.tv_hours);
            textViewTemp = itemView.findViewById(R.id.tv_hourly_temperature);
            imageViewWeatherIcon = itemView.findViewById(R.id.ic_weather_hourly);
        }

        void bindTo(int i) {
            String hours = TimeUtils.getHourString(mWeatherOfDay.get(i).getTime());
            textViewHours.setText(hours);
            String temp = mContext.getString(R.string.item_hourly_temp, mWeatherOfDay.get(i).getTemp());
            textViewTemp.setText(temp);
            int icRes = IconUtils.getIconResource(mWeatherOfDay.get(i).getPic());
            imageViewWeatherIcon.setImageResource(icRes);
        }
    }
}
