package lymansky.artem.weatherapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WeatherData {

    @SerializedName("cod")
    int cod;

    @SerializedName("list")
    ArrayList<DayData> list;

    @SerializedName("city")
    City cityInfo;

    public int getCod() {
        return cod;
    }

    public ArrayList<DayData> getList() {
        return list;
    }

    public City getCityInfo() {
        return cityInfo;
    }

}
