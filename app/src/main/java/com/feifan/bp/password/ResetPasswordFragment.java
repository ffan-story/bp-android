package com.feifan.bp.password;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.Response;
import com.feifan.bp.Constants;
import com.feifan.bp.LaunchActivity;
import com.feifan.bp.LogUtil;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.PlatformState;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.home.SettingsFragment;
import com.feifan.bp.login.LoginFragment;
import com.feifan.bp.login.UserCtrl;
import com.feifan.bp.login.UserModel;
import com.feifan.bp.login.UserProfile;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResetPasswordFragment} interface
 * to handle interaction events.
 * Use the {@link ResetPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResetPasswordFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "ResetPasswordFragment";
    private EditText mOldPwd;
    private EditText mNewPwd;
    private EditText mConfirmPwd;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ResetPasswordFragment.
     */
    public static ResetPasswordFragment newInstance() {
        ResetPasswordFragment fragment = new ResetPasswordFragment();
        return fragment;
    }

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_reset_password, container, false);
        mOldPwd = (EditText) rootView.findViewById(R.id.et_old_password);
        mNewPwd = (EditText) rootView.findViewById(R.id.et_new_password);
        mConfirmPwd = (EditText) rootView.findViewById(R.id.et_confirm_password);
        rootView.findViewById(R.id.btn_confirm).setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            toolbar.setTitle(R.string.settings_change_password_text);
            toolbar.setNavigationIcon(R.mipmap.ic_left_arrow);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //notify activity here.
                    if (mListener != null) {
                        Bundle b = new Bundle();
                        b.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, ResetPasswordFragment.class.getName());
                        b.putInt(OnFragmentInteractionListener.INTERATION_KEY_TYPE, OnFragmentInteractionListener.TYPE_NAVI_CLICK);
                        mListener.onFragmentInteraction(b);
                    }
                }
            });
        }
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
        switch (view.getId()) {
            case R.id.btn_confirm:
                String oldPwd = mOldPwd.getText().toString();
                String newPwd = mNewPwd.getText().toString();
                String confirmPwd = mConfirmPwd.getText().toString();
                if (TextUtils.isEmpty(oldPwd)) {
                    Utils.showShortToast(R.string.error_message_text_old_password_empty, Gravity.CENTER);
                    return;
                } else if (TextUtils.isEmpty(newPwd)) {
                    Utils.showShortToast(R.string.error_message_text_new_password_empty, Gravity.CENTER);
                    return;
                } else if (TextUtils.isEmpty(confirmPwd)) {
                    Utils.showShortToast(R.string.error_message_text_confirm_password_empty, Gravity.CENTER);
                    return;
                }  else if (!newPwd.equals(confirmPwd)) {
                    Utils.showShortToast(R.string.error_message_text_password_different, Gravity.CENTER);
                    return;
                }else if(newPwd.length()<8){
                    Utils.showShortToast(R.string.error_message_text_password_length, Gravity.CENTER);
                    return;
                }

                PasswordCtrl.resetPassword(oldPwd, newPwd, new Response.Listener<PasswordModel>() {
                    @Override
                    public void onResponse(PasswordModel model) {
                       // getActivity().onBackPressed();
                        Utils.showShortToast(getString(R.string.reset_pwd_suc));
                    }
                });
                break;
        }
    }

}
