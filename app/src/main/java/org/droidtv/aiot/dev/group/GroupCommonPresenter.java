package org.droidtv.aiot.dev.group;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;

import com.tuya.smart.android.demo.R;
import org.droidtv.aiot.dev.base.utils.DialogUtil;
import org.droidtv.aiot.dev.base.utils.ProgressUtil;
import org.droidtv.aiot.dev.base.utils.ToastUtil;
import org.droidtv.aiot.dev.device.IDeviceCommonView;
import com.tuya.smart.android.device.bean.SchemaBean;
import com.tuya.smart.android.device.enums.ModeEnum;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.sdk.api.ITuyaGroup;
import com.tuya.smart.sdk.bean.DeviceBean;
import com.tuya.smart.sdk.bean.GroupBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by letian on 16/8/4.
 */
public class GroupCommonPresenter extends BasePresenter {
    public static final String INTENT_GROUP_ID = "intent_group_id";
    private final IDeviceCommonView mView;
    private Context mContext;
    private ITuyaGroup mTuyaGroup;
    private long mGroupId;

    public GroupCommonPresenter(Context context, IDeviceCommonView commonView) {
        mContext = context;
        mView = commonView;
        initData();
    }

    private void initList() {
        List<SchemaBean> schemaList = getSchemaList();
        mView.setSchemaData(schemaList);
    }

    private void initData() {
        mGroupId = ((Activity) mContext).getIntent().getLongExtra(INTENT_GROUP_ID, -1);

        mTuyaGroup = TuyaHomeSdk.newGroupInstance(mGroupId);
    }


    public List<SchemaBean> getSchemaList() {
        GroupBean groupBean = TuyaHomeSdk.getDataInstance().getGroupBean(mGroupId);
        List<String> devIds = groupBean.getDevIds();
        if (devIds == null || devIds.size() == 0) return new ArrayList<>();
        DeviceBean dev = TuyaHomeSdk.getDataInstance().getDeviceBean(devIds.get(0));
        if (dev == null) return new ArrayList<>();
        Map<String, SchemaBean> schemaMap = dev.getSchemaMap();
        List<SchemaBean> schemaBeanArrayList = new ArrayList<>();
        for (Map.Entry<String, SchemaBean> entry : schemaMap.entrySet()) {
            schemaBeanArrayList.add(entry.getValue());
        }

        Collections.sort(schemaBeanArrayList, new Comparator<SchemaBean>() {
            @Override
            public int compare(SchemaBean lhs, SchemaBean rhs) {
                return Integer.valueOf(lhs.getId()) < Integer.valueOf(rhs.getId()) ? -1 : 1;
            }
        });
        return schemaBeanArrayList;
    }

    public String getDevName() {
        GroupBean groupBean = TuyaHomeSdk.getDataInstance().getGroupBean(mGroupId);
        if (groupBean == null) return "";
        return groupBean.getName();
    }

    public void getData() {
        initList();
    }

    public void onItemClick(SchemaBean schemaBean) {
        if (schemaBean == null) return;
        if (!TextUtils.equals(schemaBean.getMode(), ModeEnum.RO.getType())) {
            Intent intent = new Intent(mContext, GroupDpSendActivity.class);
            intent.putExtra(GroupDpSendPresenter.INTENT_DPID, schemaBean.getId());
            intent.putExtra(GroupDpSendPresenter.INTENT_GROUPID, mGroupId);
            mContext.startActivity(intent);
        }
    }

    public void renameDevice() {
        DialogUtil.simpleInputDialog(mContext, mContext.getString(R.string.rename), getDevName(), false, new DialogUtil.SimpleInputDialogInterface() {
            @Override
            public void onPositive(DialogInterface dialog, String inputText) {
                int limit = mContext.getResources().getInteger(R.integer.change_device_name_limit);
                if (inputText.length() > limit) {
                    ToastUtil.showToast(mContext, R.string.ty_modify_device_name_length_limit);
                } else {
                    renameTitleToServer(inputText);
                }
            }

            @Override
            public void onNegative(DialogInterface dialog) {

            }
        });
    }

    private void renameTitleToServer(final String titleName) {
        ProgressUtil.showLoading(mContext, R.string.loading);
        mTuyaGroup.renameGroup(titleName, new IResultCallback() {
            @Override
            public void onError(String code, String error) {
                ProgressUtil.hideLoading();
                ToastUtil.showToast(mContext, error);
            }

            @Override
            public void onSuccess() {
                ProgressUtil.hideLoading();
                mView.updateTitle(titleName);
            }
        });
    }

    public void removeDevice() {
        ProgressUtil.showLoading(mContext, R.string.loading);
        mTuyaGroup.dismissGroup(new IResultCallback() {
            @Override
            public void onError(String code, String error) {
                ProgressUtil.hideLoading();
                ToastUtil.showToast(mContext, error);
            }

            @Override
            public void onSuccess() {
                ProgressUtil.hideLoading();
                ((Activity) mContext).finish();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
