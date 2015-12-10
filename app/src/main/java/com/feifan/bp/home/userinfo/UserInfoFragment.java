package com.feifan.bp.home.userinfo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.feifan.bp.Constants;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.Utils;
import com.feifan.bp.base.BaseFragment;

/**
 * Created by tianjun on 2015-10-26.
 */
public class UserInfoFragment extends BaseFragment implements View.OnClickListener {
    private OnFragmentInteractionListener mListener;
    private LinearLayout mLoginInfoStore;
    private LinearLayout mLoginInfoMerchant;

    public static UserInfoFragment newInstance() {
        UserInfoFragment fragment = new UserInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login_info, container, false);
        mLoginInfoStore = (LinearLayout) rootView.findViewById(R.id.login_info_store);
        mLoginInfoMerchant = (LinearLayout) rootView.findViewById(R.id.login_info_merchant);
        getLoginInfo(rootView);
        return rootView;
    }

    private void getLoginInfo(final View rootView) {
        showProgressBar(true);
            UserProfile manager = UserProfile.getInstance();
            int uid = manager.getUid();
            UserInfoCtrl.getLoginInfo(String.valueOf(uid), new Response.Listener<UserInfoModel>() {
                @Override
                public void onResponse(UserInfoModel userInfoModel) {
                    hideProgressBar();
                    if (userInfoModel.status == Constants.RESPONSE_CODE_SUCCESS) {
                        hideEmptyView();
                        setLoginInfo(rootView, userInfoModel);
                        UserProfile.getInstance().setCityId(userInfoModel.getCityId());
                    } else {
                        showEmptyView();
                        Utils.showShortToast(getActivity(), userInfoModel.msg,
                                Gravity.CENTER);
                    }
                }
            });
    }

    private void setLoginInfo(View rootView, UserInfoModel userInfoModel) {
        ((TextView) rootView.findViewById(R.id.login_info_name)).setText(userInfoModel.getName());
        ((TextView) rootView.findViewById(R.id.login_info_phone)).setText(userInfoModel.getPhone());
        ((TextView) rootView.findViewById(R.id.login_info_identity)).setText(userInfoModel.getIdentity());
        if (userInfoModel.getAuthRangeType().equals("store")) {// store：门店，merchant：商户）
            mLoginInfoStore.setVisibility(View.VISIBLE);
            mLoginInfoMerchant.setVisibility(View.GONE);
            ((TextView) rootView.findViewById(R.id.login_info_belongs_store)).setText(userInfoModel
                    .getStoreViewName());
            if (userInfoModel.getPlazaName().equals("null")) {
                ((TextView) rootView.findViewById(R.id.login_info_belongs_square)).setText(getString(R.string.login_info_empty));
            } else {
                ((TextView) rootView.findViewById(R.id.login_info_belongs_square)).setText(userInfoModel
                        .getPlazaName());
            }
        } else if (userInfoModel.getAuthRangeType().equals("merchant")) {
            mLoginInfoMerchant.setVisibility(View.VISIBLE);
            mLoginInfoStore.setVisibility(View.GONE);
            ((TextView) rootView.findViewById(R.id.login_info_belongs_merchant)).setText(userInfoModel
                    .getMerchantName());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setTitle(R.string.login_info);
        toolbar.setNavigationIcon(R.mipmap.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    Bundle b = new Bundle();
                    b.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM,
                            UserInfoFragment.class.getName());
                    b.putInt(OnFragmentInteractionListener.INTERATION_KEY_TYPE,
                            OnFragmentInteractionListener.TYPE_NAVI_CLICK);
                    mListener.onFragmentInteraction(b);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
