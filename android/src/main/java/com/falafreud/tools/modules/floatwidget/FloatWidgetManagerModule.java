package com.falafreud.tools.modules.floatwidget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.falafreud.tools.modules.floatwidget.icon.FloatIconService;
import com.falafreud.tools.modules.NotificationFilterService;

/**
 * Created by Haroldo Shigueaki Teruya on 18/07/18.
 */
public class FloatWidgetManagerModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

    // GLOBAL VARIABLES ============================================================================
    // =============================================================================================

    private final ReactApplicationContext reactContext;
    private BroadcastReceiver broadcastReceiver = null;
    private boolean isBackground = false;
    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2048;
    private static final String SHOW_FLOAT_WIDGET_WHEN_APPLICATION_INACTIVE = "FLOAT_WIDGET";
    private static final String TAG = "FloatWidget";

    // METHODS =====================================================================================
    // =============================================================================================

    public FloatWidgetManagerModule(ReactApplicationContext reactContext) {

        super(reactContext);

        this.reactContext = reactContext;
        this.getReactApplicationContext().addLifecycleEventListener(this);
    }

    @Override
    public String getName() {

        return "FloatWidgetManagerModule";
    }

    /**
     * This method is called by {@link NotificationFilterService}.
     * This method is called when the application receive an notification.
     */
    private void onUnreadMessageReceived() {

        if (this.isBackground) {
            Intent intent = new Intent(this.reactContext, FloatIconService.class);
            intent.putExtra(Constant.ON_UNREAD_MESSAGE_RECEIVED, 1);
            this.reactContext.startService(intent);
        }
    }

    /**
     * this line invoke the FloatIconService "onDestroy".
     */
    private void stopService() {

        try {
            if (this.broadcastReceiver != null) {
                this.reactContext.unregisterReceiver(this.broadcastReceiver);
            }
        } catch (IllegalArgumentException e) {
            Log.d(TAG, getName() + " stopService: " + e.toString());
            e.printStackTrace();
        } catch (Exception e) {
            Log.d(TAG, getName() + " stopService: " + e.toString());
            e.printStackTrace();
        }
        this.reactContext.stopService(new Intent(this.reactContext, FloatIconService.class));
    }

    private void askPermission() {

        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + this.reactContext.getPackageName()));
        getCurrentActivity().startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
    }

    @ReactMethod
    public void handleStartService(int count) {

        Log.d(TAG, getName() + " handleStartService: " + count);

        if (this.isToShowWhenApplicationInactive()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                this.startService(count);
            } else if (Settings.canDrawOverlays(this.reactContext)) {
                this.startService(count);
            } else {
                askPermission();
            }
        }
    }

    /**
     * This method is called from "handleStartService".
     */
    private void startService(int count) {

        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constant.ACTION);
            this.broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent != null) {
                        FloatWidgetManagerModule.this.onUnreadMessageReceived();
                    }
                }
            };
            Intent intent = new Intent(this.reactContext, FloatIconService.class);
            intent.putExtra(Constant.ON_UNREAD_MESSAGE_RECEIVED, count);
            this.reactContext.registerReceiver(this.broadcastReceiver, intentFilter);
            this.reactContext.startService(intent);
            this.isBackground = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void handleStopService() {
        this.stopService();
        this.isBackground = false;
    }

    @Override
    public void onHostResume() {
    }

    @Override
    public void onHostPause() {
    }

    @Override
    public void onHostDestroy() {

        Log.d(TAG, getName() + " onHostDestroy");
        this.stopService();
    }

    @ReactMethod
    public void showWhenApplicationInactive(boolean enable) {

        Log.d(TAG, getName() + " showWhenApplicationInactive: " + enable);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getReactApplicationContext().getBaseContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHOW_FLOAT_WIDGET_WHEN_APPLICATION_INACTIVE, enable);
        editor.apply();
    }

    @ReactMethod
    public Boolean isToShowWhenApplicationInactive() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getReactApplicationContext().getBaseContext());
        return preferences.getBoolean(SHOW_FLOAT_WIDGET_WHEN_APPLICATION_INACTIVE, false);
    }

    // CLASS =======================================================================================
    // =============================================================================================

    public static final class Constant {

        public static final String FalaFreud = "com.falafreud.falafreud";

        // used to read the pushnotification  payload
        public static final String DATA = "data";
        public static final String TYPE = "type";
        public static final String NEW_MESSAGE = "new_message";

        // used for BroadcastReceiver
        public static final String ACTION = "com.falafreud.module.floatwidget.unread_message_received";

        // used to send data to the FloatIconService
        public static final String ON_UNREAD_MESSAGE_RECEIVED = "on_unread_message_received";
    }
}
