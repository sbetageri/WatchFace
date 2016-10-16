package com.example.android.sunshine.app.sync;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.sunshine.app.Utility;
import com.example.android.sunshine.app.data.WeatherContract;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;

/**
 * Created by Admin on 07-10-2016.
 */

public class WatchUtility {

    // Tags to sync with watchface
    public static final String TEMP_HIGH = "temp_high";
    public static final String TEMP_LOW = "temp_low";
    public static final String WEATHER_ICON_TAG = "weather_icon";
    public static final String WEATHER_ID = "weather_id";

    // these indices must match the projection
    private static final int INDEX_WEATHER_ID = 0;
    private static final int INDEX_MAX_TEMP = 1;
    private static final int INDEX_MIN_TEMP = 2;

    private static final String LOG_TAG = "watch_utility";

    private static final String[] NOTIFY_WEATHER_PROJECTION = new String[]{
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC
    };

    private static final String WEATHER_TAG = "/forecast";

    public static void sendDataToWatch(Context context,
                                       GoogleApiClient client) {
        String locationQuery = Utility.getPreferredLocation(context);

        Uri weatherUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(locationQuery, System.currentTimeMillis());

        // we'll query our contentProvider, as always
        Cursor cursor = context.getContentResolver().query(weatherUri, NOTIFY_WEATHER_PROJECTION, null, null, null);

        if (cursor.moveToFirst()) {
            int weatherId = cursor.getInt(INDEX_WEATHER_ID);
            double high = cursor.getDouble(INDEX_MAX_TEMP);
            double low = cursor.getDouble(INDEX_MIN_TEMP);

            // put high and low in data item
            String strHigh = Utility.formatTemperature(context, high);
            String strLow = Utility.formatTemperature(context, low);

            client.connect();

            PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(WEATHER_TAG);
            putDataMapRequest.getDataMap().putString(TEMP_HIGH, strHigh);
            putDataMapRequest.getDataMap().putString(TEMP_LOW, strLow);
            putDataMapRequest.getDataMap().putInt(WEATHER_ID, weatherId);

            int weatherIconId = Utility.getIconResourceForWeatherCondition(weatherId);


            Bitmap iconBitmap = BitmapFactory.decodeResource(context.getResources(), weatherIconId);
            byte[] weatherIcon = createByteArrayFromBitmap(iconBitmap);

            putDataMapRequest.getDataMap().putByteArray(WEATHER_ICON_TAG, weatherIcon);

            String milli = Long.toString(System.currentTimeMillis());
            putDataMapRequest.getDataMap().putString("time", milli);
            PutDataRequest putDataRequest = putDataMapRequest.asPutDataRequest().setUrgent();
            Wearable.DataApi.putDataItem(client, putDataRequest)
                    .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                        @Override
                        public void onResult(@NonNull DataApi.DataItemResult dataItemResult) {
                            if (dataItemResult.getStatus().isSuccess()) {
                                Log.e(LOG_TAG, "successfully sent data to watch");
                            } else {
                                Log.e(LOG_TAG, "failure to send data to watch");
                            }
                        }
                    });
        }
    }

    private static byte[] createByteArrayFromBitmap(Bitmap bitmap) {
        Log.e(LOG_TAG, "creating asset");
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        Log.e(LOG_TAG, "length : " + Integer.toString(byteStream.toByteArray().length));
        return byteStream.toByteArray();
    }

}
