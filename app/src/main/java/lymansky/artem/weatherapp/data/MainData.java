package lymansky.artem.weatherapp.data;

import com.google.gson.annotations.SerializedName;

class MainData {

    @SerializedName("temp")
    int tempAverage;

    @SerializedName("pressure")
    int pressure; //in hPa

    @SerializedName("humidity")
    int humidity;

    @SerializedName("temp_min")
    int tempMin;

    @SerializedName("temp_max")
    int tempMax;

    public int getTempAverage() {
        return tempAverage;
    }

    public int getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getTempMin() {
        return tempMin;
    }

    public int getTempMax() {
        return tempMax;
    }
}
