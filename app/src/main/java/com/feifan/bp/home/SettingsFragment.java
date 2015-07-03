package com.feifan.bp.home;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response.Listener;
import com.feifan.bp.Constants;
import com.feifan.bp.LogUtil;
import com.feifan.bp.PlatformState;
import com.feifan.bp.R;

import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.home.Model.CenterModel;
import com.feifan.bp.home.Model.MerchantModel;
import com.feifan.bp.home.Model.StoreModel;
import com.feifan.bp.login.UserProfile;
import com.feifan.bp.password.ForgetPasswordFragment;
import com.feifan.bp.password.ResetPasswordFragment;

import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UserCenterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        v.findViewById(R.id.settings_change_password).setOnClickListener(this);
        v.findViewById(R.id.merchant_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newSingleThreadExecutor().execute(new Runnable() {

                    @Override
                    public void run() {
                        PlatformState.getInstance().getUserProfile().clear();
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Bundle args = new Bundle();
                                args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, SettingsFragment.class.getName());
                                mListener.onFragmentInteraction(args);
                            }
                        });
                    }
                });
            }
        });

        UserProfile profile = PlatformState.getInstance().getUserProfile();
        if(profile.getAuthRangeType().equals(Constants.AUTH_RANGE_TYPE_MERCHANT) ) {
            HomeCtrl.fetchMerchantDetail(profile.getAuthRangeId(), new Listener<MerchantModel>() {
                @Override
                public void onResponse(MerchantModel merchantModel) {
                    Log.e("xuchunlei", merchantModel.toString());
                    setupLogo(merchantModel);
                }
            });
        } else if(profile.getAuthRangeType().equals(Constants.AUTH_RANGE_TYPE_STORE)) {
            HomeCtrl.fetchStoreDetail(profile.getAuthRangeId(), new Listener<StoreModel>() {
                @Override
                public void onResponse(StoreModel storeModel) {
                    Log.e("xuchunlei", storeModel.toString());
                    setupLogo(storeModel);

                }
            });
        }


        return v;
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

    private void setupLogo(CenterModel model) {
        Bundle args = new Bundle();
        args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, SettingsFragment.class.getName());
        args.putParcelable(OnFragmentInteractionListener.INTERATION_KEY_LOGO, model);
        mListener.onFragmentInteraction(args);
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.settings_check_upgrade:
               // TODO call check upgrade here with mListener
               LogUtil.w(SettingsFragment.class.getSimpleName(), "check upgrade is not implemented");
               break;
           case R.id.settings_change_password:
               Bundle args = new Bundle();
               args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, ResetPasswordFragment.class.getName());
               mListener.onFragmentInteraction(args);
               break;
       }
    }
}
