package com.tpvision.tuya_library_module;

import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.IResultCallback;

public class Tuya_methods_register_with_email {
    private static Email_register email_register_callback;

    public Tuya_methods_register_with_email(Email_register email_register_callback) {
        this.email_register_callback = email_register_callback;

    }

    public static void registerwith_email(String countrycode, String email) {
        TuyaHomeSdk.getUserInstance().getRegisterEmailValidateCode(countrycode, email, email_register);

    }


    static IResultCallback email_register = new IResultCallback() {
        @Override
        public void onError(String s, String s1) {
            email_register_callback.email_register_error(s,s1);
        }

        @Override
        public void onSuccess() {
            email_register_callback.email_register_success();

        }
    };
}
