package lymansky.artem.weatherapp.utils;

import java.util.ArrayList;
import java.util.List;

import lymansky.artem.weatherapp.data.DayData;
import lymansky.artem.weatherapp.data.WeatherData;
import lymansky.artem.weatherapp.db.WeatherEntry;

public class WeatherDataToEntry {

    public static List<WeatherEntry> dataToEntryConverter(WeatherData dataSet) {
        List<WeatherEntry> entries = new ArrayList<>();
        if (dataSet == null) {
            return null;
        }
        List<DayData> dayData = dataSet.getList();


        for(DayData d : dayData) {
            WeatherEntry entry = new WeatherEntry();
            long local = TimeUtils.convertToLocal(d.getDate());
            entry.setTime(local);
            entry.setDayNumber(TimeUtils.getDayNumber(local));
            entry.setPic(d.getWeather().getIcon());
            entry.setTemp((int) d.getMainData().getTempAverage());
            entry.setTempMax((int) d.getMainData().getTempMax());
            entry.setTempMin((int) d.getMainData().getTempMin());
            entry.setHumidity((int) d.getMainData().getHumidity());
            entry.setWind((int) d.getWind().getSpeed());
            entry.setWindDirection((int) d.getWind().getDegrees());

            entries.add(entry);
        }
        return entries;
    }
}
