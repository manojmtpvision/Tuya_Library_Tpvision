package com.tpvision.tuya_library_module;

import com.tuya.smart.android.user.bean.User;

public interface Login_interface {
    void login_success(User user);
    void login_error(String s, String s1);
}
