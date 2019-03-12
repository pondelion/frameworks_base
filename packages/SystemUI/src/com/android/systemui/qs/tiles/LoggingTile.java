package com.android.systemui.qs.tiles;

import android.app.ActivityManager;
import android.provider.Settings.System;

import com.android.systemui.qs.QSHost;
import com.android.systemui.plugins.qs.QSTile.BooleanState;
import com.android.systemui.qs.tileimpl.QSTileImpl;

/** Quick settings tile: Logging **/
public class LoggingTile extends QSTileImpl<BooleanState> {

    public static final String LOGGING_SETTING = System.LOGGING_MODE;

    public LoggingTile(QSHost host) {
        super(host);
    }

    @Override
    public BooleanState newTileState() {
        return new BooleanState();
    }

    @Override
    protected void handleClick() {
        
    }

    @Override
    public CharSequence getTitleLabel() {
        return mContext.getString(R.string_quick_settings_logging_label);
    }

    @Override
    protected void handleUpdateState(BooleanState state, Object arg) {
        Settings.System.putIntForUser(mContext.getContentResolver(), LOGGING_SETTING,
                state.value ? Settings.System.LOGGING_MODE_ON : Settings.System.LOGGING_MODE_OFF,
                ActivityManager.getCurrentUser());
    }

}
