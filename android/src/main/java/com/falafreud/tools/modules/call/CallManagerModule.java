package com.falafreud.tools.modules.call;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

/**
 * Created by Haroldo Shigueaki Teruya on 01/08/18.
 */
public class CallManagerModule extends ReactContextBaseJavaModule {

    // GLOBAL VARIABLES ============================================================================
    // =============================================================================================

    private final ReactApplicationContext reactContext;
    private static final String TAG = "CallManager";

    // METHODS =====================================================================================
    // =============================================================================================

    public CallManagerModule(ReactApplicationContext reactContext) {

        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {

        return "CallManagerModule";
    }

    // CLASS =======================================================================================
    // =============================================================================================

    public static final class Constant {

        public static final String SESSION_ID = "sessionId";
        public static final String TARGET = "target";
        public static final String USER_ID = "id_user";
        public static final String NAME = "name";
        public static final String PROFILE_IMAGE = "profile_image";
        public static final String IS_LEADER = "isLeader";
        public static final String VIDEO_HOURS = "videoHours";
        public static final String TARGET_NAME = "targetName";
        public static final String TIME_STAMP = "timeStamp";
        public static final String KEEP_ALIVE = "keepAlive";
    }
}
