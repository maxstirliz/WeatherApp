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
import lymansky.artem.weatherapp.adapters.DailyWeatherAdapter;
import lymansky.artem.weatherapp.db.WeatherEntry;

public class DailyWeatherFragment extends Fragment {

    private RecyclerView mRv;
    private DailyWeatherAdapter mAdapter;


    public DailyWeatherFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.daily_weather_fragment, container, false);
        initViews(view);

        WeatherDataViewModel viewModel = ViewModelProviders.of(getActivity()).get(WeatherDataViewModel.class);
        viewModel.getAllDays().observe(this, new Observer<List<WeatherEntry>>() {
            @Override
            public void onChanged(@Nullable List<WeatherEntry> entries) {
                if (entries != null && entries.size() > 0) {

                    if(mAdapter == null) {
                        mAdapter = new DailyWeatherAdapter(entries);
                        mRv.setAdapter(mAdapter);
                    } else {
                        mAdapter.setNewData(entries);
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
