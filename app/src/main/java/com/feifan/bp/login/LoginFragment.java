package com.feifan.bp.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.Response.Listener;
import com.feifan.bp.Constants;
import com.feifan.bp.LaunchActivity;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.PlatformState;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.Statistics;
import com.feifan.bp.UserProfile;
import com.feifan.bp.Utils;
import com.feifan.bp.base.network.BaseModel;
import com.feifan.bp.base.network.JsonRequest;
import com.feifan.bp.base.network.response.ToastErrorListener;
import com.feifan.bp.base.ui.BaseFragment;
import com.feifan.bp.biz.receiptsrecord.ReceiptsFragment;
import com.feifan.bp.password.ForgetPasswordFragment;

/**
 * 登录界面Fragment
 */
public class LoginFragment extends BaseFragment {

    private static final String TAG = LoginFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    /**
     * 创建LogFragment实例的静态工厂
     *
     * @return LogFragment实例
     */
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(Constants.EXTRA_KEY_TITLE, Utils.getString(R.string.login_login_text));
        fragment.setArguments(args);
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
        final EditText account = (EditText) v.findViewById(R.id.login_account);
        account.setText(PlatformState.getInstance().getCurrentPhone());
        final EditText password = (EditText) v.findViewById(R.id.login_password);
        v.findViewById(R.id.login_forget_password).setOnClickListener(new View.OnClickListener() {
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

                if (TextUtils.isEmpty(account.getText()) && TextUtils.isEmpty(password.getText())) {
                    Utils.showShortToastSafely(R.string.error_message_text_login_empty);
                    return;
                }
                if (TextUtils.isEmpty(password.getText())) {
                    Utils.showShortToastSafely(R.string.error_message_text_login_password_empty);
                    return;
                }
                if (TextUtils.isEmpty(account.getText())) {
                    Utils.showShortToastSafely(R.string.error_message_text_phone_number_empty);
                    return;
                }
                String accountStr = account.getText().toString();
                String passwordStr = password.getText().toString();
                PlatformState.getInstance().setCurrentPhone(accountStr);

                try {
                    Utils.checkPhoneNumber(accountStr);
                    showProgressBar(true);
                    UserCtrl.login(accountStr, passwordStr, new Listener<UserModel>() {
                        @Override
                        public void onResponse(UserModel userModel) {
                            if(getActivity() == null || getView() == null){
                                return;
                            }
                            hideProgressBar();
                            final UserProfile profile = UserProfile.getInstance();
                            PlatformState.getInstance().onAccountChange();
                            profile.setUid(userModel.uid);
                            profile.setUser(userModel.user);
                            profile.setAuthRangeId(userModel.authRangeId);
                            profile.setAuthRangeType(userModel.authRangeType);
                            profile.setAgId(userModel.agId);
                            profile.setToken(userModel.token);
                            profile.setLoginToken(userModel.loginToken);
                            profile.setMerchantId(userModel.merchantId);
                            profile.setPlazaId(userModel.plazaId);
//                            profile.setCookie(userModel.cookie);
                            JsonRequest.updateRedundantParams(profile);
                            Statistics.updateClientData(profile);
                            loginConfirm(userModel.token);
                            UserCtrl.checkPermissions(userModel.uid, new Listener<AuthListModel>() {
                                @Override
                                public void onResponse(AuthListModel authListModel) {
                                    profile.setAuthList(authListModel.toJsonString());
                                    profile.setHistoryUrl(authListModel.historyUrl);
                                    profile.setRightString(authListModel.rightString);

                                    // 通知界面跳转
//                                    Bundle args = new Bundle();
//                                    args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, LoginFragment.class.getName());
//                                    mListener.onFragmentInteraction(args);
                                    if(isAdded()) {
                                        String payFlowId = "";
                                        Intent intent = getActivity().getIntent();
                                        if(intent != null) {
                                            payFlowId = getActivity().getIntent().getStringExtra(ReceiptsFragment.PAY_FLOW_ID);
                                        }
                                        if(!TextUtils.isEmpty(payFlowId)){
                                            ReceiptsFragment.start(payFlowId);
                                        }else {
                                            startActivity(LaunchActivity.buildIntent(getActivity()));
                                        }
                                        getActivity().finish();
                                    }
                                }
                            });
                        }
                    }, new ToastErrorListener(){
                        @Override
                        protected void preDisposeError() {
                            hideProgressBar();
                        }
                    });
                } catch (Throwable throwable) {
                    hideProgressBar();
                    Utils.showShortToastSafely(R.string.error_message_text_phone_number_illegal);
                }

            }
        });
        return v;
    }

    private void loginConfirm(String token){
        UserCtrl.loginConfirm(token, new Listener<BaseModel>() {
            @Override
            public void onResponse(BaseModel model) {

            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((PlatformTopbarActivity)getActivity()).getToolbar().setNavigationIcon(null);
    }

    @Override
    public void onAttach(Context Context) {
        super.onAttach(Context);
        try {
            mListener = (OnFragmentInteractionListener) Context;
        } catch (ClassCastException e) {
            throw new ClassCastException(Context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

}
