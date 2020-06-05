package org.droidtv.aiot.dev.group;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tuya.smart.android.demo.R;
import org.droidtv.aiot.dev.base.adapter.BaseListArrayAdapter;
import org.droidtv.aiot.dev.base.adapter.ViewHolder;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.bean.DeviceBean;
import com.tuya.smart.sdk.bean.GroupBean;

import java.util.List;

/**
 * Created by letian on 16/8/27.
 */
public class GroupDeviceAdapter extends BaseListArrayAdapter<GroupBean> {

    private static final String TAG = "GroupDeviceAdapter";

    public GroupDeviceAdapter(Context context, int resource, List<GroupBean> data) {
        super(context, resource, data);
    }

    @Override
    protected ViewHolder view2Holder(View view) {
        return new InnerViewHolder(view);
    }

    @Override
    protected void bindView(ViewHolder viewHolder, GroupBean data) {
        viewHolder.initData(data);
    }

    public class InnerViewHolder extends ViewHolder<GroupBean> {
        public ImageView connect;
        public ImageView deviceIcon;
        public TextView device;

        public InnerViewHolder(View contentView) {
            super(contentView);
            connect = (ImageView) contentView.findViewById(R.id.iv_device_list_dot);
            deviceIcon = (ImageView) contentView.findViewById(R.id.iv_device_icon);
            device = (TextView) contentView.findViewById(R.id.tv_device);
        }

        @Override
        public void initData(GroupBean groupBean) {
            Picasso.with(mContext).load(groupBean.getIconUrl()).into(deviceIcon);
            final int resId;
            if (isGroupOnline(groupBean)) {
                if (groupBean.isShare()) {
                    resId = R.drawable.ty_devicelist_share_green;
                } else {
                    resId = R.drawable.ty_devicelist_dot_green;
                }
            } else {
                if (groupBean.isShare()) {
                    resId = R.drawable.ty_devicelist_share_gray;
                } else {
                    resId = R.drawable.ty_devicelist_dot_gray;
                }
            }
            connect.setImageResource(resId);
            device.setText(groupBean.getName());
        }

    }

    private boolean isGroupOnline(GroupBean groupBean) {
        List<String> devices = groupBean.getDevIds();
        if (devices == null || devices.size() <= 0) {
            return false;
        }

        for (String devId : devices) {
            DeviceBean dev = TuyaHomeSdk.getDataInstance().getDeviceBean(devId);
            if (dev != null && dev.getIsOnline()) {
                return true;
            }
        }
        return false;
    }

}

