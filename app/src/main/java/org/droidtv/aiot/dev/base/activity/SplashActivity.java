package org.droidtv.aiot.dev.base.activity;

import android.app.Activity;
import android.os.Bundle;

import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.common.utils.TuyaUtil;
import org.droidtv.aiot.dev.base.utils.ActivityUtils;
import org.droidtv.aiot.dev.base.utils.LoginHelper;
import org.droidtv.aiot.dev.login.activity.LoginActivity;
import org.droidtv.aiot.dev.prelogin.Prelogin_Activity;

import com.tuya.smart.home.sdk.TuyaHomeSdk;


/**
 * Created by letian on 16/7/19.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d("splash", "tuyaTime: " + TuyaUtil.formatDate(System.currentTimeMillis(), "yyyy-mm-dd hh:mm:ss"));

        if (TuyaHomeSdk.getUserInstance().isLogin()) {//已登录，跳主页
           // LoginHelper.afterLogin();
            ActivityUtils.gotoHomeActivity(this);
        } else {
            ActivityUtils.gotoActivity(this, Prelogin_Activity.class, ActivityUtils.ANIMATE_FORWARD, true);
        }
    }

}
