package com.feifan.bp.password;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.net.BaseRequestProcessListener;
import com.feifan.bp.util.SecureUtil;
import com.feifan.bp.widget.CountDownButton;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ForgetPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgetPasswordFragment extends BaseFragment implements View.OnClickListener {


    private OnFragmentInteractionListener mListener;
    private EditText mPhoneNum;
    private EditText mSmsCode;
    private CountDownButton mCountDownButton;
    private Button mBtnConfirm;
    String mAuthCode;
    String mKeyCode;
    String mRadomSmsCode = "";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ForgetPasswordFragment.
     */
    public static ForgetPasswordFragment newInstance() {
        ForgetPasswordFragment fragment = new ForgetPasswordFragment();
        return fragment;
    }

    public ForgetPasswordFragment() {

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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onClick(View view) {
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
                    Utils.showShortToast(getActivity(), getString(R.string.error_message_text_phone_number_empty));
                    return;
                }


                PasswordCtrl.forgetPassword(getActivity(), newPhone, mAuthCode, mKeyCode,
                        new BaseRequestProcessListener<PasswordModel>(getActivity()) {
                    @Override
                    public void onResponse(PasswordModel passwordModel2) {
                        Utils.showShortToast(getActivity(), getString(R.string.new_password_sended));
                        getActivity().onBackPressed();
                    }
                });

//                PasswordCtrl.forgetPassword(newPhone, mAuthCode, mKeyCode, new Response.Listener<PasswordModel>() {
//                    @Override
//                    public void onResponse(PasswordModel model) {
//                        Utils.showShortToast(getString(R.string.new_password_sended));
//                        getActivity().onBackPressed();
//                    }
//                });
                break;
            case R.id.get_sms_code:
                if (!isCheckedMobile(phone)) {
                    return;
                }
                PasswordCtrl.checkPhoneNumExist(getActivity(), phone,
                        new BaseRequestProcessListener<PasswordModel>(getActivity()) {
                    @Override
                    public void onResponse(PasswordModel passwordModel2) {
                        mCountDownButton.startCountDown();
                        mAuthCode = passwordModel2.getAuthCode();
                        mKeyCode = passwordModel2.getKey();
                        getSmsCode();
                        PasswordCtrl.sendSMSCode(getActivity(), phone, mRadomSmsCode,
                                new BaseRequestProcessListener<PasswordModel>(getActivity()) {
                                    @Override
                                    public void onResponse(PasswordModel passwordModel2) {
                                        Utils.showShortToast(getActivity(), getString(R.string.sms_sended));
                                    }
                                });
//                        PasswordCtrl.sendSMSCode(phone, mRadomSmsCode, new Response.Listener<PasswordModel>() {
//                            @Override
//                            public void onResponse(PasswordModel model) {
//                                Utils.showShortToast(getString(R.string.sms_sended));
//                            }
//                        });
                    }
                });
//                PasswordCtrl.checkPhoneNumExist(phone, new Response.Listener<PasswordModel>() {
//                    @Override
//                    public void onResponse(PasswordModel model) {
//                        mCountDownButton.startCountDown();
//                        mAuthCode=model.authCode;
//                        mKeyCode=model.key;
//                        getSmsCode();
//                        PasswordCtrl.sendSMSCode(phone, mRadomSmsCode, new Response.Listener<PasswordModel>() {
//                            @Override
//                            public void onResponse(PasswordModel model) {  Utils.showShortToast(getString(R.string.sms_sended));
//                            }
//                        });
//                    }
//                });
                break;
        }
    }

    public boolean isCheckedMobile(String phone) {
        if (TextUtils.isEmpty(phone)) {
            Utils.showShortToast(getActivity(), getString(R.string.error_message_text_phone_number_empty));
            return false;
        }
        try {
            Utils.checkPhoneNumber(getActivity(), phone);
        } catch (Throwable throwable) {
            Utils.showShortToast(getActivity(), R.string.error_message_text_phone_number_illegal, Gravity.CENTER);
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
