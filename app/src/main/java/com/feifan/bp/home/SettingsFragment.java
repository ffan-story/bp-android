package com.feifan.bp.home;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.feifan.bp.BuildConfig;
import com.feifan.bp.Constants;
import com.feifan.bp.LaunchActivity;
import com.feifan.bp.LogUtil;
import com.feifan.bp.PlatformState;
import com.feifan.bp.R;

import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.Utils;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.password.ResetPasswordFragment;

import java.util.concurrent.Executors;

/**
 * 设置界面Fragment
 */
public class SettingsFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "SettingsFragment";


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
        v.findViewById(R.id.settings_exit).setOnClickListener(this);
        TextView upgrade = (TextView)v.findViewById(R.id.settings_check_upgrade);
        upgrade.setText(getString(R.string.settings_check_upgrade_format, BuildConfig.VERSION_NAME));
        upgrade.setOnClickListener(this);

        return v;
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setTitle(R.string.home_settings_text);
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

    private long mLastClickTime = 0;

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.settings_check_upgrade:
               if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
                   return;
               }
               mLastClickTime = SystemClock.elapsedRealtime();
               HomeCtrl.checkUpgrade(new Listener<VersionModel>() {
                   @Override
                   public void onResponse(VersionModel versionModel) {
                       LogUtil.i(TAG, versionModel.toString());
                       if (versionModel.versionCode > BuildConfig.VERSION_CODE) {
                           Bundle args = new Bundle();
                           args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, SettingsFragment.class.getName());
                           args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, versionModel.versionUrl);
                           mListener.onFragmentInteraction(args);
                       } else {
                           Utils.showShortToast(R.string.settings_check_update_none);
                       }
                   }
               });
               break;
           case R.id.settings_change_password:
               Bundle args = new Bundle();
               args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, SettingsFragment.class.getName());
               args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, ResetPasswordFragment.class.getName());
               mListener.onFragmentInteraction(args);
               break;
           case R.id.settings_exit:
               Executors.newSingleThreadExecutor().execute(new Runnable() {

                   @Override
                   public void run() {
                       PlatformState.getInstance().reset();
                       getActivity().runOnUiThread(new Runnable() {

                           @Override
                           public void run() {
                               Bundle args = new Bundle();
                               args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, SettingsFragment.class.getName());
                               args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, LaunchActivity.class.getName());
                               mListener.onFragmentInteraction(args);
                           }
                       });
                   }
               });
               break;
       }
    }
}
