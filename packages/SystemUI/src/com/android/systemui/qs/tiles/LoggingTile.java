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

import com.testlib.Test;

/** Quick settings tile: Logging **/
public class LoggingTile extends QSTileImpl<BooleanState> {

    public static final String TAG = "LoggingTile";
    public static final String LOGGING_SETTING = Settings.System.LOGGING_MODE;

    public LoggingTile(QSHost host) {
        super(host);
    }

    @Override
    public BooleanState newTileState() {
        return new BooleanState();
    }

    @Override
    protected void handleClick() {
        Test test = new Test();
        Toast.makeText(mContext , test.getTestString(), Toast.LENGTH_SHORT).show();
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

}
