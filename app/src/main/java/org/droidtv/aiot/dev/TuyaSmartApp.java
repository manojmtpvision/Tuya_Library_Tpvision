package org.droidtv.aiot.dev;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;


import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.tpvision.tuya_library_module.Tuya_methods;
import com.tuya.smart.android.base.TuyaSmartSdk;
import com.tuya.smart.android.common.utils.L;
import org.droidtv.aiot.dev.login.activity.LoginActivity;
import org.droidtv.aiot.dev.prelogin.Prelogin_Activity;

import com.tuya.smart.android.network.TuyaSmartNetWork;
import com.tuya.smart.android.panel.TuyaPanelSDK;
import com.tuya.smart.camera.TuyaCameraPanelSDK;
import com.tuya.smart.sdk.TuyaSdk;
import com.tuya.smart.sdk.api.INeedLoginListener;
import com.tuya.smart.wrapper.api.TuyaWrapper;


public class TuyaSmartApp extends MultiDexApplication {

    private static final String TAG = "TuyaSmartApp";
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(TuyaSmartApp.this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        L.d(TAG, "onCreate " + getProcessName(this));
        L.setSendLogOn(true);
        TuyaSmartNetWork.mNTY = false;
        TuyaSdk.init(this);
        TuyaSdk.setOnNeedLoginListener(new INeedLoginListener() {
            @Override
            public void onNeedLogin(Context context) {
                Intent intent = new Intent(context, Prelogin_Activity.class);
                if (!(context instanceof Activity)) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                startActivity(intent);
            }
        });
        TuyaSdk.setDebugMode(true);
        TuyaWrapper.init(this);
        TuyaCameraPanelSDK.init(this);
        TuyaPanelSDK.init(this, TuyaSmartSdk.getAppkey(), TuyaSmartSdk.getAppSecret());
    }

    public static String getProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }

    private static Context context;

    public static Context getAppContext() {
        return context;
    }


}
