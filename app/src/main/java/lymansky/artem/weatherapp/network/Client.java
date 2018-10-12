package lymansky.artem.weatherapp.network;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    private static final String BASE_URL = "https://api.openweathermap.org";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    public static <S> S getService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
