package com.falafreud.tools.modules;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.falafreud.tools.modules.call.CallManagerModule;
import com.falafreud.tools.modules.floatwidget.FloatWidgetManagerModule;
import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Haroldo Shigueaki Teruya on 20/07/18.
 */
public class NotificationFilterService extends NotificationExtenderService {

    // GLOBAL VARIABLES ============================================================================
    // =============================================================================================

    public static final String TAG = "NotificationService";

    // METHODS =====================================================================================
    // =============================================================================================

    /**
     * This method is called when the device receive an push notification using the OneSignal SDK.
     *
     * @param receivedResult
     * @return
     */
    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {

        JSONObject additionalData = receivedResult.payload.additionalData;

        Log.d(TAG, TAG + " onNotificationProcessing content: " + additionalData.toString());
        try {
            if (additionalData.has(FloatWidgetManagerModule.Constant.TYPE)) {
                Log.d(TAG, TAG + " onNotificationProcessing is new message");
                if (additionalData.getString(FloatWidgetManagerModule.Constant.TYPE).equals(FloatWidgetManagerModule.Constant.NEW_MESSAGE)) {
                    this.unreadMessageReceived();
                    return false;
                }
            } else {
                boolean hidden = this.incomingCallReceived(additionalData);
                return hidden;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, TAG + " onNotificationProcessing error: " + e.toString());
        }

        // Return true to stop the notification from displaying.
        return false;
    }

    /**
     * This method is responsible to start an ACTION.
     * The receiver is the FloatWidgetManagerModule instance.
     */
    private void unreadMessageReceived() {

        Intent intent = new Intent();
        intent.setAction(FloatWidgetManagerModule.Constant.ACTION);
        Log.d(TAG, TAG + " unreadMessageReceived");
        NotificationFilterService.this.sendBroadcast(intent);
    }

    private boolean incomingCallReceived(JSONObject additionalData) throws JSONException {

        boolean hidden = false;
        Log.d(TAG, TAG + " incomingCallReceived trying...");
        if (additionalData.has(CallManagerModule.Constant.SESSION_ID) &&
                additionalData.has(CallManagerModule.Constant.TARGET) &&
                additionalData.has(CallManagerModule.Constant.USER_ID) &&
                additionalData.has(CallManagerModule.Constant.NAME) &&
                additionalData.has(CallManagerModule.Constant.PROFILE_IMAGE) &&
                additionalData.has(CallManagerModule.Constant.IS_LEADER) &&
                additionalData.has(CallManagerModule.Constant.VIDEO_HOURS) &&
                additionalData.has(CallManagerModule.Constant.TARGET_NAME) &&
                additionalData.has(CallManagerModule.Constant.TIME_STAMP) &&
                additionalData.has(CallManagerModule.Constant.KEEP_ALIVE)) {

            Log.d(TAG, TAG + " incomingCallReceived has datas...");

            String sessionId = additionalData.getString(CallManagerModule.Constant.SESSION_ID);
            String target = additionalData.getString(CallManagerModule.Constant.TARGET);
            String id_user = additionalData.getString(CallManagerModule.Constant.USER_ID);
            String name = additionalData.getString(CallManagerModule.Constant.NAME);
            String profile_image = additionalData.getString(CallManagerModule.Constant.PROFILE_IMAGE);
            boolean isLeader = additionalData.getBoolean(CallManagerModule.Constant.IS_LEADER);
            int videoHours = additionalData.getInt(CallManagerModule.Constant.VIDEO_HOURS);
            String targetName = additionalData.getString(CallManagerModule.Constant.TARGET_NAME);
            int timeStamp = additionalData.getInt(CallManagerModule.Constant.TIME_STAMP);
            int keepAlive = additionalData.getInt(CallManagerModule.Constant.KEEP_ALIVE);

            int currentTimeStamp = timeStamp;
            int keepAliveTimeStamp = keepAlive;
            int past = currentTimeStamp - keepAliveTimeStamp;
            int future = currentTimeStamp + keepAliveTimeStamp;
            boolean existACall = (past < currentTimeStamp && currentTimeStamp < future);

            Log.d(TAG, TAG + " incomingCallReceived: " + String.valueOf(past) + " <  " + String.valueOf(currentTimeStamp) + " && " + String.valueOf(currentTimeStamp) + " < " + String.valueOf(future) + " = " + String.valueOf(existACall));

            if (existACall) {
                hidden = true;
                PackageManager packageManager = this.getPackageManager();
                Intent launchIntent = packageManager.getLaunchIntentForPackage("falafreud.falafreud.com");
                launchIntent.putExtra(CallManagerModule.Constant.SESSION_ID, sessionId);
                launchIntent.putExtra(CallManagerModule.Constant.TARGET, target);
                launchIntent.putExtra(CallManagerModule.Constant.USER_ID, id_user);
                launchIntent.putExtra(CallManagerModule.Constant.NAME, name);
                launchIntent.putExtra(CallManagerModule.Constant.PROFILE_IMAGE, profile_image);
                launchIntent.putExtra(CallManagerModule.Constant.IS_LEADER, isLeader);
                launchIntent.putExtra(CallManagerModule.Constant.VIDEO_HOURS, videoHours);
                launchIntent.putExtra(CallManagerModule.Constant.TARGET_NAME, targetName);
                launchIntent.putExtra(CallManagerModule.Constant.TIME_STAMP, timeStamp);
                launchIntent.putExtra(CallManagerModule.Constant.KEEP_ALIVE, keepAlive);
                this.startActivity(launchIntent);
            }
        }
        return hidden;
    }

    // CLASS =======================================================================================
    // =============================================================================================
}
