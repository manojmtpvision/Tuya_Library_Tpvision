package org.droidtv.aiot.dev.family.item;

import android.view.View;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import org.droidtv.aiot.dev.family.recyclerview.item.BaseEmpty;
import org.droidtv.aiot.dev.family.recyclerview.item.BaseViewHolder;

import butterknife.BindView;

public class FamilyIndexEmpty extends BaseEmpty<String> {

    @BindView(R.id.recycler_family_empty_btn)
    TextView addFamilyBtn;

    public FamilyIndexEmpty() {
        super("");
    }

    @Override
    public void onSetViewsData(BaseViewHolder holder) {
        addFamilyBtn.setOnClickListener(mListener);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.recycler_family_empty;
    }

    private View.OnClickListener mListener;

    public void setOnAddFamilyListener(View.OnClickListener listener) {
        mListener = listener;
    }
}
