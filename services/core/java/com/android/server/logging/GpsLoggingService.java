package com.android.server.logging;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.android.server.SystemService;


public final class GpsLoggingService extends SystemService
        implements LocationListener {

    public static final String TAG = "GpsLoggingService";
    private Context mContext;
    private LocationManager mLocationManager;
    private Location mLocation;
    private boolean mIsListening = false;

    public GpsLoggingService(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onBootPhase(int phase) {
        if (phase == PHASE_BOOT_COMPLETED) {
            mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            startListeningLocation();
            Log.i(TAG, "started listening GPS location");
        }
    }

    public void startListeningLocation() {
        if (mLocationManager == null || mIsListening) {
            return;
        }

        Log.d(TAG, "GPS_PROVIDER enabled : " + String.valueOf(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)));
        Log.d(TAG, "NETWORK_PROVIDER enabled : " + String.valueOf(mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)));

        mLocationManager.requestLocationUpdates(
            null,
            this,
            Looper.getMainLooper()
        );
        mIsListening = true;

        if (mLocationManager.getLastLocation() == null) {
            if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                mLocationManager.requestSingleUpdate(
                    LocationManager.NETWORK_PROVIDER,
                    this,
                    Looper.getMainLooper()
                );
            } else if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                mLocationManager.requestSingleUpdate(
                    LocationManager.GPS_PROVIDER,
                    this,
                    Looper.getMainLooper()
                );
            }
        }
    }

    public void stopListeningUpdate() {
        if (mLocationManager == null || !mIsListening) {
            return;
        }

        mLocationManager.removeUpdates(this);
        mIsListening = false;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            mLocation = location;
            Log.d(TAG, "onLocationChanged : "
                    + " provider =  " + location.getProvider()
                    + ", accuracy = " + location.getAccuracy()
                    + ", time = " + location.getTime());
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}