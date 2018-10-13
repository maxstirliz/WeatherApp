package lymansky.artem.weatherapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {
    private static final long MILLISECONDS_INDEX = 1000L;

    private static final String FULL_DATE_FORMAT = "EE, dd MMMM";
    private static final String WEEK_DAY_FORMAT = "EE";
    private static final String HOUR_FORMAT = "HH";
    private static final String DAY_FORMAT = "dd";

    public static long convertToMilliseconds(long utcTime) {
        return utcTime * MILLISECONDS_INDEX;
    }

    public static String getFullDateFormat(long time) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat(FULL_DATE_FORMAT, Locale.getDefault());
        return format.format(date);
    }

    public static String getWeekDayFormat(long time) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat(WEEK_DAY_FORMAT, Locale.getDefault());
        return format.format(date);
    }

    public static String getHourString(long time) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat(HOUR_FORMAT, Locale.getDefault());
        return format.format(date);
    }

    public static int getHourInt(long time) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat(HOUR_FORMAT, Locale.getDefault());
        return Integer.parseInt(format.format(date));
    }

    public static int getDayNumber(long time) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat(DAY_FORMAT, Locale.getDefault());
        return Integer.parseInt(format.format(date));
    }

    public static int getCurrentDayNumber() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(DAY_FORMAT, Locale.getDefault());
        return Integer.parseInt(format.format(date));
    }
}
