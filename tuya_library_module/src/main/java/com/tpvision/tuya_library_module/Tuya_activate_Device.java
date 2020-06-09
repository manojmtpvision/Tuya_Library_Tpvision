package com.tpvision.tuya_library_module;

import com.tuya.smart.sdk.bean.DeviceBean;

public interface Tuya_activate_Device {
    void onError_activating_device(String s, String s1);
    void onActiveSuccess_activating_device(DeviceBean gwDevResp);
    void onStep_activating_device(String s, Object o);
}
