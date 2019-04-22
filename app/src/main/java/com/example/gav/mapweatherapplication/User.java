package com.example.gav.mapweatherapplication;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.gav.mapweatherapplication.utils.Constants;

public class User {

    final public static String LAST_LATITUDE = "LAST_LATITUDE";
    final public static String LAST_LONGITUDE = "LAST_LONGITUDE";

    private double lastLatitude;
    private double lastLongitude;

    public void save() {
        final SharedPreferences.Editor storage = PreferenceManager.getDefaultSharedPreferences(App.getContext()).edit();
        storage.putString(LAST_LATITUDE, ""+lastLatitude);
        storage.putString(LAST_LONGITUDE, ""+lastLongitude);
        storage.apply();
    }

    public void load() {
        final SharedPreferences storage = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        lastLatitude = Double.valueOf(storage.getString(LAST_LATITUDE, Double.toString(Constants.BASE_LATITUDE)));
        lastLongitude = Double.valueOf(storage.getString(LAST_LONGITUDE, Double.toString(Constants.BASE_LONGITUDE)));
    }

    public double getLastLatitude() {
        return lastLatitude;
    }

    public void setLastLatitude(double lastLatitude) {
        this.lastLatitude = lastLatitude;
    }

    public double getLastLongitude() {
        return lastLongitude;
    }

    public void setLastLongitude(double lastLongitude) {
        this.lastLongitude = lastLongitude;
    }
}
