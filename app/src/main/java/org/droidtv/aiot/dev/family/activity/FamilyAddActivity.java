package org.droidtv.aiot.dev.family.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.smart.android.demo.R;
import org.droidtv.aiot.dev.base.activity.BaseActivity;
import org.droidtv.aiot.dev.base.app.Constant;
import org.droidtv.aiot.dev.base.utils.ActivityUtils;
import org.droidtv.aiot.dev.base.utils.CollectionUtils;
import org.droidtv.aiot.dev.family.item.FamilyAddEditNameItem;
import org.droidtv.aiot.dev.family.item.FamilyDefaultRoomItem;
import org.droidtv.aiot.dev.family.presenter.FamilyAddPresenter;
import org.droidtv.aiot.dev.family.recyclerview.adapter.BaseRVAdapter;
import org.droidtv.aiot.dev.family.recyclerview.item.BaseItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FamilyAddActivity extends BaseActivity implements IFamilyAddView {

    @BindView(R.id.family_add_rv)
    RecyclerView rv;
    @BindArray(R.array.family_add_default_name_array)
    String[] defaultFamilyArray;

    public static final String KEY_EMPTY_FAMILY="empty_family_key";

    private FamilyAddPresenter mPresenter;

    private BaseRVAdapter<BaseItem> mAdapter;

    Unbinder unbinder;
    private FamilyAddEditNameItem familyAddEditNameItem;
    private List<FamilyDefaultRoomItem> defaultRoomItemList;
    private boolean isEmptyFamily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_add);
        unbinder = ButterKnife.bind(this);
        isEmptyFamily = getIntent().getBooleanExtra(KEY_EMPTY_FAMILY, false);
        initToolbar();
        initTitle();
        initAdapter();
        initListener();
        initPresenter();
    }

    private void initPresenter() {
        mPresenter = new FamilyAddPresenter(this);
    }

    private void initListener() {

    }

    private void initAdapter() {
        mAdapter = new BaseRVAdapter<>();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(mAdapter);


        familyAddEditNameItem = new FamilyAddEditNameItem();
        mAdapter.addItemView(familyAddEditNameItem);

        defaultRoomItemList = new ArrayList<>();
        for (String familyName : defaultFamilyArray) {
            FamilyDefaultRoomItem item = new FamilyDefaultRoomItem(familyName);
            defaultRoomItemList.add(item);
            mAdapter.addItemView(item);
        }

    }

    private void initTitle() {
        setTitle(getString(R.string.family_add_title));
        mToolBar.setTitleTextColor(Color.WHITE);
        setDisplayHomeAsUpEnabled();
        setMenu(R.menu.toolbar_save, new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_menu_save) {
                    checkParams();
                }
                return false;
            }
        });
    }


    private void checkParams() {
        String homeName = familyAddEditNameItem.getEditText();
        if (TextUtils.isEmpty(homeName)) {
            showToast(R.string.family_add_name_check_tip);
            return;
        }
        List<String> checkRoomList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(defaultRoomItemList)) {
            for (FamilyDefaultRoomItem familyAddDefaultItem : defaultRoomItemList) {
                if (familyAddDefaultItem.isCheck()) {
                    checkRoomList.add(familyAddDefaultItem.getData());
                }
            }
        }
        if (CollectionUtils.isEmpty(checkRoomList)) {
            showToast(R.string.family_add_name_check_tip);
            return;
        }
        showLoading();
        mPresenter.addFamily(homeName,checkRoomList);
    }


    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void doSaveSuccess() {
        hideLoading();
        showToast(R.string.save_success);
        finishActivity();
        if (isEmptyFamily){
            Constant.finishActivity();
            ActivityUtils.gotoHomeActivity(this);
        }
    }

    @Override
    public void doSaveFailed() {
        hideLoading();
        showToast(R.string.save_failed);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
        mPresenter.onDestroy();
    }
}
