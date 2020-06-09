package com.tpvision.tuya_library_module;

import com.tuya.smart.android.user.api.ILoginCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.TuyaHomeSdk;

public class Tuya_methods_Login {
    protected static Login_interface login_interface;


    public Tuya_methods_Login(){
        this.login_interface = login_interface;

    }

    public static void loginWithPhonePassword(String countrycode, String username, String password) {
        TuyaHomeSdk.getUserInstance().loginWithPhonePassword(countrycode, username, password, mLoginCallback);
    }

    public static void loginWithEmail(String countrycode, String username, String password) {
        TuyaHomeSdk.getUserInstance().loginWithEmail(countrycode, username, password, mLoginCallback);
    }

    private static ILoginCallback mLoginCallback = new ILoginCallback() {
        @Override
        public void onSuccess(User user) {
            login_interface.login_success(user);
        }

        @Override
        public void onError(String s, String s1) {
            login_interface.login_error(s, s1);

        }
    };
}
