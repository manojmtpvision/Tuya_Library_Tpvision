package com.tpvision.tuya_library_module;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;

import com.tuya.smart.android.base.TuyaSmartSdk;
import com.tuya.smart.android.network.TuyaSmartNetWork;
import com.tuya.smart.android.panel.TuyaPanelSDK;
import com.tuya.smart.android.user.api.ILoginCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.camera.TuyaCameraPanelSDK;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.TuyaSdk;
import com.tuya.smart.sdk.api.INeedLoginListener;
import com.tuya.smart.wrapper.api.TuyaWrapper;

import org.cybergarage.upnp.device.ST;

import static com.tuya.smart.camera.utils.IntentUtils.startActivity;

public class Tuya_methods {
    protected static Login_interface login_interface;

    //Application class
    public static <T> void application_class(final Class<T> className, Context context) {
        TuyaSmartNetWork.mNTY = false;
        TuyaSdk.init((Application) context);
        TuyaSdk.setOnNeedLoginListener(new INeedLoginListener() {
            @Override
            public void onNeedLogin(Context context) {
                Intent intent = new Intent(context, className);
                if (!(context instanceof Activity)) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                startActivity(intent);
            }
        });
        TuyaSdk.setDebugMode(true);
        TuyaWrapper.init((Application) context);
        TuyaCameraPanelSDK.init((Application) context);
        TuyaPanelSDK.init((Application) context, TuyaSmartSdk.getAppkey(), TuyaSmartSdk.getAppSecret());

    }
    //Application class



//Login
    public static void loginWithPhonePassword(String countrycode, String username, String password,Login_interface login_interface) {
        login_interface=login_interface;
        TuyaHomeSdk.getUserInstance().loginWithPhonePassword(countrycode, username, password, mLoginCallback);
    }

    public static void loginWithEmail(String countrycode, String username, String password,Login_interface login_interface) {
        login_interface=login_interface;
        TuyaHomeSdk.getUserInstance().loginWithEmail(countrycode, username, password, mLoginCallback);
    }


    private static ILoginCallback mLoginCallback = new ILoginCallback() {
        @Override
        public void onSuccess(User user) {
            login_interface.success(user);
        }

        @Override
        public void onError(String s, String s1) {
            login_interface.error(s,s1);

        }
    };
    //Login
}
