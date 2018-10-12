package lymansky.artem.weatherapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherData {


    @SerializedName("list")
    private List<DayData> list;

    @SerializedName("city")
    private City cityInfo;

    public List<DayData> getList() {
        return list;
    }

    public City getCityInfo() {
        return cityInfo;
    }

}
