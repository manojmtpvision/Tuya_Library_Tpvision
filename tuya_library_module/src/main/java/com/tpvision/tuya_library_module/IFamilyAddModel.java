package com.tpvision.tuya_library_module;

import com.tuya.smart.home.sdk.bean.HomeBean;

public interface IFamilyAddModel {
void success_home_added(HomeBean homeBean);
void error_home_added(String s, String s1);
}
