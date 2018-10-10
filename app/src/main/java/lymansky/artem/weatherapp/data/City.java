package lymansky.artem.weatherapp.data;

import com.google.gson.annotations.SerializedName;

class City {

    @SerializedName("id")
    int id;

    @SerializedName("name")
    String name;

    @SerializedName("coord")
    Coord coord;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coord getCoord() {
        return coord;
    }
}
