package lymansky.artem.weatherapp.data;

import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("icon")
    private String icon;

    public String getIcon() {
        return icon;
    }
}
