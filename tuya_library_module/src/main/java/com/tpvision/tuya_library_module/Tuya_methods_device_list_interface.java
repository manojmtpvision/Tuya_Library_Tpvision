package com.tpvision.tuya_library_module;

import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.List;

public interface Tuya_methods_device_list_interface {
    void onSuccess_device_list(List<DeviceBean> deviceList);
    void onerror_device_list(String errorCode, String errorMsg);
}
