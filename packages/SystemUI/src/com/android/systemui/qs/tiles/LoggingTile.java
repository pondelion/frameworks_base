package com.android.systemui.qs.tiles;

import android.app.ActivityManager;
import android.content.Intent;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.android.systemui.R;
import com.android.systemui.qs.QSHost;
import com.android.systemui.plugins.qs.QSTile.BooleanState;
import com.android.systemui.qs.tileimpl.QSTileImpl;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.regions.Region;
import com.amazonaws.services.dynamodbv2.*;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;

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

        try {
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                mContext,
                "YOUR_POOL_ID",
                Regions.US_EAST_1
            );

            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);

            final DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

            (new Thread(new Runnable() {
                public void run() {
                    AOSPTest record = new AOSPTest();
                    record.setKey1("test_string1");
                    record.setKey2("test_string2");
                    record.setKey3(21873);
                    record.setKey4(3.141592f);
                    mapper.save(record);
                }
            })).start();
        } catch (final Exception e) {
            Log.e(TAG, "Failed to save record to DynamoDB.", e);
        }

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


@DynamoDBTable(tableName = "AOSPTest")
class AOSPTest {

    private String mKey1;
    private String mKey2;
    private int mKey3;
    private float mKey4;

    @DynamoDBHashKey(attributeName = "Key1")
    public String getKey1() {return mKey1; }
    public void setKey1(String key) { this.mKey1 = key; }

    @DynamoDBRangeKey(attributeName = "Key2")
    public String getKey2() {return mKey2; }
    public void setKey2(String key) { this.mKey2 = key; }

    @DynamoDBAttribute(attributeName = "Key3")
    public int getKey3() {return mKey3; }
    public void setKey3(int key) { this.mKey3 = key; }

    @DynamoDBAttribute(attributeName = "Key4")
    public float getKey4() {return mKey4; }
    public void setKey4(float key) { this.mKey4 = key; }

}