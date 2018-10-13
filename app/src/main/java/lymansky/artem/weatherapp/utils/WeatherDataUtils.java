package lymansky.artem.weatherapp.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import lymansky.artem.weatherapp.data.DayData;
import lymansky.artem.weatherapp.data.WeatherData;
import lymansky.artem.weatherapp.db.WeatherEntry;

public class WeatherDataUtils {

    /**
     * Converts data retrieved by retrofit2 @WeatherData into @WeatherEntry to store in Room database
     *
     * @param data
     * @return
     */
    public static List<WeatherEntry> convertDataToEntry(WeatherData data) {
        List<WeatherEntry> entries = new ArrayList<>();
        if (data == null) {
            return null;
        }

        List<DayData> dayData = data.getList();

        for (DayData d : dayData) {
            WeatherEntry entry = new WeatherEntry();
            long timestamp = TimeUtils.convertToMilliseconds(d.getDate());
            entry.setTime(timestamp);
            entry.setDayNumber(TimeUtils.getDayNumber(timestamp));
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

    /**
     * Returns city name
     *
     * @param data
     * @return
     */
    public static String getCityName(WeatherData data) {
        return data.getCityInfo().getName();
    }

    /**
     * Returns all the entries of the given day number
     *
     * @param entries
     * @param day
     * @return
     */
    public static List<WeatherEntry> getWeatherOfDay(List<WeatherEntry> entries, int day) {
        List<WeatherEntry> newEntries = new ArrayList<>();
        for (WeatherEntry e : entries) {
            if (day == e.getDayNumber()) {
                newEntries.add(e);
            }
        }
        return newEntries;
    }

    /**
     * Returns a list of unique day instances from all entries
     *
     * @param entries
     * @return
     */
    public static List<WeatherEntry> getUniqueDays(List<WeatherEntry> entries) {
        List<WeatherEntry> newEntries = new ArrayList<>();
        newEntries.add(entries.get(0));
        int day = entries.get(0).getDayNumber();
        for (int i = 1; i < entries.size(); ++i) {
            if (day != entries.get(i).getDayNumber()) {
                newEntries.add(entries.get(i));
                day = entries.get(i).getDayNumber();
            }
        }
        return newEntries;
    }

    /**
     * Returns max temperature of the given entries
     *
     * @param entries
     * @return
     */
    public static int getMaxTemp(List<WeatherEntry> entries) {
        int maxTemp = Integer.MIN_VALUE;
        for (WeatherEntry e : entries) {
            if (e.getTempMax() > maxTemp) {
                maxTemp = e.getTempMax();
            }
        }
        return maxTemp;
    }

    /**
     * Returns min temperature of the given entries
     *
     * @param entries
     * @return
     */
    public static int getMinTemp(List<WeatherEntry> entries) {
        int minTemp = Integer.MAX_VALUE;
        for (WeatherEntry e : entries) {
            if (e.getTempMin() < minTemp) {
                minTemp = e.getTempMin();
            }
        }
        return minTemp;
    }

    /**
     * Returns max weather from the whole entries by day
     *
     * @param entries
     * @param day
     * @return
     */
    public static int getMaxTempByDay(List<WeatherEntry> entries, int day) {
        List<WeatherEntry> newEntry = getWeatherOfDay(entries, day);
        return getMaxTemp(newEntry);
    }

    /**
     * Returns min weather from the whole entries by day
     *
     * @param entries
     * @param day
     * @return
     */
    public static int getMinTempByDay(List<WeatherEntry> entries, int day) {
        List<WeatherEntry> newEntry = getWeatherOfDay(entries, day);
        return getMinTemp(newEntry);
    }

    /**
     * Returns index of all entries where time is closer to current timestamp
     * @param allEntries
     * @param day
     * @return
     */
    public static int getRecentDayTimeIndex(List<WeatherEntry> allEntries, int day) {
        List<WeatherEntry> entries = getWeatherOfDay(allEntries, day);
        long currentTime = System.currentTimeMillis();
        long distance = Math.abs(entries.get(0).getTime() - currentTime);
        int index = 0;
        for (int i = 1; i < entries.size(); ++i) {
            long iDistance = Math.abs(entries.get(i).getTime() - currentTime);
            if (iDistance < distance) {
                index = i;
                distance = iDistance;
            }
        }
        return index;
    }

    /**
     * Returns list of items that have timestamp less than today's earlier timestamp
     * @param entries
     * @return
     */
    public static List<WeatherEntry> selectLessThan(List<WeatherEntry> entries) {
        List<WeatherEntry> todays = getWeatherOfDay(entries, TimeUtils.getCurrentDayNumber());
        long timestamp = todays.get(0).getTime();
        List<WeatherEntry> selected = new ArrayList<>();
        for (WeatherEntry e : entries) {
            if(e.getTime() < timestamp) {
                selected.add(e);
            }
        }
        return selected;
    }
}
