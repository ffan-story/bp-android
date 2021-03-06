package com.feifan.bp.password;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response.Listener;
import com.feifan.bp.Constants;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ui.BaseFragment;
import com.feifan.bp.base.network.BaseModel;
import com.feifan.bp.util.SecureUtil;
import com.feifan.bp.widget.CountDownButton;

/**
 * 忘记密码界面
 *
 */
public class ForgetPasswordFragment extends BaseFragment implements View.OnClickListener {

    private EditText mPhoneNum;
    private EditText mSmsCode;
    private CountDownButton mCountDownButton;
    private Button mBtnConfirm;
    String mAuthCode;
    String mKeyCode;
    String mRadomSmsCode = "";

    public ForgetPasswordFragment() {
        Bundle args = new Bundle();
        args.putString(Constants.EXTRA_KEY_TITLE, Utils.getString(R.string.reset_password));
        setArguments(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forget_password, container, false);
        mPhoneNum = (EditText) rootView.findViewById(R.id.et_phone_num);
        mSmsCode = (EditText) rootView.findViewById(R.id.sms_code);
        mCountDownButton = (CountDownButton) rootView.findViewById(R.id.get_sms_code);
        mBtnConfirm = (Button) rootView.findViewById(R.id.btn_confirm);
        mCountDownButton.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(this);
        return rootView;
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setTitle(R.string.reset_password);
        toolbar.setNavigationIcon(R.mipmap.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(!isAdded()){
            return;
        }
        final String phone = mPhoneNum.getText().toString();
        String smsCode = mSmsCode.getText().toString();
        switch (view.getId()) {

            case R.id.btn_confirm:
                if (!isCheckedMobile(phone)) {
                    return;
                }

                if (TextUtils.isEmpty(smsCode)) {
                    Utils.showShortToast(getActivity(), getString(R.string.sms_error));
                    return;
                }
                String newPhone = "";
                if (!TextUtils.isEmpty(mKeyCode)) {
                    try {
                        newPhone = SecureUtil.encode(phone, mKeyCode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (TextUtils.isEmpty(newPhone)) {
                    Utils.showShortToast(getActivity().getApplicationContext(), getString(R.string.error_message_text_phone_number_empty));
                    return;
                }

                PasswordCtrl.forgetPassword(newPhone, mAuthCode, mKeyCode, new Listener() {
                    @Override
                    public void onResponse(Object o) {
                        if (isAdded()) {
                            Utils.showShortToast(getActivity(), getString(R.string.new_password_sended));
                            getActivity().finish();
                        }
                    }
                });
                break;
            case R.id.get_sms_code:
                if (!isCheckedMobile(phone)) {
                    return;
                }

                PasswordCtrl.checkPhoneNumExist(phone, new Listener<PasswordModel>() {
                    @Override
                    public void onResponse(PasswordModel passwordModel) {
                        mCountDownButton.startCountDown();
                        mAuthCode = passwordModel.getAuthCode();
                        mKeyCode = passwordModel.getKey();
                        getSmsCode();
                        PasswordCtrl.sendSMSCode(phone, mRadomSmsCode, new Listener<BaseModel>() {
                            @Override
                            public void onResponse(BaseModel baseModel) {
                                Utils.showShortToastSafely(R.string.sms_sended);
                            }
                        });
                    }
                });
                break;
        }
    }

    public boolean isCheckedMobile(String phone) {
        if (TextUtils.isEmpty(phone)) {
            Utils.showShortToastSafely(R.string.error_message_text_phone_number_empty);
            return false;
        }
        try {
            Utils.checkPhoneNumber(phone);
        } catch (Throwable throwable) {
            Utils.showShortToastSafely(R.string.error_message_text_phone_number_illegal);
            return false;
        }
        return true;
    }

    public void getSmsCode() {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            sBuffer.append((int) (Math.random() * 10));
        }
        mRadomSmsCode = sBuffer.toString();
    }
}
