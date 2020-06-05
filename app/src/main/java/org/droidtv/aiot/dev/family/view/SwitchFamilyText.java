package org.droidtv.aiot.dev.family.view;

import android.content.Context;

import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tuya.smart.android.demo.R;
import org.droidtv.aiot.dev.family.FamilyManager;
import org.droidtv.aiot.dev.family.event.EventCurrentHomeChange;
import com.tuya.smart.home.sdk.bean.HomeBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SwitchFamilyText extends FrameLayout {

    private View rootView;

    private TextView familyText;


    public SwitchFamilyText(@NonNull Context context) {
        this(context, null);
    }

    public SwitchFamilyText(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchFamilyText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        EventBus.getDefault().register(this);
        rootView = View.inflate(getContext(), R.layout.view_family_switch, this);
        familyText = rootView.findViewById(R.id.view_family_switch_text);
        initData();

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FamilyDropActionSheet dropActionSheet = new FamilyDropActionSheet(getContext());
                dropActionSheet.showAtLocation(Gravity.TOP,0,0);
            }
        });
    }

    private void initData() {
        HomeBean currentHome = FamilyManager.getInstance().getCurrentHome();
        if (null != currentHome) {
            familyText.setText(currentHome.getName());
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeviceChange(EventCurrentHomeChange currentHomeChange) {
        HomeBean homeBean = currentHomeChange.getHomeBean();
        if (null == homeBean) {
            return;
        }
        familyText.setText(homeBean.getName());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }
}
