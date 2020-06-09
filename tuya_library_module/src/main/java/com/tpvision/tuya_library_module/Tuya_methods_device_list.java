package com.tpvision.tuya_library_module;

import android.content.Context;
import android.content.SharedPreferences;

import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.api.ITuyaHomeStatusListener;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.callback.ITuyaGetHomeListCallback;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Tuya_methods_device_list {
    public static long HOME_ID = 1099001;
    static Context context;
    public Tuya_methods_device_list(Context context){
        this.context=context;

    }

    public static void getDataFromServer() {
        TuyaHomeSdk.getHomeManagerInstance().queryHomeList(new ITuyaGetHomeListCallback() {
            @Override
            public void onSuccess(List<HomeBean> homeBeans) {
                if (homeBeans.size() == 0) {
//                    mView.gotoCreateHome();
                    return;
                }
                final long homeId = homeBeans.get(0).getHomeId();
                HOME_ID = homeId;
                SharedPreferences.Editor editor =context. getSharedPreferences("home_id", MODE_PRIVATE).edit();
                editor.putLong("home", HOME_ID);
                editor.apply();
                TuyaHomeSdk.newHomeInstance(homeId).getHomeDetail(new ITuyaHomeResultCallback() {
                    @Override
                    public void onSuccess(HomeBean bean) {

                        getdevice_list(bean.getDeviceList());
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {

                    }
                });
                TuyaHomeSdk.newHomeInstance(homeId).registerHomeStatusListener(new ITuyaHomeStatusListener() {
                    @Override
                    public void onDeviceAdded(String devId) {
                    }

                    @Override
                    public void onDeviceRemoved(String devId) {

                    }

                    @Override
                    public void onGroupAdded(long groupId) {

                    }

                    @Override
                    public void onGroupRemoved(long groupId) {

                    }

                    @Override
                    public void onMeshAdded(String meshId) {
                    }


                });

            }

            @Override
            public void onError(String errorCode, String error) {
                TuyaHomeSdk.newHomeInstance(HOME_ID).getHomeLocalCache(new ITuyaHomeResultCallback() {
                    @Override
                    public void onSuccess(HomeBean bean) {
//                        L.d(TAG, com.alibaba.fastjson.JSONObject.toJSONString(bean));
                        getdevice_list(bean.getDeviceList());
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {

                    }
                });
            }
        });
    }

    private static List<DeviceBean>  getdevice_list(List<DeviceBean> deviceList) {

        return deviceList;

    }
}
