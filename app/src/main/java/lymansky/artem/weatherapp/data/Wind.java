package lymansky.artem.weatherapp.data;

import com.google.gson.annotations.SerializedName;

public class Wind {

    @SerializedName("speed")
    double speed; // in mps

    @SerializedName("deg")
    double degrees;

    public double getSpeed() {
        return speed;
    }

    public double getDegrees() {
        return degrees;
    }
}
