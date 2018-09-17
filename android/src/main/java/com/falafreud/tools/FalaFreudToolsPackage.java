package com.falafreud.tools;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.falafreud.tools.modules.call.CallManagerModule;
import com.falafreud.tools.modules.floatwidget.FloatWidgetManagerModule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FalaFreudToolsPackage implements ReactPackage {

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {

        return Arrays.<NativeModule>asList(
            // new FloatWidgetManagerModule(reactContext),
            new CallManagerModule(reactContext));
    }

    // Deprecated from RN 0.47
    public List<Class<? extends JavaScriptModule>> createJSModules() {

        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {

        return Collections.emptyList();
    }
}
