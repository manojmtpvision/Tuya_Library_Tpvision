package com.tpvision.tuya_library_module;

import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.callback.ITuyaGetHomeListCallback;

import java.util.List;

public class Tuya_methods_Query_list {
    private static Query_list query_list;

    public Tuya_methods_Query_list(Query_list query_list) {
        this.query_list=query_list;
    }

    public static void query_list() {
        TuyaHomeSdk.getHomeManagerInstance().queryHomeList(new ITuyaGetHomeListCallback() {
            @Override
            public void onSuccess(List<HomeBean> list) {
                query_list.query_success(list);
            }

            @Override
            public void onError(String s, String s1) {
                query_list.query_failure(s, s1);

            }
        });
    }
}
