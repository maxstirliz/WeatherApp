package lymansky.artem.weatherapp.data;

import com.google.gson.annotations.SerializedName;

class Weather {

    @SerializedName("icon")
    private String icon;

    public String getIcon() {
        return icon;
    }
}
