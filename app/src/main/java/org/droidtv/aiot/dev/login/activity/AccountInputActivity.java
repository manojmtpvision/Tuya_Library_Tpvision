package org.droidtv.aiot.dev.login.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tuya.smart.android.common.utils.ValidatorUtil;
import com.tuya.smart.android.demo.R;
import org.droidtv.aiot.dev.base.activity.BaseActivity;
import org.droidtv.aiot.dev.base.utils.ActivityUtils;
import org.droidtv.aiot.dev.base.utils.ToastUtil;
import org.droidtv.aiot.dev.login.IAccountInputView;
import org.droidtv.aiot.dev.login.presenter.AccountInputPresenter;

public class AccountInputActivity extends BaseActivity implements TextWatcher, IAccountInputView {

    public static final String EXTRA_ACCOUNT_INPUT_MODE = "extra_account_input_mode";
    public static final int MODE_REGISTER = 0;
    public static final int MODE_PASSWORD_FOUND = 1;

    private int mMode = 0;
    String mystring,privacy_str;
    private AccountInputPresenter mPresenter;

    private int mAccountType = AccountConfirmActivity.PLATFORM_PHONE;
    private TextView mCountryName;
    private EditText mEtAccount;
    private Button mNextStepBtn;
    private TextView terms,privacy;
    EditText password,re_password;
    CheckBox checkBox;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.bt_next) {
//                if (mCountryName.getText().toString().trim().length() == 0){
//                    Toast.makeText(getApplicationContext(),"Country code is empty",Toast.LENGTH_LONG).show();
//                }else if (mEtAccount.getText().toString().trim().length() == 0){
//                    Toast.makeText(getApplicationContext(),"Please enter Mobile number or Emailid",Toast.LENGTH_LONG).show();
//
//                }else if (password.getText().toString().trim().length() == 0){
//                    Toast.makeText(getApplicationContext(),"Please enter Password",Toast.LENGTH_LONG).show();
//                }else if (re_password.getText().toString().trim().length() == 0){
//                    Toast.makeText(getApplicationContext(),"Please re-enter Password",Toast.LENGTH_LONG).show();
//
//                }
//                else if (password.getText().toString()!=re_password.getText().toString()){
//                    Toast.makeText(getApplicationContext(),"Password mis matching",Toast.LENGTH_LONG).show();
//
//                }
//                else {
//                    if (mAccountType == AccountConfirmActivity.PLATFORM_PHONE
//                            && mCountryName.getText().toString().contains("+86")
//                            && mEtAccount.getText().length() != 11) {
//                        ToastUtil.shortToast(AccountInputActivity.this, getString(R.string.ty_phone_num_error));
//                        return;
//                    }
//                    if (mAccountType == AccountConfirmActivity.PLATFORM_EMAIL) {
//                        mPresenter.gotoNext(mAccountType);
//                    } else if (mAccountType == AccountConfirmActivity.PLATFORM_PHONE) {
//                        mPresenter.gotoNext(mAccountType);
//                    }
//                }

            } else if (v.getId() == R.id.country_name) {
                mPresenter.selectCountry();
            }
        }
    };

    public static void gotoAccountInputActivity(Activity activity, int mode, int requestCode) {
        Intent intent = new Intent(activity, AccountInputActivity.class);
        intent.putExtra(EXTRA_ACCOUNT_INPUT_MODE, mode);
        ActivityUtils.startActivityForResult(activity, intent, requestCode, 0, false);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_input);
        initData();
        initToolbar();
        getToolBar().setVisibility(View.GONE);
        initTitle();
        hideTitleBarLine();
        initView();
        initPresenter();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    private void initData() {
        mMode = getIntent().getIntExtra(EXTRA_ACCOUNT_INPUT_MODE, MODE_REGISTER);
    }

    private void initPresenter() {
        mPresenter = new AccountInputPresenter(this, this);
    }

    private void initView() {
        password=findViewById(R.id.password);
        re_password=findViewById(R.id.re_password);
        checkBox=findViewById(R.id.checkBox);
        mCountryName = (TextView) findViewById(R.id.country_name);
        mCountryName.setOnClickListener(mOnClickListener);
        mNextStepBtn = (Button) findViewById(R.id.bt_next);
      //  mNextStepBtn.setOnClickListener(mOnClickListener);
//        bt_next=findViewById(R.id.bt_next);
        mNextStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCountryName.getText().toString().trim().length() == 0){
                    Toast.makeText(getApplicationContext(),"Country code is empty",Toast.LENGTH_LONG).show();
                }else if (mEtAccount.getText().toString().trim().length() == 0){
                    Toast.makeText(getApplicationContext(),"Please enter Mobile number or Emailid",Toast.LENGTH_LONG).show();

                }else if (password.getText().toString().trim().length() == 0){
                    Toast.makeText(getApplicationContext(),"Please enter Password",Toast.LENGTH_LONG).show();
                }else if (re_password.getText().toString().trim().length() == 0){
                    Toast.makeText(getApplicationContext(),"Please re-enter Password",Toast.LENGTH_LONG).show();

                }
                else if (!password.getText().toString().equals(re_password.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Password mis matching",Toast.LENGTH_LONG).show();

                }else if (!checkBox.isChecked()){
                    Toast.makeText(getApplicationContext(),"Password Accept Privacy policy",Toast.LENGTH_LONG).show();


                }
                else {
                    SharedPreferences.Editor editor = getSharedPreferences("passwords", MODE_PRIVATE).edit();
                    editor.putString("pass", re_password.getText().toString());
                    editor.apply();
                    if (mAccountType == AccountConfirmActivity.PLATFORM_PHONE
                            && mCountryName.getText().toString().contains("+86")
                            && mEtAccount.getText().length() != 11) {
                        ToastUtil.shortToast(AccountInputActivity.this, getString(R.string.ty_phone_num_error));
                        return;
                    }
                    if (mAccountType == AccountConfirmActivity.PLATFORM_EMAIL) {
                        mPresenter.gotoNext(mAccountType);
                    } else if (mAccountType == AccountConfirmActivity.PLATFORM_PHONE) {
                        mPresenter.gotoNext(mAccountType);
                    }
                }
            }
        });
//        mNextStepBtn.setEnabled(false);
        mEtAccount = (EditText) findViewById(R.id.et_account);
        mEtAccount.addTextChangedListener(this);
         mystring=new String("Terms of use");
        SpannableString content = new SpannableString(mystring);
        content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
        privacy_str=new String("Privacy policy");
        SpannableString content1 = new SpannableString(privacy_str);
        content1.setSpan(new UnderlineSpan(), 0, privacy_str.length(), 0);
        terms=findViewById(R.id.terms);
        privacy=findViewById(R.id.privacy);
        terms.setText(content);
        privacy.setText(content1);

        privacy=findViewById(R.id.privacy);
    }

    private void initTitle() {
        switch (mMode) {
            case MODE_REGISTER:
                setTitle(R.string.ty_login_register);
                break;

            case MODE_PASSWORD_FOUND:
                setTitle(R.string.ty_login_forget_keyword_find);
                break;
        }
        setDisplayHomeAsUpEnabled();
    }

    // 输入账号监听
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String userName = mEtAccount.getText().toString();

        if (TextUtils.isEmpty(userName)) {
           // mNextStepBtn.setEnabled(false);
        } else {
            if (ValidatorUtil.isEmail(userName)) {
                // 邮箱
                mAccountType = AccountConfirmActivity.PLATFORM_EMAIL;
               // mNextStepBtn.setEnabled(true);
            } else {
                // 手机号码
                try {
                    Long.valueOf(userName);
                    mAccountType = AccountConfirmActivity.PLATFORM_PHONE;
                   // mNextStepBtn.setEnabled(true);
                } catch (Exception e) {
                   // mNextStepBtn.setEnabled(false);
                }
            }
        }
    }

    @Override
    public void setCountryInfo(String countryName, String countryNum) {
        mCountryName.setText(String.format("%s +%s", countryName, countryNum));
    }

    @Override
    public String getAccount() {
        return mEtAccount.getText().toString();
    }

    @Override
    public int getMode() {
        return mMode;
    }

    @Override
    public boolean needLogin() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }
}
