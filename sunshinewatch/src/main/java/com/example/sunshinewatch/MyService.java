package com.example.sunshinewatch;

import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

public class MyService extends WearableListenerService {

    public static final String TEMP_HIGH = "temp_high";
    public static final String TEMP_LOW = "temp_low";
    public static final String _TAG = "watch_data_changed";

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(_TAG, "wearable listener");
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        WeatherDataSingleton obj = WeatherDataSingleton.getInstance();
        //obj.high = "sai";
        //obj.low = "ram";
        Log.e(_TAG, "data changed");
        for(DataEvent dataEvent : dataEventBuffer) {
            if(dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                DataItem item = dataEvent.getDataItem();
                if(item.getUri().getPath().compareTo("/forecast") == 0) {
                    DataMap map = DataMapItem.fromDataItem(item).getDataMap();
                    String high = map.getString(TEMP_HIGH);
                    String low = map.getString(TEMP_LOW);
                    //obj.high = high;
                    //obj.low = low;
                    WeatherDataSingleton.changeValues(high, low, -1);
                    obj = WeatherDataSingleton.getInstance();
                    Log.e(_TAG, "high : " + obj.high);
                    Log.e(_TAG, "low : " + obj.low);
                }
            }
        }
    }

    /**
     * Helper method to provide the icon resource id according to the weather condition id returned
     * by the OpenWeatherMap call.
     * @param weatherId from OpenWeatherMap API response
     * @return resource id for the corresponding icon. -1 if no relation is found.
     */
    private int getIconResourceForWeatherCondition(int weatherId) {
        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.ic_storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.ic_light_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.ic_rain;
        } else if (weatherId == 511) {
            return R.drawable.ic_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.ic_rain;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.ic_snow;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.ic_fog;
        } else if (weatherId == 761 || weatherId == 781) {
            return R.drawable.ic_storm;
        } else if (weatherId == 800) {
            return R.drawable.ic_clear;
        } else if (weatherId == 801) {
            return R.drawable.ic_light_clouds;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.ic_cloudy;
        }
        return -1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
