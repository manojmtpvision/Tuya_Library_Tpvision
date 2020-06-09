package com.tpvision.tuya_library_module;

import com.tuya.smart.android.user.bean.User;

public interface Code_valid_Phone_or_Email {
   void valid_code_success(User user);
   void valid_code_failure(String errorCode, String errorMsg);
}
