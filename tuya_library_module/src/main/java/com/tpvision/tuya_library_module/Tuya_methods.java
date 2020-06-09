package com.tpvision.tuya_library_module;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.tuya.smart.android.base.TuyaSmartSdk;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.network.TuyaSmartNetWork;
import com.tuya.smart.android.panel.TuyaPanelSDK;
import com.tuya.smart.android.panel.api.ITuyaPanelLoadCallback;
import com.tuya.smart.android.user.api.ILoginCallback;
import com.tuya.smart.android.user.api.IRegisterCallback;
import com.tuya.smart.android.user.api.IValidateCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.camera.DeviceInfoListener;
import com.tuya.smart.camera.FeedbackListener;
import com.tuya.smart.camera.TuyaCameraPanelSDK;
import com.tuya.smart.camera.blackpanel.activity.CameraPanelActivity;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.api.ITuyaHomeStatusListener;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.builder.ActivatorBuilder;
import com.tuya.smart.home.sdk.callback.ITuyaGetHomeListCallback;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;
import com.tuya.smart.sdk.TuyaSdk;
import com.tuya.smart.sdk.api.INeedLoginListener;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.sdk.api.ITuyaActivator;
import com.tuya.smart.sdk.api.ITuyaActivatorGetToken;
import com.tuya.smart.sdk.api.ITuyaSmartActivatorListener;
import com.tuya.smart.sdk.bean.DeviceBean;
import com.tuya.smart.sdk.enums.ActivatorEZStepCode;
import com.tuya.smart.wrapper.api.TuyaWrapper;

import org.cybergarage.upnp.device.ST;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.tuya.smart.camera.utils.IntentUtils.startActivity;
import static com.tuya.smart.sdk.enums.ActivatorModelEnum.TY_EZ;

public class Tuya_methods {
    protected static Login_interface login_interface;
    private static Email_register email_register_callback;
    private static IFamilyAddModel iFamilyAddModel;
    private static Phone_register phone_register_callback;
    private static  Code_valid_Phone_or_Email code_valid_phone_or_email;
    private static Query_list query_list;
    private HomeBean currentHomeBean;
    static String userId;
    static String json;
    private static String tokens;
    private static ITuyaActivator mTuyaActivator;
    public static long HOME_ID = 1099001;


    //---------------------------------------------------------
    //Login constructor
    public Tuya_methods(Login_interface login_interface) {
        this.login_interface = login_interface;

    }
    //Login constructor
//---------------------------------------------------------

    //Add family constructor
    public Tuya_methods(IFamilyAddModel iFamilyAddModel) {
        this.iFamilyAddModel = iFamilyAddModel;


    }
    //Add family constructor
//---------------------------------------------------------

    //query_list
    public Tuya_methods(Query_list list) {
        this.query_list = list;

    }
    //query_list
//---------------------------------------------------------
    //register with email
    public Tuya_methods(Email_register email_register_callback){
        this.email_register_callback=email_register_callback;

    }
    //register with email
    //------------------------------------------------------
    //register with phone
    public Tuya_methods(Phone_register phone_register_callback){
        this.phone_register_callback=phone_register_callback;

    }
    //register with phone
    //------------------------------------------------------
    //validate code

    public Tuya_methods( Code_valid_Phone_or_Email code_valid_phone_or_email){
        this.code_valid_phone_or_email=code_valid_phone_or_email;
    }
    //validate code
    //-----------------------------------------------------------


    //Application class
    public static <T> void application_class(final Class<T> className, Context context) {
        TuyaSmartNetWork.mNTY = false;
        TuyaSdk.init((Application) context);
        TuyaSdk.setOnNeedLoginListener(new INeedLoginListener() {
            @Override
            public void onNeedLogin(Context context) {
                Intent intent = new Intent(context, className);
                if (!(context instanceof Activity)) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                startActivity(intent);
            }
        });
        TuyaSdk.setDebugMode(true);
        TuyaWrapper.init((Application) context);
        TuyaCameraPanelSDK.init((Application) context);
        TuyaPanelSDK.init((Application) context, "5vpadpjdykpj93jngge7", "qkcqraaqx9wa88wppd9rtvcq8vhnja4t");

    }
    //Application class
//---------------------------------------------------------


    //Login
    public static void loginWithPhonePassword(String countrycode, String username, String password) {
        TuyaHomeSdk.getUserInstance().loginWithPhonePassword(countrycode, username, password, mLoginCallback);
    }

    public static void loginWithEmail(String countrycode, String username, String password) {
        TuyaHomeSdk.getUserInstance().loginWithEmail(countrycode, username, password, mLoginCallback);
    }


    private static ILoginCallback mLoginCallback = new ILoginCallback() {
        @Override
        public void onSuccess(User user) {
            login_interface.login_success(user);
        }

        @Override
        public void onError(String s, String s1) {
            login_interface.login_error(s, s1);

        }
    };

    //Login
//---------------------------------------------------------
    //register
    public static void registerwith_email(String countrycode, String email) {
        TuyaHomeSdk.getUserInstance().getRegisterEmailValidateCode(countrycode, email, email_register);

    }

    static IResultCallback email_register = new IResultCallback() {
        @Override
        public void onError(String s, String s1) {
            email_register_callback.email_register_error(s,s1);
        }

        @Override
        public void onSuccess() {
            email_register_callback.email_register_success();

        }
    };

    public static void register_with_phone(String mCountryCode, String mPhoneNum) {
        TuyaHomeSdk.getUserInstance().getValidateCode(mCountryCode, mPhoneNum, phone_register);

    }

    private static IValidateCallback phone_register = new IValidateCallback() {
        @Override
        public void onSuccess() {
            phone_register_callback.Phone_register_success();
        }

        @Override
        public void onError(String s, String s1) {
            phone_register_callback.Phone_register_error(s,s1);

        }
    };

    public static void send_validate_code_with_email(String mCountryCode,String mEmail,String password,String code) {
        TuyaHomeSdk.getUserInstance().registerAccountWithEmail(mCountryCode, mEmail, password, code,code_with_email_or_phone);

    }
    private static IRegisterCallback code_with_email_or_phone = new IRegisterCallback() {
        @Override
        public void onSuccess(User user) {
            code_valid_phone_or_email.valid_code_success(user);
        }

        @Override
        public void onError(String errorCode, String errorMsg) {
            code_valid_phone_or_email.valid_code_failure( errorCode,  errorMsg);


        }
    };

    public static void send_validate_code_with_phone(String mCountryCode,String mPhoneNum,String password,String code) {
        TuyaHomeSdk.getUserInstance().registerAccountWithPhone(mCountryCode, mPhoneNum, password,code, code_with_email_or_phone);


    }

    //register
    //-------------------------------------------------------------------
    //add Family
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

    //add Family
    //---------------------------------------------------------
//query_list
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

    //query_list
    //---------------------------------------------------------
//setcurrentHome
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

            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
    }

    private static void initConfigDevice(String token) {
        tokens = token;
    }

    private static long getCurrentHomeId() {

        Log.d("Results", "getCurrentHomeId called");
        HomeBean currentHome = getCurrentHome();
        if (null == currentHome) {
            return -1;
        }
        return currentHome.getHomeId();
    }

    //setcurrentHome
    //----------------------------------------------
    //activate the device
    public static void newMultiActivator(String ssid, String password, String token, Context mContext, long tomeout) {

        mTuyaActivator = TuyaHomeSdk.getActivatorInstance().newMultiActivator(new ActivatorBuilder()
                .setSsid(ssid)
                .setContext(mContext)
                .setPassword(password)
                .setActivatorModel(TY_EZ)
                .setTimeOut(tomeout)
                .setToken(token).setListener(new ITuyaSmartActivatorListener() {
                    @Override
                    public void onError(String s, String s1) {

                    }

                    @Override
                    public void onActiveSuccess(DeviceBean gwDevResp) {

                    }

                    @Override
                    public void onStep(String s, Object o) {

                    }
                }));


    }

    //activate the device
    //------------------------------------------------
//get all list
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

                TuyaHomeSdk.newHomeInstance(homeId).getHomeDetail(new ITuyaHomeResultCallback() {
                    @Override
                    public void onSuccess(HomeBean bean) {

                        updateDeviceData(bean.getDeviceList());
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
                        updateDeviceData(bean.getDeviceList());
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {

                    }
                });
            }
        });
    }

    private static void updateDeviceData(List<DeviceBean> deviceList) {


    }

    //get all list
//------------------------------------------------------
//getonclick
    public static void onDeviceClick(DeviceBean deviceBean, Context context) {
        if (!deviceBean.getIsOnline()) {
//            showDevIsNotOnlineTip(deviceBean);
            return;
        }
        onItemClick(deviceBean, context);
    }

    private static void onItemClick(DeviceBean devBean, Context context) {
        if (devBean == null) {
            return;
        }
        if (devBean.getProductId().equals("4eAeY1i5sUPJ8m8d")) {
//            Intent intent = new Intent(mActivity, SwitchActivity.class);
//            intent.putExtra(SwitchActivity.INTENT_DEVID, devBean.getDevId());
//            mActivity.startActivity(intent);
        } else {
            gotoDeviceCommonActivity(devBean, context);
        }

    }

    private static void gotoDeviceCommonActivity(DeviceBean devBean, Context context) {

        gotoCameraPanel(context, HOME_ID, devBean);

        TuyaCameraPanelSDK.setCustomDeviceInfoListener(new DeviceInfoListener() {
            @Override
            public void onDeviceInfoClick(String deviceId) {
            }
        });

        TuyaCameraPanelSDK.setCustomFeedbackListener(new FeedbackListener() {

            @Override
            public void onFeedbackClick(String deviceId) {
            }
        });
    }

    private static void gotoCameraPanel(Context context, long homeId, DeviceBean devBean) {

        if (null == devBean || null == devBean.getProductBean()) {
            return;
        }
        //判断是什么面板
        if ("RN".equals(devBean.getProductBean().getUiInfo().getType())) {
            if (context instanceof Activity) {
                gotoRNCameraPanel((Activity) context, homeId, devBean.getDevId());
            }
        } else if ("NA".equals(devBean.getProductBean().getUiInfo().getType())) {
            if (context instanceof Activity) {
                gotoNativeCameraPanel(context, homeId, devBean.getDevId());
            }
        }
    }

    private static void gotoNativeCameraPanel(Context context, long homeId, String deviceId) {
        TuyaCameraPanelSDK.setCurrentHomeId(homeId);
        Log.d("Results", "Activity_Trigerred");

        //方式一
        Bundle bundle = new Bundle();
        bundle.putString("extra_camera_uuid", deviceId);
        Intent intent = new Intent(context, CameraPanelActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private static void gotoRNCameraPanel(Activity context, long homeId, String deviceId) {
        Log.d("Results", "callback_trigerred");
        TuyaCameraPanelSDK.setCurrentHomeId(homeId);
        TuyaPanelSDK.getPanelInstance().gotoPanelViewControllerWithDevice(context, homeId, deviceId, new ITuyaPanelLoadCallback() {
            @Override
            public void onStart(String s) {
            }

            @Override
            public void onError(String s, int i, String s1) {
            }

            @Override
            public void onSuccess(String s) {
            }

            @Override
            public void onProgress(String s, int i) {
            }
        });
    }
    //getonclick
    //----------------------------------------------------------
}
