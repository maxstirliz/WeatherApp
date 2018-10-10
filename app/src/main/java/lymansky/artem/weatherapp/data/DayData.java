package lymansky.artem.weatherapp.data;

import com.google.gson.annotations.SerializedName;

class DayData {

    @SerializedName("dt")
    long date; //Unix, UTC

    @SerializedName("main")
    MainData mainData;

    @SerializedName("weather")
    Weather[] weather;

    @SerializedName("wind")
    Wind wind;

    public long getDate() {
        return date;
    }

    public MainData getMainData() {
        return mainData;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public Wind getWind() {
        return wind;
    }
}
