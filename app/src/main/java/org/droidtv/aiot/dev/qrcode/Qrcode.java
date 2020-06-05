package org.droidtv.aiot.dev.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.airbnb.lottie.animation.content.Content;
import com.tuya.sdk.panel.common.utils.SharedPreferencesUtils;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.builder.TuyaQRCodeActivatorBuilder;
import com.tuya.smart.sdk.api.ITuyaActivator;
import com.tuya.smart.sdk.api.ITuyaDataCallback;
import com.tuya.smart.sdk.api.ITuyaSmartActivatorListener;
import com.tuya.smart.sdk.bean.DeviceBean;

import org.droidtv.aiot.dev.MessageEvent;
import org.droidtv.aiot.dev.NewBaseActivity;
import org.droidtv.aiot.dev.base.utils.ToastUtil;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

public class Qrcode extends NewBaseActivity implements QRCodeView.Delegate {
    public final static String BUNDLE_SCAN_QR_CODE_ACTIVITY = "BUNDLE_SCAN_QR_CODE_ACTIVITY";
    private final String TAG = Qrcode.class.getSimpleName();
    @BindView(R.id.zbarview)
    ZBarView zbarview;
    public final static String WIFI_NAME = "WIFI_NAME";
    public final static String WIFI_PASSWORD = "WIFI_PASSWORD";
    public final static String TOKEN = "TOKEN";
    private String mWifiName;
    private String mToken;
    private String mPassword;

    private Handler mHandler = new Handler();
    private ITuyaActivator mTuyaActivator;
    private String mCodeUrl = "";

    public static void enterScanQrCodeActivity(Activity activity, String token, String ssid, String password) {
        Intent intent = new Intent(activity, Qrcode.class);
        Bundle bundle = new Bundle();
        bundle.putString(Qrcode.WIFI_NAME, ssid);
        bundle.putString(Qrcode.WIFI_PASSWORD, password);
        bundle.putString(Qrcode.TOKEN, token);
        intent.putExtra(BUNDLE_SCAN_QR_CODE_ACTIVITY, bundle);
        activity.startActivity(intent);
    }

    private void getParam() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra(BUNDLE_SCAN_QR_CODE_ACTIVITY);
            if (bundle != null) {
                mWifiName = bundle.getString(Qrcode.WIFI_NAME);
                mPassword = bundle.getString(Qrcode.WIFI_PASSWORD);
                mToken = bundle.getString(Qrcode.TOKEN);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_code);
        ButterKnife.bind(this);
        initToolbar();
        getParam();
        init();
    }

    private void initToolbar() {
        Toolbar idToolbar = findViewById(R.id.idToolbar);
        setSupportActionBar(idToolbar);
        TextView toolbarTitle = idToolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("扫一扫");
        idToolbar.setNavigationIcon(R.drawable.ty_hb_back_icon);
        idToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        zbarview.setDelegate(Qrcode.this);
    }

    @Override
    public void onResume() {
        super.onResume();
        zbarview.startCamera();
        zbarview.startSpotAndShowRect();
    }

    @Override
    public void onPause() {
        zbarview.stopCamera();
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (mTuyaActivator != null) {
            mTuyaActivator.stop();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        zbarview.onDestroy();
        if (mTuyaActivator != null) {
            mTuyaActivator.onDestroy();
        }
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Activity.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i(TAG, "result:" + result);
        if (TextUtils.isEmpty(mCodeUrl)) {
            mCodeUrl = result;
            ToastUtil.shortToast(Qrcode.this, result);
            getUUID(mCodeUrl);
            vibrate();
            zbarview.startSpot();
        }
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        String tipText = zbarview.getScanBoxView().getTipText();
        if (!TextUtils.isEmpty(tipText)) {
            String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
            if (isDark) {
                if (!tipText.contains(ambientBrightnessTip)) {
                    zbarview.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
                }
            } else {
                if (tipText.contains(ambientBrightnessTip)) {
                    tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                    zbarview.getScanBoxView().setTipText(tipText);
                }
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();


    }

    private void getUUID(String url) {
        Map<String, Object> postData = new HashMap<>();
        postData.put("code", url);
        TuyaHomeSdk.getRequestInstance().requestWithApiNameWithoutSession("tuya.m.qrcode.parse", "4.0", postData, String.class, new ITuyaDataCallback<String>() {
            @Override
            public void onSuccess(String result) {
                String uuid = result;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("actionData");
                    Object object = jsonObject1.opt("uuid");
                    if (object instanceof String) {
                        getDeviceBean((String) object);
                    }
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
                mCodeUrl = "";
                zbarview.startSpot();
            }
        });
    }

    private void getDeviceBean(String uuid) {
//        try {
          //  long homeId = SharedPreferencesUtils.getLongParam(ScanQrCodeActivity.this, Content.HOME_ID, Content.DEFAULT_HOME_ID);
//            TuyaQRCodeActivatorBuilder builder = new TuyaQRCodeActivatorBuilder()
//                    .setUuid(uuid)
//                    .setHomeId(homeId)
//                    .setContext(Qrcode.this)
//                    .setTimeOut(100)
//                    .setListener(new ITuyaSmartActivatorListener() {
//                        @Override
//                        public void onError(String errorCode, String errorMsg) {
//                            Log.d(TAG, " error = " + errorMsg);
//                            mCodeUrl = "";
//                            zbarview.startSpot();
//                        }
//
//                        @Override
//                        public void onActiveSuccess(DeviceBean devResp) {
//                            Log.d(TAG, " Success = " + devResp.toString());
//                            mHandler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
////                                SharedPreferencesUtils.setParam(ScanQrCodeActivity.this, Content.SCAN_SELECT_DEVICE, DeviceBean.class);
////                                    MessageEvent messageEvent = new MessageEvent();
////                                    messageEvent.setEventType(EventConstant.MAIN_ACTIVITY_FRAGMENT_GET_SERVICE_DATA);
////                                    EventBus.getDefault().post(messageEvent);
////                                    ScanGetDeviceActivity.enterScanGetDeviceActivity(ScanQrCodeActivity.this, devResp.getDevId(), devResp.getName(), devResp.iconUrl);
//                                }
//                            }, 2000);
//                        }
//
//                        @Override
//                        public void onStep(String step, Object data) {
//                            Log.d(TAG, " onStep = " + data.toString());
//                        }
//                    });
//            mTuyaActivator = TuyaHomeSdk.getActivatorInstance().newQRCodeDevActivator(builder);
//            mTuyaActivator.start();
//        }catch (Exception e){
//            Log.d(TAG, e.toString());
//        }
    }


}
