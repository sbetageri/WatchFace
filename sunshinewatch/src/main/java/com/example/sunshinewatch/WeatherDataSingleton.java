package com.example.sunshinewatch;

/**
 * Created by Admin on 05-10-2016.
 */

public class WeatherDataSingleton {
    public String high;
    public String low;
    public int iconId;
    private static WeatherDataSingleton obj;

    public static WeatherDataSingleton getInstance() {
        if(obj == null) {
            obj = new WeatherDataSingleton();
        }
        return obj;
    }

    public static void changeValues(String sHigh, String sLow, int icon) {
        if(obj == null) {
            getInstance();
        }
        obj.high = sHigh;
        obj.low = sLow;
        obj.iconId = icon;
    }

    private WeatherDataSingleton() {
        high = "-39";
        low = "-40";
        iconId = -1;
    }
}
