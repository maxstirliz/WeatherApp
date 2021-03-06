package lymansky.artem.weatherapp.networking;

import lymansky.artem.weatherapp.data.WeatherData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {

    @GET("/data/2.5/forecast")
    Call<WeatherData> getWeatherByCityName(@Query("q") String cityName,
                                                 @Query("units") String unit,
                                                 @Query("APPID") String key);

    @GET("/data/2.5/forecast")
    Call<WeatherData> getWeatherByCityId(@Query("id") int cityId,
                                               @Query("units") String unit,
                                               @Query("APPID") String key);

    @GET("/data/2.5/forecast")
    Call<WeatherData> getWeatherByZipCode(@Query("zip") int zipCode,
                                                @Query("units") String unit,
                                                @Query("APPID") String key);

    @GET("/data/2.5/forecast")
    Call<WeatherData> getWeatherByCoord(@Query("lat") float lat,
                                              @Query("lon") float lon,
                                              @Query("units") String unit,
                                              @Query("APPID") String key);


}
