package com.tpvision.tuya_library_module;

import com.tuya.smart.android.user.bean.User;

public interface Login_interface {
    void success(User user);
    void error(String s, String s1);
}
