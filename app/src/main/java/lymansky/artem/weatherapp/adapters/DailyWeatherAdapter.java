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
import lymansky.artem.weatherapp.utils.WeatherDataUtils;

public class DailyWeatherAdapter extends RecyclerView.Adapter<DailyWeatherAdapter.DailyWeatherViewHolder> {

    private Context mContext;
    private List<WeatherEntry> mEntries;
    private List<WeatherEntry> mDailyWeather;

    private int mSelectedPos = RecyclerView.NO_POSITION;

    private static OnItemClick itemClickListener;

    public interface OnItemClick {
        void onItemClick(int day);
    }

    public static void setOnItemClickListener (OnItemClick listener) {
        itemClickListener = listener;
    }

    public DailyWeatherAdapter(List<WeatherEntry> entries) {
        mEntries = entries;
        mDailyWeather = WeatherDataUtils.getUniqueDays(entries);
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
        dailyWeatherViewHolder.setSelected(i == mSelectedPos);
        dailyWeatherViewHolder.bindTo(i);
    }

    public void setNewData(List<WeatherEntry> entries) {
        mEntries = entries;
        mDailyWeather = WeatherDataUtils.getUniqueDays(entries);
        notifyDataSetChanged();
    }

    class DailyWeatherViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        boolean isSelected;

        View view;
        TextView textViewWeekDay;
        TextView textViewTempRange;
        ImageView imageViewWeatherIcon;

        public DailyWeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            itemView.setOnClickListener(this);
            textViewWeekDay = itemView.findViewById(R.id.tv_daily_week);
            textViewTempRange = itemView.findViewById(R.id.tv_temp_daily);
            imageViewWeatherIcon = itemView.findViewById(R.id.ic_weather_daily);
        }

        @Override
        public void onClick(View v) {
            notifyItemChanged(mSelectedPos);
            mSelectedPos = getAdapterPosition();
            notifyItemChanged(mSelectedPos);
            itemClickListener.onItemClick(mDailyWeather.get(getAdapterPosition()).getDayNumber());
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        void bindTo(int i) {
            int dayNumber = mDailyWeather.get(i).getDayNumber();
            if (isSelected) {
                String day = TimeUtils.getWeekDayFormat(mDailyWeather.get(i).getTime());
                textViewWeekDay.setText(day);
                textViewWeekDay.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                String range = mContext.getString(R.string.df_temperature_range,
                        WeatherDataUtils.getMaxTempByDay(mEntries, dayNumber),
                        WeatherDataUtils.getMinTempByDay(mEntries, dayNumber));
                textViewTempRange.setText(range);
                textViewTempRange.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                int res = IconUtils.getIconResource(mDailyWeather.get(i).getPic(), isSelected);
                imageViewWeatherIcon.setImageResource(res);
                view.setBackgroundColor(mContext.getResources().getColor(R.color.colorSelectionBg));
            } else {
                String day = TimeUtils.getWeekDayFormat(mDailyWeather.get(i).getTime());
                textViewWeekDay.setText(day);
                textViewWeekDay.setTextColor(mContext.getResources().getColor(R.color.colorText));
                String range = mContext.getString(R.string.df_temperature_range,
                        WeatherDataUtils.getMaxTempByDay(mEntries, dayNumber),
                        WeatherDataUtils.getMinTempByDay(mEntries, dayNumber));
                textViewTempRange.setText(range);
                textViewTempRange.setTextColor(mContext.getResources().getColor(R.color.colorText));
                int res = IconUtils.getIconResource(mDailyWeather.get(i).getPic(), isSelected);
                imageViewWeatherIcon.setImageResource(res);
                view.setBackgroundColor(mContext.getResources().getColor(R.color.colorBackground));
            }
        }
    }
}
