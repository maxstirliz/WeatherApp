package lymansky.artem.weatherapp.utils;

import lymansky.artem.weatherapp.R;

public class IconUtils {

    //bright
    private static final String CLEAR_DAY = "01d";
    private static final String CLEAR_NIGHT = "01n";
    //cloudy
    private static final String CLOUDY_DAY = "02d";
    private static final String CLOUDY_NIGHT = "02n";
    private static final String SCAT_CLOUDS_DAY = "03d";
    private static final String SCAT_CLOUDS_NIGHT = "03n";
    private static final String BROKEN_CLOUDS_DAY = "04d";
    private static final String BROKEN_CLOUDS_NIGHT = "04n";
    private static final String SNOW_DAY = "13d";
    private static final String SNOW_NIGHT = "13n";
    private static final String MIST_DAY = "50d";
    private static final String MIST_NIGHT = "50n";
    //shower
    private static final String SHOWER_DAY = "09d";
    private static final String SHOWER_NIGHT = "09n";
    //rain
    private static final String RAIN_DAY = "10d";
    private static final String RAIN_NIGHT = "10n";
    //thunder
    private static final String THUNDER_DAY = "11d";
    private static final String THUNDER_NIGHT = "11n";

    //wind directions
    //north is default
    //east
    private static final int EAST_START = 46;
    private static final int EAST_END = 135;
    //south
    private static final int SOUTH_END = 225;
    //west
    private static final int WEST_END = 315;


    public static int getIconResource(String icon, boolean isSelected) {
        int iconResource;

        switch (icon) {
            case CLEAR_DAY:
                iconResource = isSelected ? R.drawable.ic_blue_day_bright : R.drawable.ic_black_day_bright;
                break;
            case CLEAR_NIGHT:
                iconResource = isSelected ? R.drawable.ic_blue_night_bright : R.drawable.ic_black_night_bright;
                break;
            case CLOUDY_DAY:
            case SCAT_CLOUDS_DAY:
            case BROKEN_CLOUDS_DAY:
            case SNOW_DAY:
            case MIST_DAY:
                iconResource = isSelected ? R.drawable.ic_blue_day_cloudy : R.drawable.ic_black_day_cloudy;
                break;
            case MIST_NIGHT:
            case CLOUDY_NIGHT:
            case SCAT_CLOUDS_NIGHT:
            case BROKEN_CLOUDS_NIGHT:
            case SNOW_NIGHT:
                iconResource = isSelected ? R.drawable.ic_blue_night_cloudy : R.drawable.ic_black_night_cloudy;
                break;
            case SHOWER_DAY:
                iconResource = isSelected ? R.drawable.ic_blue_day_shower : R.drawable.ic_black_day_shower;
                break;
            case SHOWER_NIGHT:
                iconResource = isSelected ? R.drawable.ic_blue_night_shower : R.drawable.ic_black_night_shower;
                break;
            case RAIN_DAY:
                iconResource = isSelected ? R.drawable.ic_blue_day_rain : R.drawable.ic_black_day_rain;
                break;
            case RAIN_NIGHT:
                iconResource = isSelected ? R.drawable.ic_blue_night_rain : R.drawable.ic_black_night_rain;
                break;
            case THUNDER_DAY:
                iconResource = isSelected ? R.drawable.ic_blue_day_thunder : R.drawable.ic_black_day_thunder;
                break;
            case THUNDER_NIGHT:
                iconResource = isSelected ? R.drawable.ic_blue_night_rain : R.drawable.ic_black_night_rain;
                break;
            default:
                iconResource = isSelected ? R.drawable.ic_blue_day_cloudy : R.drawable.ic_black_day_cloudy;
                break;
        }
        return iconResource;
    }

    public static int getIconResource(String icon) {
        int iconResource;

        switch (icon) {
            case CLEAR_DAY:
                iconResource = R.drawable.ic_white_day_bright;
                break;
            case CLEAR_NIGHT:
                iconResource = R.drawable.ic_white_night_bright;
                break;
            case CLOUDY_DAY:
            case SCAT_CLOUDS_DAY:
            case BROKEN_CLOUDS_DAY:
            case SNOW_DAY:
            case MIST_DAY:
                iconResource = R.drawable.ic_white_day_cloudy;
                break;
            case MIST_NIGHT:
            case CLOUDY_NIGHT:
            case SCAT_CLOUDS_NIGHT:
            case BROKEN_CLOUDS_NIGHT:
            case SNOW_NIGHT:
                iconResource = R.drawable.ic_white_night_cloudy;
                break;
            case SHOWER_DAY:
                iconResource = R.drawable.ic_white_day_shower;
                break;
            case SHOWER_NIGHT:
                iconResource = R.drawable.ic_white_night_shower;
                break;
            case RAIN_DAY:
                iconResource = R.drawable.ic_white_day_rain;
                break;
            case RAIN_NIGHT:
                iconResource = R.drawable.ic_white_night_rain;
                break;
            case THUNDER_DAY:
                iconResource = R.drawable.ic_white_day_thunder;
                break;
            case THUNDER_NIGHT:
                iconResource = R.drawable.ic_white_night_rain;
                break;
            default:
                iconResource = R.drawable.ic_white_day_cloudy;
                break;
        }
        return iconResource;
    }

    public static int getWindDirectionResource(int direction) {
        int resource;
        if (direction >= EAST_START && direction <= EAST_END) {
            resource = R.drawable.ic_arrow_right;
        } else if (direction > EAST_END && direction <= SOUTH_END) {
            resource = R.drawable.ic_arrow_down;
        } else if (direction > SOUTH_END && direction <= WEST_END) {
            resource = R.drawable.ic_arrow_left;
        } else {
            resource = R.drawable.ic_arrow_up;
        }
        return resource;
    }
}
