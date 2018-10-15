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
import lymansky.artem.weatherapp.data.DayItem;
import lymansky.artem.weatherapp.utils.IconUtils;

public class DailyWeatherAdapter extends RecyclerView.Adapter<DailyWeatherAdapter.DailyWeatherViewHolder> {
    
    private List<DayItem> mDayItems;

    private int mSelectedPos = RecyclerView.NO_POSITION;

    private static OnItemClick itemClickListener;

    public interface OnItemClick {
        void onItemClick(int day);
    }

    public static void setOnItemClickListener(OnItemClick listener) {
        itemClickListener = listener;
    }

    public DailyWeatherAdapter(List<DayItem> items) {
        mDayItems = items;
    }

    @NonNull
    @Override
    public DailyWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.list_item_daily, viewGroup, false);
        return new DailyWeatherViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mDayItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull DailyWeatherViewHolder dailyWeatherViewHolder, int i) {
        dailyWeatherViewHolder.setSelected(i == mSelectedPos);
        dailyWeatherViewHolder.bindTo(i);
    }

    public void setNewData(List<DayItem> items) {
        mDayItems = items;
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
            itemClickListener.onItemClick(mDayItems.get(mSelectedPos).getDayNumber());
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        void bindTo(int i) {
            textViewWeekDay.setText(mDayItems.get(i).getWeekDay());
            textViewTempRange.setText(mDayItems.get(i).getTempRange());
            int icRes = IconUtils.getIconResource(mDayItems.get(i).getIcon(), isSelected);
            imageViewWeatherIcon.setImageResource(icRes);
            if (isSelected) {
                textViewWeekDay.setTextColor(view.getResources().getColor(R.color.colorPrimary));
                textViewTempRange.setTextColor(view.getResources().getColor(R.color.colorPrimary));
                view.setBackgroundColor(view.getResources().getColor(R.color.colorSelectionBg));
            } else {
                textViewWeekDay.setTextColor(view.getResources().getColor(R.color.colorText));
                textViewTempRange.setTextColor(view.getResources().getColor(R.color.colorText));
                view.setBackgroundColor(view.getResources().getColor(R.color.colorBackground));
            }
        }
    }
}
