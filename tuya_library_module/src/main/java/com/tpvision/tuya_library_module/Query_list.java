package com.tpvision.tuya_library_module;

import com.tuya.smart.home.sdk.bean.HomeBean;

import java.util.List;

public interface Query_list {
    void query_success(List<HomeBean> list);
    void query_failure(String s, String s1);
}
