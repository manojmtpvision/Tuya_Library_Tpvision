package com.tpvision.tuya_library_module;

import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;

import java.util.List;

public class Tuya_methods_addfamily {
    private static IFamilyAddModel iFamilyAddModel;

    public Tuya_methods_addfamily(IFamilyAddModel iFamilyAddModel){
        this.iFamilyAddModel=iFamilyAddModel;

    }

    public static void addfamily(String homeName, List<String> roomList) {
        TuyaHomeSdk.getHomeManagerInstance().createHome(homeName, 0, 0, "", roomList, new ITuyaHomeResultCallback() {
            @Override
            public void onSuccess(HomeBean homeBean) {
                iFamilyAddModel.success_home_added(homeBean);

            }

            @Override
            public void onError(String s, String s1) {
                iFamilyAddModel.error_home_added(s, s1);
            }
        });
    }
}
