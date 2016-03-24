package com.feifan.bp.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.feifan.bp.BuildConfig;
import com.feifan.bp.Constants;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.PlatformState;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.Statistics;
import com.feifan.bp.UserProfile;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ui.BaseFragment;
import com.feifan.bp.login.LoginFragment;
import com.feifan.bp.login.UserCtrl;
import com.feifan.bp.base.network.BaseModel;
import com.feifan.bp.password.ResetPasswordFragment;
import com.feifan.bp.settings.feedback.FeedBackFragment;
import com.feifan.bp.settings.helpcenter.HelpCenterFragment;
import com.feifan.statlib.FmsAgent;

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
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(Constants.EXTRA_KEY_TITLE, Utils.getString(R.string.home_settings_text));
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

    private long mLastClickTime = 0;

    @Override
    public void onClick(View v) {
        if(!isAdded()) {
            return;
        }
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
                break;
            case R.id.settings_change_password:
                Bundle args = new Bundle();
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, SettingsFragment.class.getName());
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, ResetPasswordFragment.class.getName());
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TITLE, getString(R.string.reset_password));
                mListener.onFragmentInteraction(args);
                break;

            case R.id.settings_advice_feedback:
                //统计埋点 意见反馈
                FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_SETTING_FEEDBACK);
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
                        Utils.showShortToastSafely(R.string.settings_clear_cache_finished_text);
                    }
                }, 1000);

                break;
            case R.id.settings_exit:
                //调用登出接口
                UserCtrl.logout(new Listener<BaseModel>() {
                    @Override
                    public void onResponse(BaseModel model) {

                    PlatformState.getInstance().reset();
                    UserProfile.getInstance().clear();
                    if(isAdded()){
                        PlatformTopbarActivity.startActivityFromOther(getActivity(), LoginFragment.class.getName(), getActivity().getString(R.string.login_login_text));
                        getActivity().finish();
                    }
//                        Bundle args = new Bundle();
//                        args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, SettingsFragment.class.getName());
//                        args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, LaunchActivity.class.getName());

//                        mListener.onFragmentInteraction(args);

//                        Executors.newSingleThreadExecutor().execute(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                getActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Bundle args = new Bundle();
//                                        args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, SettingsFragment.class.getName());
//                                        args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, LaunchActivity.class.getName());
//                                        mListener.onFragmentInteraction(args);
//                                    }
//                                });
//                            }
//                        });
                    }
                });
                break;
        }
    }

    private void checkVersion() {
        HomeCtrl.checkVersion(new Listener<VersionModel>() {
            @Override
            public void onResponse(VersionModel versionModel) {
                if (getActivity() == null || getActivity().isFinishing()) {
                    return;
                }
                final int mustUpdate = versionModel.getMustUpdate();
                final String url = versionModel.getVersionUrl();

                if (mustUpdate == VersionModel.UPDATE_NO_UPDATE) {
                    Utils.showShortToast(getActivity(), R.string.settings_check_update_none);
                } else {
                    AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                    b.setTitle(getString(R.string.version_update_title));

                    b.setPositiveButton(getString(R.string.btn_version_update_new), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(Utils.getSystemBrowser(url));
                            PlatformState.getInstance().reset();
                            UserProfile.getInstance().clear();
                        }
                    });
                    if (mustUpdate == VersionModel.UPDATE_NO_FORCE) {
                        b.setMessage(getString(R.string.version_update_normal));
                        b.setNegativeButton(getString(R.string.btn_version_update_later), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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
                Utils.showShortToastSafely(R.string.settings_check_update_none);
            }
        });
    }
}
