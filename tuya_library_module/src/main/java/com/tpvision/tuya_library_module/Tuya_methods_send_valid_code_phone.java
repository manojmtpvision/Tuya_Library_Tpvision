package com.tpvision.tuya_library_module;

import com.tuya.smart.android.user.api.IRegisterCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.TuyaHomeSdk;

public class Tuya_methods_send_valid_code_phone {
    private static Code_valid_Phone_or_Email code_valid_phone_or_email;

    public Tuya_methods_send_valid_code_phone(Code_valid_Phone_or_Email code_valid_phone_or_email){
        this.code_valid_phone_or_email=code_valid_phone_or_email;

    }

    public static void send_validate_code_with_phone(String mCountryCode,String mPhoneNum,String password,String code) {
        TuyaHomeSdk.getUserInstance().registerAccountWithPhone(mCountryCode, mPhoneNum, password,code, code_with_email_or_phone);


    }
    private static IRegisterCallback code_with_email_or_phone = new IRegisterCallback() {
        @Override
        public void onSuccess(User user) {
            code_valid_phone_or_email.valid_code_success(user);
        }

        @Override
        public void onError(String errorCode, String errorMsg) {
            code_valid_phone_or_email.valid_code_failure( errorCode,  errorMsg);


        }
    };
}
