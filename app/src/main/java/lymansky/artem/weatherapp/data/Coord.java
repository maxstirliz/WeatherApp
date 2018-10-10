package lymansky.artem.weatherapp.data;

import com.google.gson.annotations.SerializedName;

public class Coord {

    @SerializedName("lon")
    double lon;

    @SerializedName("lat")
    double lat;

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }
}
