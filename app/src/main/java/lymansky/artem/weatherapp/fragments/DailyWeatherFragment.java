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

import java.util.List;

import lymansky.artem.weatherapp.R;
import lymansky.artem.weatherapp.WeatherDataViewModel;
import lymansky.artem.weatherapp.adapters.DailyWeatherAdapter;
import lymansky.artem.weatherapp.data.DayItem;
import lymansky.artem.weatherapp.db.WeatherEntry;
import lymansky.artem.weatherapp.utils.WeatherDataUtils;

public class DailyWeatherFragment extends Fragment {

    private RecyclerView mRv;
    private DailyWeatherAdapter mAdapter;
    private WeatherDataViewModel mViewModel;


    public DailyWeatherFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.daily_weather_fragment, container, false);
        initViews(view);

        mViewModel = ViewModelProviders.of(getActivity()).get(WeatherDataViewModel.class);
        mViewModel.getAll().observe(this, new Observer<List<WeatherEntry>>() {
            @Override
            public void onChanged(@Nullable List<WeatherEntry> entries) {
                if (entries != null && entries.size() > 0) {
                    List<DayItem> dayItems = WeatherDataUtils.getDayItems(entries, getContext());
                    if (mAdapter == null) {
                        mAdapter = new DailyWeatherAdapter(dayItems , DailyWeatherFragment.this);
                        mRv.setAdapter(mAdapter);
                    } else {
                        mAdapter.setNewData(dayItems);
                    }
                }
            }
        });



        return view;
    }

    private void initViews(View view) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRv = view.findViewById(R.id.rv_daily_fragment);
        mRv.setHasFixedSize(true);
        mRv.setLayoutManager(layoutManager);
    }
}
