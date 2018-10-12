package lymansky.artem.weatherapp.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "weather")
public class WeatherEntry {

    @PrimaryKey
    @ColumnInfo(name = "time")
    private long time;
    @ColumnInfo(name = "day_number")
    private int dayNumber;
    @ColumnInfo(name = "pic")
    private String pic;
    @ColumnInfo(name = "temp")
    private int temp;
    @ColumnInfo(name = "temp_max")
    private int tempMax;
    @ColumnInfo(name = "temp_min")
    private int tempMin;
    @ColumnInfo(name = "humidity")
    private int humidity;
    @ColumnInfo(name = "wind")
    private int wind;
    @ColumnInfo(name = "wind_direction")
    private int windDirection;

    @Ignore
    public WeatherEntry() {}

    public WeatherEntry(long time, int dayNumber, String pic, int temp, int tempMax, int tempMin, int humidity, int wind, int windDirection) {
        this.time = time;
        this.dayNumber = dayNumber;
        this.pic = pic;
        this.temp = temp;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.humidity = humidity;
        this.wind = wind;
        this.windDirection = windDirection;
    }


    //Getters and Setters
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getTempMax() {
        return tempMax;
    }

    public void setTempMax(int tempMax) {
        this.tempMax = tempMax;
    }

    public int getTempMin() {
        return tempMin;
    }

    public void setTempMin(int tempMin) {
        this.tempMin = tempMin;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getWind() {
        return wind;
    }

    public void setWind(int wind) {
        this.wind = wind;
    }

    public int getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(int windDirection) {
        this.windDirection = windDirection;
    }
}
