package com.tpvision.tuya_library_module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.tuya.smart.android.panel.TuyaPanelSDK;
import com.tuya.smart.android.panel.api.ITuyaPanelLoadCallback;
import com.tuya.smart.camera.DeviceInfoListener;
import com.tuya.smart.camera.FeedbackListener;
import com.tuya.smart.camera.TuyaCameraPanelSDK;
import com.tuya.smart.camera.blackpanel.activity.CameraPanelActivity;
import com.tuya.smart.sdk.bean.DeviceBean;

import static android.content.Context.MODE_PRIVATE;

public class Tuya_methods_on_device_item_click {
    static Context context;
    static Panel_interface panel_interface;
    public Tuya_methods_on_device_item_click(Context context, Panel_interface panel_interface){
        this.context=context;
        this.panel_interface=panel_interface;
    }
    public static void onDeviceClick(DeviceBean deviceBean) {
        if (!deviceBean.getIsOnline()) {
//            showDevIsNotOnlineTip(deviceBean);
            Log.d("results","offline");
            return;
        }
        Activity activity = (Activity) context;

        onItemClick(deviceBean, activity);
    }

    private static void onItemClick(DeviceBean devBean, Activity context) {
        if (devBean == null) {
            Log.d("results","devBean is null");

            return;
        }
        if (devBean.getProductId().equals("4eAeY1i5sUPJ8m8d")) {
            Log.d("results","4eAeY1i5sUPJ8m8d");

//            Intent intent = new Intent(mActivity, SwitchActivity.class);
//            intent.putExtra(SwitchActivity.INTENT_DEVID, devBean.getDevId());
//            mActivity.startActivity(intent);
        } else {
            gotoDeviceCommonActivity(devBean, context);
        }

    }

    private static void gotoDeviceCommonActivity(DeviceBean devBean, Activity context) {
        Log.d("results","gotoDeviceCommonActivity called");

        SharedPreferences prefs = context.getSharedPreferences("home_id", MODE_PRIVATE);
        Long name = prefs.getLong("home", 0);
        gotoCameraPanel(context, name, devBean);

        TuyaCameraPanelSDK.setCustomDeviceInfoListener(new DeviceInfoListener() {
            @Override
            public void onDeviceInfoClick(String deviceId) {
            }
        });

        TuyaCameraPanelSDK.setCustomFeedbackListener(new FeedbackListener() {

            @Override
            public void onFeedbackClick(String deviceId) {
            }
        });
    }

    private static void gotoCameraPanel(Activity context, long homeId, DeviceBean devBean) {
        Log.d("results","gotoCameraPanel called");

        Log.d("results",Long.toString(homeId));

        if (null == devBean || null == devBean.getProductBean()) {
            return;
        }
        Log.d("results",devBean.getProductBean().getUiInfo().getType());
        //判断是什么面板
        if ("RN".equals(devBean.getProductBean().getUiInfo().getType())) {
            Log.d("results","RN trigerred");

            if (context instanceof Activity) {
                gotoRNCameraPanel( context, homeId, devBean.getDevId());
            }
        } else if ("NA".equals(devBean.getProductBean().getUiInfo().getType())) {
            if (context instanceof Activity) {
                gotoNativeCameraPanel(context, homeId, devBean.getDevId());
            }
        }
    }

    private static void gotoNativeCameraPanel(Context context, long homeId, String deviceId) {
        TuyaCameraPanelSDK.setCurrentHomeId(homeId);
        Log.d("results", "Activity_Trigerred");

        //方式一
        Bundle bundle = new Bundle();
        bundle.putString("extra_camera_uuid", deviceId);
        Intent intent = new Intent(context, CameraPanelActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private static void gotoRNCameraPanel(Activity context, long homeId, String deviceId) {
        Log.d("results", "gotoRNCameraPanel trigerred");
        TuyaCameraPanelSDK.setCurrentHomeId(homeId);
        TuyaPanelSDK.getPanelInstance().gotoPanelViewControllerWithDevice(context, homeId, deviceId, new ITuyaPanelLoadCallback() {
            @Override
            public void onStart(String s) {
                panel_interface.panel_onstart(s);
            }

            @Override
            public void onError(String s, int i, String s1) {
                panel_interface.panel_onerror(s,i,s1);

            }

            @Override
            public void onSuccess(String s) {
                panel_interface.panel_onSuccess(s);
            }

            @Override
            public void onProgress(String s, int i) {
                panel_interface.panel_onProgress(s,i);
            }
        });
    }
}
