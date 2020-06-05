package org.droidtv.aiot.dev;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.tuya.smart.android.demo.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Unbinder;

public abstract class NewBaseActivity extends RxAppCompatActivity implements IView{
    public Unbinder unbinder;
    public boolean isHideKeyboardEnable = true;
    private SVProgressHUD progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initBase();
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.toolbar_bar_background));
        }

    }


    private void initBase() {
        if (registerEventBus()) {
            initEventBus();
        }
    }

    /**
     * 注册EventBus事件监听
     */
    private void initEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    public boolean isLoading() {
        return progressDialog != null && progressDialog.isShowing();
    }

    public void showLoadingDialog(final String text) {
        runOnUiThread(new Runnable() {
            public void run() {
                if (progressDialog == null || !progressDialog.isShowing()) {
                    progressDialog = new SVProgressHUD(NewBaseActivity.this);
                }
                if (text != null) {
                    progressDialog.showWithStatus(text);
                }
                progressDialog.show();
            }
        });

    }

    public void hideLoadingDialog() {
        runOnUiThread(new Runnable() {
            public void run() {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });

    }


    @Override
    protected void onDestroy() {
        try {
            //移除所有fragment
            List<Fragment> list = getSupportFragmentManager().getFragments();
            if (list != null && list.size() > 0) {
                for (Fragment fragment : list) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.remove(fragment);
                    ft.commitAllowingStateLoss();
                }
            }

        } catch (Exception e) {
        }
        //判断是否注册了监听事件，如果注册了监听事件，则取消注册
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }


        super.onDestroy();
    }


    @Override
    public Object getContextObject() {
        return this;
    }

    @Subscribe
    public void onEventReceive(Object event) {

    }

    @Override
    public LifecycleProvider getLifecycleProvider() {
        return this;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public boolean registerEventBus() {
        return false;
    }

    public Activity getActivity() {
        return this;
    }

    public void setBinder(Unbinder unbinder) {
        this.unbinder = unbinder;
    }

    /**
     * 点击空白区域 自动隐藏软键盘
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isHideKeyboardEnable) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideKeyboard(v, ev)) {
                    if (v != null) {
                        hideKeyboard(v.getWindowToken());
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     */
    public boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left) || !(event.getX() < right) || !(event.getY() > top) || !(event.getY() < bottom);
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }


    /**
     * 获取InputMethodManager，隐藏软键盘
     */
    public void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (im != null) {
                im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public void setUnbinder(Unbinder unbinder) {
        this.unbinder = unbinder;
    }

}
