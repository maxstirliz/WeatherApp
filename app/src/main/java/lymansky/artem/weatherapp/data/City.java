package lymansky.artem.weatherapp.data;

import com.google.gson.annotations.SerializedName;

public class City {

    @SerializedName("name")
    String name;

    public String getName() {
        return name;
    }
}
