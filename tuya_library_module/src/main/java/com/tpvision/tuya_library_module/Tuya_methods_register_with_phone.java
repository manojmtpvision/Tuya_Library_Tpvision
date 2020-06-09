package com.tpvision.tuya_library_module;

import com.tuya.smart.android.user.api.IValidateCallback;
import com.tuya.smart.home.sdk.TuyaHomeSdk;

public class Tuya_methods_register_with_phone {
    private static Phone_register phone_register_callback;

    public Tuya_methods_register_with_phone(Phone_register phone_register_callback){
        this.phone_register_callback=phone_register_callback;

    }
    public static void register_with_phone(String mCountryCode, String mPhoneNum) {
        TuyaHomeSdk.getUserInstance().getValidateCode(mCountryCode, mPhoneNum, phone_register);

    }
    private static IValidateCallback phone_register = new IValidateCallback() {
        @Override
        public void onSuccess() {
            phone_register_callback.Phone_register_success();
        }

        @Override
        public void onError(String s, String s1) {
            phone_register_callback.Phone_register_error(s,s1);

        }
    };
}
