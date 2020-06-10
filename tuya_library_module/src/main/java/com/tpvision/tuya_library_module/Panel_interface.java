package com.tpvision.tuya_library_module;

public interface Panel_interface {
    void panel_onstart(String s);
    void panel_onerror(String s, int i, String s1);
    void panel_onSuccess(String s);
    void panel_onProgress(String s, int i);
}
