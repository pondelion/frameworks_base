package com.android.server;

import android.content.Context;
import android.util.Log;


public final class TestService extends SystemService {

    public static final String TAG = "TestService";

    public TestService(Context context) {
        super(context);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        Log.d(TAG, "hogehoge");
    }
}
