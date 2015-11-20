package com.feifan.bp.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.feifan.bp.BuildConfig;
import com.feifan.bp.feedback.FeedBackFragment;
import com.feifan.bp.LaunchActivity;
import com.feifan.bp.helpcenter.HelpCenterFragment;
import com.feifan.bp.util.LogUtil;
import com.feifan.bp.PlatformState;
import com.feifan.bp.R;

import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.Utils;
import com.feifan.bp.UserProfile;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        v.findViewById(R.id.settings_help_center).setOnClickListener(this);
        v.findViewById(R.id.settings_change_password).setOnClickListener(this);
        v.findViewById(R.id.settings_clear_cache).setOnClickListener(this);
        v.findViewById(R.id.settings_exit).setOnClickListener(this);
        TextView upgrade = (TextView) v.findViewById(R.id.settings_check_upgrade);
        upgrade.setText(getString(R.string.settings_check_upgrade_format, BuildConfig.VERSION_NAME));
        upgrade.setOnClickListener(this);
        v.findViewById(R.id.settings_advice_feedback).setOnClickListener(this);

        return v;
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setTitle(R.string.home_settings_text);
//        toolbar.setNavigationIcon(R.mipmap.ic_left_arrow);
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
        switch (v.getId()) {
            case R.id.settings_help_center:
                Bundle helpBundle = new Bundle();
                helpBundle.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, SettingsFragment.class.getName());
                helpBundle.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, HelpCenterFragment.class.getName());
                mListener.onFragmentInteraction(helpBundle);
                break;

            case R.id.settings_check_upgrade:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                checkVersion();
//               HomeCtrl.checkVersion(getActivity(), new BaseRequestProcessListener<CheckVersionModel>(getActivity()) {
//                   @Override
//                   public void onResponse(CheckVersionModel checkVersionModel) {
//                       LogUtil.i(TAG, checkVersionModel.toString());
//                       if (checkVersionModel.getVersionCode() > BuildConfig.VERSION_CODE) {
//                           Bundle args = new Bundle();
//                           args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, SettingsFragment.class.getName());
//                           args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, checkVersionModel.getVersionUrl());
//                           mListener.onFragmentInteraction(args);
//                       } else {
//                           Utils.showShortToast(getActivity(), R.string.settings_check_update_none);
//                       }
//                   }
//               });
                break;
            case R.id.settings_change_password:
                Bundle args = new Bundle();
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, SettingsFragment.class.getName());
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, ResetPasswordFragment.class.getName());
                mListener.onFragmentInteraction(args);
                break;
            case R.id.settings_advice_feedback:
                //add by tianjun 2015.10.27
                Bundle bundle = new Bundle();
                bundle.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, SettingsFragment.class.getName());
                bundle.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, FeedBackFragment.class.getName());
                mListener.onFragmentInteraction(bundle);
                break;
            //end.
            case R.id.settings_clear_cache:
                showProgressBar(false);
                PlatformState.getInstance().clearCache();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressBar();
                        Utils.showShortToast(getActivity(), R.string.settings_clear_cache_finished_text);
                    }
                }, 1000);

                break;
            case R.id.settings_exit:
                Executors.newSingleThreadExecutor().execute(new Runnable() {

                    @Override
                    public void run() {
                        PlatformState.getInstance().reset();
                        UserProfile.getInstance().clear();
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

    private static final String PREFERENCE_NAME = "wanda_bp";
    private static final String PREF_VERSION_CODE = "pref_version_code";
    private static final String PREF_VERSION_BEFORE_UPDATE = "pref_version_before_update";

    private void checkVersion() {
        HomeCtrl.checkVersion(new Listener<VersionModel>() {
            @Override
            public void onResponse(VersionModel versionModel) {
                if (getActivity() == null || getActivity().isFinishing()) {
                    return;
                }

                final int mustUpdate = versionModel.getMustUpdate();
                final String url = versionModel.getVersionUrl();
                final int versionCode = versionModel.getVersionCode();

                if (mustUpdate == VersionModel.UPDATE_NO_UPDATE) {
                    Utils.showShortToast(getActivity(), R.string.settings_check_update_none);
                } else {
                    AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                    b.setTitle(getString(R.string.version_update_title));

                    b.setPositiveButton(getString(R.string.btn_version_update_new), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = getActivity().
                                    getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
                            editor.putInt(PREF_VERSION_BEFORE_UPDATE, BuildConfig.VERSION_CODE);
                            editor.apply();
                            startActivity(Utils.getSystemBrowser(url));
                        }
                    });
                    if (mustUpdate == VersionModel.UPDATE_NO_FORCE) {
                        b.setMessage(getString(R.string.version_update_normal));
                        b.setNegativeButton(getString(R.string.btn_version_update_later), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences(PREFERENCE_NAME,
                                        Context.MODE_PRIVATE).edit();
                                editor.putInt(PREF_VERSION_CODE, versionCode);
                                editor.apply();
                                dialog.dismiss();
                            }
                        });
                    } else {
                        b.setMessage(getString(R.string.version_update_force));
                        b.setNegativeButton(getString(R.string.common_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                    }

                    b.setCancelable(false);
                    b.create().show();
                }
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.i(TAG, "onErrorResponse() error=" + volleyError != null ? volleyError.getMessage() : "null");
                Utils.showShortToast(getActivity(), R.string.settings_check_update_none);
            }
        });
    }
}
