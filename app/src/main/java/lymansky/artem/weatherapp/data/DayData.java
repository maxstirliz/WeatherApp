package lymansky.artem.weatherapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DayData {

    @SerializedName("dt")
    private long date; //Unix, UTC

    @SerializedName("main")
    private MainData mainData;

    @SerializedName("weather")
    private List<Weather> weather;

    @SerializedName("wind")
    private Wind wind;

    public long getDate() {
        return date;
    }

    public MainData getMainData() {
        return mainData;
    }

    public Weather getWeather() {
        return weather.get(0);
    }

    public Wind getWind() {
        return wind;
    }
}
