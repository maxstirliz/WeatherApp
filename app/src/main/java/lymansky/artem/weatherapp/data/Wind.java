package lymansky.artem.weatherapp.data;

import com.google.gson.annotations.SerializedName;

class Wind {

    @SerializedName("speed")
    int speed; // in mps

    @SerializedName("deg")
    int degrees;

    public int getSpeed() {
        return speed;
    }

    public int getDegrees() {
        return degrees;
    }
}
