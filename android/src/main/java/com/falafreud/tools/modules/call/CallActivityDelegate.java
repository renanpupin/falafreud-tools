package com.falafreud.tools.modules.call;

import android.app.Activity;
import android.os.Bundle;

import com.facebook.react.ReactActivityDelegate;

import javax.annotation.Nullable;

/**
 * Created by Haroldo Shigueaki Teruya on 30/07/18.
 */
public class CallActivityDelegate extends ReactActivityDelegate {

    // GLOBAL VARIABLES ============================================================================
    // =============================================================================================

    // Data call bundle
    private Bundle bundle = null;

    // Main activity reference
    private final @Nullable
    Activity activity;

    // METHODS =====================================================================================
    // =============================================================================================

    // Main activity call delegator construcor
    public CallActivityDelegate(Activity activity, String mainComponentName) {

        super(activity, mainComponentName);
        this.activity = activity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // bundle is where we put our alarmID with launchIntent.putExtra
        Bundle bundle = activity.getIntent().getExtras();
        if (
                bundle != null &&
                        bundle.containsKey(CallManagerModule.Constant.SESSION_ID) &&
                        bundle.containsKey(CallManagerModule.Constant.TARGET) &&
                        bundle.containsKey(CallManagerModule.Constant.USER_ID) &&
                        bundle.containsKey(CallManagerModule.Constant.NAME) &&
                        bundle.containsKey(CallManagerModule.Constant.PROFILE_IMAGE) &&
                        bundle.containsKey(CallManagerModule.Constant.IS_LEADER) &&
                        bundle.containsKey(CallManagerModule.Constant.VIDEO_HOURS) &&
                        bundle.containsKey(CallManagerModule.Constant.TARGET_NAME) &&
                        bundle.containsKey(CallManagerModule.Constant.TIME_STAMP) &&
                        bundle.containsKey(CallManagerModule.Constant.KEEP_ALIVE)
                ) {
            this.bundle = new Bundle();
            this.bundle = bundle;
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Bundle getLaunchOptions() {

        // this.createPackageForTest(); // discomment this line just for test purpose

        return bundle;
    }

    // CLASS =======================================================================================
    // =============================================================================================
    
    /**
     * For test purpose.
     */
    private void createPackageForTest() {

        this.bundle = new Bundle();
        // put any initialProps here
        this.bundle.putString(CallManagerModule.Constant.SESSION_ID, "sessionId");
        this.bundle.putString(CallManagerModule.Constant.TARGET, "target");
        this.bundle.putString(CallManagerModule.Constant.USER_ID, "id_user");
        this.bundle.putString(CallManagerModule.Constant.NAME, "name");
        this.bundle.putString(CallManagerModule.Constant.PROFILE_IMAGE, "profile_image");
        this.bundle.putString(CallManagerModule.Constant.IS_LEADER, "isLeader");
        this.bundle.putString(CallManagerModule.Constant.VIDEO_HOURS, "videoHours");
        this.bundle.putString(CallManagerModule.Constant.TARGET_NAME, "targetName");
        this.bundle.putString(CallManagerModule.Constant.TIME_STAMP, "timeStamp");
        this.bundle.putString(CallManagerModule.Constant.KEEP_ALIVE, "keepAlive");
    }
}