package com.tpvision.tuya_library_module;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.sdk.api.ITuyaActivatorGetToken;

public class SetHome {
    static String userId;
    static String json;
    private static String tokens;
    static Get_Token_Interface get_token_interface;
    public SetHome(Get_Token_Interface get_token_interface){
        this.get_token_interface=get_token_interface;

    }

    public static void setcurrenthome(HomeBean homeBean) {
        long targetHomeId = homeBean.getHomeId();
        putcurrenthome(homeBean);

    }

    private static void putcurrenthome(HomeBean homeBean) {
        userId = null;
        User user = TuyaHomeSdk.getUserInstance().getUser();
        if (null != user) {
            userId = user.getUid();
        }
        json = JSON.toJSONString(homeBean);
    }

    public static HomeBean getCurrentHome() {
        Log.d("Results", "getCurrentHome inside Familysphelper called");

        userId = null;
        User user = TuyaHomeSdk.getUserInstance().getUser();
        if (null != user) {
            userId = user.getUid();
            Log.d("Results", "getCurrentHome inside Familysphelper called " + userId);

        }

        String currentFamilyStr = json;
        if (TextUtils.isEmpty(currentFamilyStr)) {
            return null;
        }
        Log.d("Results", "getCurrentHome inside Familysphelper called " + currentFamilyStr);

        return JSON.parseObject(currentFamilyStr, HomeBean.class);
    }

    public static void getTokenForConfigDevice() {
        long homeId = getCurrentHomeId();
        TuyaHomeSdk.getActivatorInstance().getActivatorToken(homeId, new ITuyaActivatorGetToken() {
            @Override
            public void onSuccess(String token) {
                initConfigDevice(token);
                get_token_interface.get_token_onSuccess(token);

            }

            @Override
            public void onFailure(String s, String s1) {
                get_token_interface.get_token_onFailure(s,s1);

            }
        });
    }

    public static String getTokens() {
        return tokens;
    }

    private static void initConfigDevice(String token) {
        tokens = token;
//        return tokens;
    }

    private static long getCurrentHomeId() {

        Log.d("Results", "getCurrentHomeId called");
        HomeBean currentHome = getCurrentHome();
        if (null == currentHome) {
            return -1;
        }
        return currentHome.getHomeId();
    }
}
