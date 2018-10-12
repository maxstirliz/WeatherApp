package lymansky.artem.weatherapp.data;

import com.google.gson.annotations.SerializedName;

public class MainData {

    @SerializedName("temp")
    double tempAverage;

    @SerializedName("pressure")
    double pressure; //in hPa

    @SerializedName("humidity")
    double humidity;

    @SerializedName("temp_min")
    double tempMin;

    @SerializedName("temp_max")
    double tempMax;

    public double getTempAverage() {
        return tempAverage;
    }

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getTempMin() {
        return tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }
}
