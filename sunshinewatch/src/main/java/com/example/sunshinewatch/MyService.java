package com.example.sunshinewatch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.InputStream;

public class MyService extends WearableListenerService {

    public static final String TEMP_HIGH = "temp_high";
    public static final String TEMP_LOW = "temp_low";
    public static final String WEATHER_ID = "weather_id";
    public static final String WEATHER_ICON = "weather_icon";
    public static final String _TAG = "watch_data_changed";

    public static final String WEATHER_TAG = "/forecast";

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
                if(item.getUri().getPath().compareTo(WEATHER_TAG) == 0) {
                    DataMap map = DataMapItem.fromDataItem(item).getDataMap();
                    String high = map.getString(TEMP_HIGH);
                    String low = map.getString(TEMP_LOW);
                    byte[] icon = map.getByteArray(WEATHER_ICON);
                    Bitmap weatherImage = BitmapFactory.decodeByteArray(icon, 0, icon.length);
                    int weatherId = map.getInt(WEATHER_ID);
                    WeatherDataSingleton.changeValues(high, low, weatherId, weatherImage);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
