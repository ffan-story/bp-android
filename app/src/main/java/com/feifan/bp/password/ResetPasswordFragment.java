package com.feifan.bp.password;

import android.os.Bundle;

import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.feifan.bp.Constants;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ui.BaseFragment;

/**
 * 重置密码
 */
public class ResetPasswordFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "ResetPasswordFragment";
    private EditText mOldPwd;
    private EditText mNewPwd;
    private EditText mConfirmPwd;
    private Button mConfirmBtn;
    private boolean mIsConfirmEnable = true;

    public ResetPasswordFragment() {
        Bundle args = new Bundle();
        args.putString(Constants.EXTRA_KEY_TITLE, Utils.getString(R.string.settings_change_password_text));
        setArguments(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mIsConfirmEnable = true;
        View rootView = inflater.inflate(R.layout.fragment_reset_password, container, false);
        mOldPwd = (EditText) rootView.findViewById(R.id.et_old_password);
        mNewPwd = (EditText) rootView.findViewById(R.id.et_new_password);
        mConfirmPwd = (EditText) rootView.findViewById(R.id.et_confirm_password);
        mConfirmBtn = (Button) rootView.findViewById(R.id.btn_confirm);

        mOldPwd.addTextChangedListener(mTextWatcher);
        mNewPwd.addTextChangedListener(mTextWatcher);
        mConfirmPwd.addTextChangedListener(mTextWatcher);
        mConfirmBtn.setOnClickListener(this);
        return rootView;
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mIsConfirmEnable = true;
            Editable editable = mNewPwd.getText();
            int len = editable.length();
            if (len > Constants.PASSWORD_MAX_LENGTH) {
                if (len == Constants.PASSWORD_MAX_LENGTH + 1) {
                    Utils.showShortToast(getActivity(), R.string.error_message_text_password_length_max,
                            Gravity.CENTER);
                }
                int selEndIndex = Selection.getSelectionEnd(editable);
                String str = editable.toString();
                //截取新字符串
                String newStr = str.substring(0, Constants.PASSWORD_MAX_LENGTH);
                mNewPwd.setText(newStr);
                editable = mNewPwd.getText();

                //新字符串的长度
                int newLen = editable.length();
                //旧光标位置超过字符串长度
                if (selEndIndex > newLen) {
                    selEndIndex = editable.length();
                }
                //设置新光标所在的位置
                Selection.setSelection(editable, selEndIndex);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                if (!mIsConfirmEnable) {
                break;
            }
                String oldPwd = mOldPwd.getText().toString();
                String newPwd = mNewPwd.getText().toString();
                String confirmPwd = mConfirmPwd.getText().toString();
                if (TextUtils.isEmpty(oldPwd)) {
                    Utils.showShortToast(getActivity(), R.string.error_message_text_old_password_empty, Gravity.CENTER);
                    return;
                } else if (TextUtils.isEmpty(newPwd)) {
                    Utils.showShortToast(getActivity(), R.string.error_message_text_new_password_empty, Gravity.CENTER);
                    return;
                } else if (TextUtils.isEmpty(confirmPwd)) {
                    Utils.showShortToast(getActivity(), R.string.error_message_text_confirm_password_empty, Gravity.CENTER);
                    return;
                } else if (!newPwd.equals(confirmPwd)) {
                    Utils.showShortToast(getActivity(), R.string.error_message_text_password_different, Gravity.CENTER);
                    return;
                } else if (newPwd.length() < Constants.PASSWORD_MIN_LENGTH) {
                    Utils.showShortToast(getActivity(), R.string.error_message_text_password_length, Gravity.CENTER);
                    return;
                } else if (newPwd.length() > Constants.PASSWORD_MAX_LENGTH) {
                    Utils.showShortToast(getActivity(), R.string.error_message_text_password_length_max, Gravity.CENTER);
                    return;
                }
                mIsConfirmEnable = false;
                PasswordCtrl.resetPassword(oldPwd, newPwd, new Response.Listener<PasswordModel>() {
                    @Override
                    public void onResponse(PasswordModel passwordModel) {
                        getActivity().onBackPressed();
                        Utils.showShortToast(getActivity(), getString(R.string.reset_pwd_suc));
                    }
                });

                break;
        }
    }

}
