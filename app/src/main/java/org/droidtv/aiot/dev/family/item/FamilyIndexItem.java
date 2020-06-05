package org.droidtv.aiot.dev.family.item;

import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import org.droidtv.aiot.dev.family.recyclerview.item.BaseItem;
import org.droidtv.aiot.dev.family.recyclerview.item.BaseViewHolder;
import com.tuya.smart.home.sdk.bean.HomeBean;

import butterknife.BindView;

public class FamilyIndexItem extends BaseItem<HomeBean> {

    @BindView(R.id.recycler_family_item_name)
    TextView nameTv;


    public FamilyIndexItem(HomeBean data) {
        super(data);
    }

    @Override
    public int getViewType() {
        return 0;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.recycler_family_index_item;
    }

    @Override
    public void onReleaseViews(BaseViewHolder holder, int sectionKey, int sectionViewPosition) {
        nameTv.setText("");
    }

    @Override
    public void onSetViewsData(BaseViewHolder holder, int sectionKey, int sectionViewPosition) {
        nameTv.setText(getData().getName());
    }
}
