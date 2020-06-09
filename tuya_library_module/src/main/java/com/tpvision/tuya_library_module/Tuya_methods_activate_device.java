package com.tpvision.tuya_library_module;

import android.content.Context;

import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.builder.ActivatorBuilder;
import com.tuya.smart.sdk.api.ITuyaActivator;
import com.tuya.smart.sdk.api.ITuyaSmartActivatorListener;
import com.tuya.smart.sdk.bean.DeviceBean;

import static com.tuya.smart.sdk.enums.ActivatorModelEnum.TY_EZ;

public class Tuya_methods_activate_device {
    private static ITuyaActivator mTuyaActivator;
    static Tuya_activate_Device tuya_activate_device;
    public Tuya_methods_activate_device(  Tuya_activate_Device tuya_activate_device) {
        this.tuya_activate_device=tuya_activate_device;
    }

    public static void newMultiActivator(String ssid, String password, String token, Context mContext, long tomeout) {

        mTuyaActivator = TuyaHomeSdk.getActivatorInstance().newMultiActivator(new ActivatorBuilder()
                .setSsid(ssid)
                .setContext(mContext)
                .setPassword(password)
                .setActivatorModel(TY_EZ)
                .setTimeOut(tomeout)
                .setToken(token).setListener(new ITuyaSmartActivatorListener() {
                    @Override
                    public void onError(String s, String s1) {
                        tuya_activate_device.onError_activating_device(s,s1);
                    }

                    @Override
                    public void onActiveSuccess(DeviceBean gwDevResp) {
                        tuya_activate_device.onActiveSuccess_activating_device(gwDevResp);

                    }

                    @Override
                    public void onStep(String s, Object o) {
                        tuya_activate_device.onStep_activating_device(s,o);


                    }
                }));


    }

}
