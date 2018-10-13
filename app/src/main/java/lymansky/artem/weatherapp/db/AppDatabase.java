package lymansky.artem.weatherapp.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {WeatherEntry.class}, version = 1)

public abstract class AppDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DB_NAME = "weatherdb";
    private static volatile AppDatabase sInstance;

    public static AppDatabase getInstance(final Context context) {
        if(sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DB_NAME)
                        .build();
            }
        }
        return sInstance;
    }
    public abstract WeatherDao weatherDao();
}
