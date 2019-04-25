package com.example.gav.mapweatherapplication.utils;

import android.content.Context;

import com.example.gav.mapweatherapplication.R;

public class WeatherDescriptionProvider {

    private static final String BROKEN_CLOUDS       = "broken clouds";
    private static final String SCATTERED_CLOUDS    = "scattered clouds";
    private static final String FEW_CLOUDS          = "few clouds";
    private static final String OVERCAST_CLOUDS     = "overcast clouds";
    private static final String CLEAR_SKY           = "clear sky";
    private static final String LIGHT_RAIN          = "light rain";
    private static final String MODERATE_RAIN       = "moderate rain";
    private static final String HEAVY_INTENSITY_RAIN= "heavy_intensity_rain";
    private static final String LIGHT_SNOW          = "light snow";
    private static final String SNOW                = "snow";

    public static String getTranslatedDescription(Context context, String defaultDescription) {
        String result;
        switch (defaultDescription) {
            case BROKEN_CLOUDS:
                result = context.getString(R.string.broken_clouds);
                break;
            case SCATTERED_CLOUDS:
                result = context.getString(R.string.scattered_clouds);
                break;
            case FEW_CLOUDS:
                result = context.getString(R.string.few_clouds);
                break;
            case OVERCAST_CLOUDS:
                result = context.getString(R.string.overcast_clouds);
                break;
            case CLEAR_SKY:
                result = context.getString(R.string.clear_sky);
                break;
            case LIGHT_RAIN:
                result = context.getString(R.string.light_rain);
                break;
            case LIGHT_SNOW:
                result = context.getString(R.string.light_rain);
                break;
            case MODERATE_RAIN:
                result = context.getString(R.string.moderate_rain);
                break;
            case HEAVY_INTENSITY_RAIN:
                result = context.getString(R.string.heavy_intensity_rain);
                break;
            case SNOW:
                result = context.getString(R.string.snow);
                break;
            default:
                result = defaultDescription;
                break;
        }
        return result;
    }
}
