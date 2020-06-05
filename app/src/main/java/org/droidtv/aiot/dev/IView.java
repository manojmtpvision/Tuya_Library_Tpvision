package org.droidtv.aiot.dev;

import android.app.Activity;

import com.trello.rxlifecycle2.LifecycleProvider;

public interface IView {

    Object getContextObject();

    Activity getActivity();

    boolean registerEventBus();

    void onEventReceive(Object object);

    LifecycleProvider getLifecycleProvider();
}
