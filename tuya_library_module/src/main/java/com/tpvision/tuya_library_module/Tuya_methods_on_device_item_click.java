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
    Context context;
    public Tuya_methods_on_device_item_click(Context context){
        this.context=context;
    }
    public static void onDeviceClick(DeviceBean deviceBean, Context context) {
        if (!deviceBean.getIsOnline()) {
//            showDevIsNotOnlineTip(deviceBean);
            return;
        }
        onItemClick(deviceBean, context);
    }

    private static void onItemClick(DeviceBean devBean, Context context) {
        if (devBean == null) {
            return;
        }
        if (devBean.getProductId().equals("4eAeY1i5sUPJ8m8d")) {
//            Intent intent = new Intent(mActivity, SwitchActivity.class);
//            intent.putExtra(SwitchActivity.INTENT_DEVID, devBean.getDevId());
//            mActivity.startActivity(intent);
        } else {
            gotoDeviceCommonActivity(devBean, context);
        }

    }

    private static void gotoDeviceCommonActivity(DeviceBean devBean, Context context) {
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

    private static void gotoCameraPanel(Context context, long homeId, DeviceBean devBean) {

        if (null == devBean || null == devBean.getProductBean()) {
            return;
        }
        //判断是什么面板
        if ("RN".equals(devBean.getProductBean().getUiInfo().getType())) {
            if (context instanceof Activity) {
                gotoRNCameraPanel((Activity) context, homeId, devBean.getDevId());
            }
        } else if ("NA".equals(devBean.getProductBean().getUiInfo().getType())) {
            if (context instanceof Activity) {
                gotoNativeCameraPanel(context, homeId, devBean.getDevId());
            }
        }
    }

    private static void gotoNativeCameraPanel(Context context, long homeId, String deviceId) {
        TuyaCameraPanelSDK.setCurrentHomeId(homeId);
        Log.d("Results", "Activity_Trigerred");

        //方式一
        Bundle bundle = new Bundle();
        bundle.putString("extra_camera_uuid", deviceId);
        Intent intent = new Intent(context, CameraPanelActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private static void gotoRNCameraPanel(Activity context, long homeId, String deviceId) {
        Log.d("Results", "callback_trigerred");
        TuyaCameraPanelSDK.setCurrentHomeId(homeId);
        TuyaPanelSDK.getPanelInstance().gotoPanelViewControllerWithDevice(context, homeId, deviceId, new ITuyaPanelLoadCallback() {
            @Override
            public void onStart(String s) {
            }

            @Override
            public void onError(String s, int i, String s1) {
            }

            @Override
            public void onSuccess(String s) {
            }

            @Override
            public void onProgress(String s, int i) {
            }
        });
    }
}
