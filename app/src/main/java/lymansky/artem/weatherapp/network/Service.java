package lymansky.artem.weatherapp.network;

import java.util.List;

import lymansky.artem.weatherapp.data.WeatherData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {

    @GET("/data/2.5/forecast")
    Call<List<WeatherData>> getWeatherByCityName(@Query("q") String cityName,
                                                 @Query("units") String unit,
                                                 @Query("lang") String lang,
                                                 @Query("APPID") String key);

    @GET("/data/2.5/forecast")
    Call<List<WeatherData>> getWeatherByCityId(@Query("id") int cityId,
                                               @Query("units") String unit,
                                               @Query("lang") String lang,
                                               @Query("cnt") int count,
                                               @Query("APPID") String key);

    @GET("/data/2.5/forecast")
    Call<List<WeatherData>> getWeatherByZipCode(@Query("zip") int zipCode,
                                                @Query("units") String unit,
                                                @Query("lang") String lang,
                                                @Query("cnt") int count,
                                                @Query("APPID") String key);

    @GET("/data/2.5/forecast")
    Call<List<WeatherData>> getWeatherByCoord(@Query("lat") long lat,
                                              @Query("lon") long lon,
                                              @Query("units") String unit,
                                              @Query("lang") String lang,
                                              @Query("cnt") int count,
                                              @Query("APPID") String key);


}
