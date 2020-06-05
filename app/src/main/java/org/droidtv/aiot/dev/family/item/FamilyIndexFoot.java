package org.droidtv.aiot.dev.family.item;

import android.view.View;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import org.droidtv.aiot.dev.family.recyclerview.item.BaseFoot;
import org.droidtv.aiot.dev.family.recyclerview.item.BaseViewHolder;

import butterknife.BindView;

public class FamilyIndexFoot extends BaseFoot<String> {
    @BindView(R.id.family_index_add_txt)
    TextView addTxt;

    public FamilyIndexFoot() {
        super("");
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.recycler_family_index_foot;
    }

    @Override
    public void onReleaseViews(BaseViewHolder holder, int sectionKey, int sectionFootPosition) {

    }

    @Override
    public void onSetViewsData(BaseViewHolder holder, int sectionKey, int sectionFootPosition) {
        addTxt.setOnClickListener(mListener);
    }


    private View.OnClickListener mListener;

    public void setOnAddFamilyListener(View.OnClickListener listener) {
        mListener = listener;
    }
}
