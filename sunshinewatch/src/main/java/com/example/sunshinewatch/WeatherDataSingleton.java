package com.example.sunshinewatch;

import android.graphics.Bitmap;

/**
 * Created by Admin on 05-10-2016.
 */

public class WeatherDataSingleton {
    public String high;
    public String low;
    public int weatherId;
    public Bitmap weatherIcon;
    private static WeatherDataSingleton obj;

    public static WeatherDataSingleton getInstance() {
        if(obj == null) {
            obj = new WeatherDataSingleton();
        }
        return obj;
    }

    public static void changeValues(String sHigh, String sLow, int icon, Bitmap weatherImage) {
        if(obj == null) {
            getInstance();
        }
        obj.high = sHigh;
        obj.low = sLow;
        obj.weatherId = icon;
        obj.weatherIcon = weatherImage;
    }

    private WeatherDataSingleton() {
        high = "no";
        low = "data";
        weatherId = -1;
    }
}
