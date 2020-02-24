package com.android.systemui.qs.tiles;

import android.app.ActivityManager;
import android.content.Intent;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import com.android.systemui.R;
import com.android.systemui.qs.QSHost;
import com.android.systemui.plugins.qs.QSTile.BooleanState;
import com.android.systemui.qs.tileimpl.QSTileImpl;

import java.io.IOException;

/** Quick settings tile: Logging **/
public class LoggingTile extends QSTileImpl<BooleanState> {

    public static final String TAG = "LoggingTile";
    public static final String LOGGING_SETTING = Settings.System.LOGGING_MODE;
    private Process mTCPDumpProc;

    public LoggingTile(QSHost host) {
        super(host);
    }

    @Override
    public BooleanState newTileState() {
        return new BooleanState();
    }

    @Override
    protected void handleClick() {
        try {
            boolean isEnabled = isLoggingModeEnabled();
            Settings.System.putIntForUser(mContext.getContentResolver(), LOGGING_SETTING,
                    isEnabled ? 0 : 1, ActivityManager.getCurrentUser());
        } catch (final SettingNotFoundException e) {
            Log.w(TAG, "Settings [" + LOGGING_SETTING + "] not found.");
        }
        refreshState();
    }

    @Override
    public CharSequence getTileLabel() {
        return mContext.getString(R.string.quick_settings_logging_label);
    }

    @Override
    protected void handleUpdateState(BooleanState state, Object arg) {

        if (state.value) {
            if (mTCPDumpProc == null) {
                mTCPDumpProc = startTCPDump();
                Log.d(TAG, "started tcpdump");
                Toast.makeText(mContext, "started tcpdump", Toast.LENGTH_LONG).show();
            }
        } else {
            if (mTCPDumpProc != null) {
                mTCPDumpProc.destroy();
                mTCPDumpProc = null;
                Toast.makeText(mContext, "stopped tcpdump", Toast.LENGTH_LONG).show();
            }
        }

        try {
            boolean isEnabled = isLoggingModeEnabled();
            final Drawable mEnable = mContext.getDrawable(R.drawable.sun);
            final Drawable mDisable = mContext.getDrawable(R.drawable.sun2);
            state.value = isEnabled;
            state.icon = new DrawableIcon(state.value ? mEnable : mDisable);
        } catch (final SettingNotFoundException e) {
            Log.w(TAG, "Settings [" + LOGGING_SETTING + "] not found.");
        }
        state.label = "logging mode";
        state.contentDescription = state.label;
    }

    @Override
    public void handleSetListening(boolean listening) {

    }

    @Override
    public Intent getLongClickIntent() {
        return new Intent("action");
    }

    @Override
    public int getMetricsCategory() {
        return 0;
    }

    private boolean isLoggingModeEnabled() throws SettingNotFoundException {
        return Settings.System.getIntForUser(mContext.getContentResolver(), LOGGING_SETTING,
                ActivityManager.getCurrentUser()) == 1 ? true : false;
    }

    private Process startTCPDump() {
        // String cmd = "tcpdump -s 0 -i wlan0 -v -w /sdcard/tcpdump.pcap";
        String[] cmd = new String[]{"tcpdump", "-s", "0", "-i", "wlan0", "-v", "-w", "/sdcard/tcpdump.pcap"};
        Process p = null;
        try {
            // MEMO:
            // 02-24 09:11:54.480  1785  1785 I tcpdump : type=1400 audit(0.0:46): avc: denied { create } for scontext=u:r:platform_app:s0:c512,c768 tcontext=u:r:platform_app:s0:c512,c768 tclass=socket permissive=1
            // 02-24 09:11:54.483  1785  1785 I tcpdump : type=1400 audit(0.0:47): avc: denied { ioctl } for path="socket:[23508]" dev="sockfs" ino=23508 ioctlcmd=8946 scontext=u:r:platform_app:s0:c512,c768 tcontext=u:r:platform_app:s0:c512,c768 tclass=socket permissive=1
            // Disabling SELinux by 'adb shell setenforce 0' seems does not work.
            p = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            Log.e(TAG, "Error in Runtime.getRuntime().exec(cmd)", e);
        }
        return p;
    }

}
