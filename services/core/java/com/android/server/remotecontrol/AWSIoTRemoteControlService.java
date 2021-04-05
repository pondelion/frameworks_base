package com.android.server.remotecontrol;

import android.content.Context;
import android.util.Log;

import com.android.server.SystemService;

public final class AWSIoTRemoteControlService extends SystemService
        implements IRemoteControlService {
    public static final String TAG = "AWSIoTRemoteControlService";

    public AWSIoTRemoteControlService(Context context) {
        super(context);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
    }

    @Override
    public void startRemoteControlService() {

    }

    @Override
    public void stopRemoteControlService() {

    }
}
