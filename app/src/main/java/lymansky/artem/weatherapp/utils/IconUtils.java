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
                iconResource = isSelected ? R.drawable.ic_01d_blue : R.drawable.ic_01d_black;
                break;
            case CLEAR_NIGHT:
                iconResource = isSelected ? R.drawable.ic_01n_blue : R.drawable.ic_01n_black;
                break;
            case CLOUDY_DAY:
                iconResource = isSelected ? R.drawable.ic_02d_blue : R.drawable.ic_02d_black;
                break;
            case SCAT_CLOUDS_DAY:
            case SCAT_CLOUDS_NIGHT:
                iconResource = isSelected ? R.drawable.ic_03dn_blue : R.drawable.ic_03dn_black;
                break;
            case BROKEN_CLOUDS_DAY:
            case BROKEN_CLOUDS_NIGHT:
                iconResource = isSelected ? R.drawable.ic_04dn_blue : R.drawable.ic_04dn_black;
                break;
            case SNOW_DAY:
            case SNOW_NIGHT:
                iconResource = isSelected ? R.drawable.ic_13dn_blue : R.drawable.ic_13dn_black;
                break;
            case MIST_DAY:
            case MIST_NIGHT:
                iconResource = isSelected ? R.drawable.ic_50dn_blue : R.drawable.ic_50dn_black;
                break;
            case CLOUDY_NIGHT:
                iconResource = isSelected ? R.drawable.ic_02n_blue : R.drawable.ic_02n_black;
                break;
            case SHOWER_DAY:
            case SHOWER_NIGHT:
                iconResource = isSelected ? R.drawable.ic_09dn_blue : R.drawable.ic_09dn_black;
                break;
            case RAIN_DAY:
            case RAIN_NIGHT:
                iconResource = isSelected ? R.drawable.ic_10dn_blue : R.drawable.ic_10dn_black;
                break;
            case THUNDER_DAY:
            case THUNDER_NIGHT:
                iconResource = isSelected ? R.drawable.ic_11dn_blue : R.drawable.ic_11dn_black;
                break;
            default:
                iconResource = isSelected ? R.drawable.ic_02d_blue : R.drawable.ic_02d_black;
                break;
        }
        return iconResource;
    }

    public static int getIconResource(String icon) {
        int iconResource;

        switch (icon) {
            case CLEAR_DAY:
                iconResource = R.drawable.ic_01d;
                break;
            case CLEAR_NIGHT:
                iconResource = R.drawable.ic_01n;
                break;
            case CLOUDY_DAY:
                iconResource = R.drawable.ic_02d;
                break;
            case SCAT_CLOUDS_DAY:
            case SCAT_CLOUDS_NIGHT:
                iconResource = R.drawable.ic_03dn;
                break;
            case BROKEN_CLOUDS_DAY:
            case BROKEN_CLOUDS_NIGHT:
                iconResource = R.drawable.ic_04dn;
                break;
            case SNOW_DAY:
            case SNOW_NIGHT:
                iconResource = R.drawable.ic_13dn;
                break;
            case MIST_DAY:
            case MIST_NIGHT:
                iconResource = R.drawable.ic_50dn;
                break;
            case CLOUDY_NIGHT:
                iconResource = R.drawable.ic_02n;
                break;
            case SHOWER_DAY:
            case SHOWER_NIGHT:
                iconResource = R.drawable.ic_09dn;
                break;
            case RAIN_DAY:
            case RAIN_NIGHT:
                iconResource = R.drawable.ic_10dn;
                break;
            case THUNDER_DAY:
            case THUNDER_NIGHT:
                iconResource = R.drawable.ic_11dn;
                break;
            default:
                iconResource = R.drawable.ic_02d;
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
