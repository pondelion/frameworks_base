package com.android.systemui.qs.tiles;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.Context;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;
import android.os.AsyncTask;

import com.android.systemui.R;
import com.android.systemui.qs.QSHost;
import com.android.systemui.plugins.qs.QSTile.BooleanState;
import com.android.systemui.qs.tileimpl.QSTileImpl;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;

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
        Toast.makeText(mContext, "Tapped", Toast.LENGTH_SHORT).show();

        final String IDENTITY_POOL_ID = "YOUR_POOL_ID";

        try {
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                mContext,
                IDENTITY_POOL_ID,
                Regions.US_EAST_1
            );

            LambdaInvokerFactory factory = new LambdaInvokerFactory(
                mContext,
                Regions.US_EAST_1,
                credentialsProvider
            );

            MyInterface myInterface = factory.build(MyInterface.class);

            NameInfo nameInfo = new NameInfo("John", "Doe");

            new LambdaAsyncTask(mContext, myInterface).execute(nameInfo);
        } catch (final Exception e) {
            Toast.makeText(mContext, "Failed to execute AWS Lambda", Toast.LENGTH_SHORT).show();
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
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


class NameInfo {
    private String firstName;
    private String lastName;

    public NameInfo() {}

    public NameInfo(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}


interface MyInterface {

    @LambdaFunction
    String echo(NameInfo nameInfo);

    @LambdaFunction(functionName = "echo")
    void noEcho(NameInfo nameInfo);
}


class LambdaAsyncTask extends AsyncTask<NameInfo, Void, String> {

    private Context mContext;
    private MyInterface mInterface;

    public LambdaAsyncTask(Context context, MyInterface myInterface) {
        this.mContext = context;
        this.mInterface = myInterface;
    }

    @Override
    protected String doInBackground(NameInfo... params) {
        Log.d(LoggingTile.TAG, "doInBackground");
        try {
            return mInterface.echo(params[0]);
        } catch (LambdaFunctionException lfe) {
            Log.d(LoggingTile.TAG, "Failed to invoke echo", lfe);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (result == null) {
            Log.d(LoggingTile.TAG, "result is null");
            return;
        }

        Log.d(LoggingTile.TAG, result);
    }
}
