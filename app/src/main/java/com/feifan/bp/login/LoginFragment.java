package com.feifan.bp.login;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.Response;
import com.feifan.bp.LogUtil;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.PlatformState;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.password.ForgetPasswordFragment;

/**
 * 登录界面Fragment
 */
public class LoginFragment extends Fragment {

    private static final String TAG = LoginFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    /**
     * 创建LogFragment实例的静态工厂
     *
     * @return LogFragment实例
     */
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    public LoginFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        final EditText account = (EditText)v.findViewById(R.id.login_account);
        final EditText password = (EditText)v.findViewById(R.id.login_password);
        v.findViewById(R.id.login_forget_password).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // 通知界面跳转
                Bundle args = new Bundle();
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, ForgetPasswordFragment.class.getName());
                mListener.onFragmentInteraction(args);
            }
        });
        v.findViewById(R.id.login_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(account.getText())) {
                    Utils.showShortToast(R.string.error_message_text_phone_number_empty, Gravity.CENTER);
                    return;
                }
                if(TextUtils.isEmpty(password.getText())) {
                    Utils.showShortToast(R.string.error_message_text_password_empty, Gravity.CENTER);
                    return;
                }
                String accountStr = account.getText().toString();
                String passwordStr = password.getText().toString();

                try {
                    Utils.checkPhoneNumber(accountStr);
                    UserCtrl.login(accountStr, passwordStr, new Response.Listener<UserModel>() {
                        @Override
                        public void onResponse(UserModel userModel) {
                            LogUtil.i(TAG, userModel.toString());
                            // 保存登录信息
                            UserProfile profile = PlatformState.getInstance().getUserProfile();
                            profile.setUid(userModel.uid);
                            profile.setUser(userModel.user);
                            profile.setAuthRangeId(userModel.authRangeId);
                            profile.setAuthRangeType(userModel.authRangeType);
                            profile.setAgId(userModel.agId);
                            profile.setLoginToken(userModel.loginToken);

                            // 通知界面跳转
                            Bundle args = new Bundle();
                            args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, LoginFragment.class.getName());
                            mListener.onFragmentInteraction(args);
                        }
                    });
                } catch (Throwable throwable) {
                    Utils.showShortToast(R.string.error_message_text_phone_number_illegal, Gravity.CENTER);
                }

            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
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
//        mListener = null;
    }

}
