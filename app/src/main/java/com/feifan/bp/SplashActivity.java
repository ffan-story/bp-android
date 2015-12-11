package com.feifan.bp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.feifan.bp.base.BaseActivity;
import com.feifan.bp.home.HomeCtrl;
import com.feifan.bp.home.VersionModel;
import com.feifan.bp.login.AuthListModel;
import com.feifan.bp.login.UserCtrl;
import com.feifan.bp.util.LogUtil;
import com.feifan.statlib.FmsAgent;

/**
 * Created by maning on 15/7/29.
 */
public class SplashActivity extends BaseActivity {

    private static final String TAG = "SplashActivity";

    private static final int SHOW_TIME = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        setContentView(R.layout.activity_splash);

//        版本更新BUG罪魁祸首!!!!!!!!!!!!!!!
//        int versionBeforeUpdate = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).
//                getInt(PREF_VERSION_BEFORE_UPDATE, -1);
//        if (versionBeforeUpdate != -1 && versionBeforeUpdate < BuildConfig.VERSION_CODE) {
//            UserProfile.getInstance().clear();
//        }

        checkVersion();

        //统计埋点----用户启动APP
        FmsAgent.onEvent(this, Statistics.USER_OPEN_APP);
//        new Handler() {}.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(LaunchActivity.buildIntent());
//                finish();
//            }
//        }, SHOW_TIME);
    }

    @Override
    protected boolean isShowToolbar() {
        return false;
    }

    private static final String PREFERENCE_NAME = "wanda_bp";
    private static final String PREF_VERSION_CODE = "pref_version_code";
    private static final String PREF_VERSION_BEFORE_UPDATE = "pref_version_before_update";

    private void checkVersion() {

        HomeCtrl.checkVersion(new Listener<VersionModel>() {
            @Override
            public void onResponse(VersionModel versionModel) {
                final int mustUpdate = versionModel.getMustUpdate();
                final String url = versionModel.getVersionUrl();
//                final int versionCode = versionModel.getVersionCode();

                UserProfile manager = UserProfile.getInstance();
                int uid = manager.getUid();

                if (uid == Constants.NO_INTEGER) {
//                    int sVersionCode = SplashActivity.this.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).
//                            getInt(PREF_VERSION_CODE, -1);

//                    if (versionCode <= sVersionCode) {
//                        startActivity(LaunchActivity.buildIntent(SplashActivity.this));
//                        finish();
//                        return;
//                    }
                    if (mustUpdate == VersionModel.UPDATE_NO_UPDATE) {
                        startActivity(LaunchActivity.buildIntent(SplashActivity.this));
                        finish();
                    } else {
                        AlertDialog.Builder b = new AlertDialog.Builder(SplashActivity.this);
                        b.setTitle(getString(R.string.version_update_title));

                        b.setPositiveButton(getString(R.string.btn_version_update_new), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                SharedPreferences.Editor editor = SplashActivity.this.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
//                                editor.putInt(PREF_VERSION_BEFORE_UPDATE, BuildConfig.VERSION_CODE);
//                                editor.apply();
                                startActivity(Utils.getSystemBrowser(url));
                                finish();
                            }
                        });
                        if (mustUpdate == VersionModel.UPDATE_NO_FORCE) {
                            b.setMessage(getString(R.string.version_update_normal));
                            b.setNegativeButton(getString(R.string.btn_version_update_later), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    SharedPreferences.Editor editor = SplashActivity.this.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
//                                    editor.putInt(PREF_VERSION_CODE, versionCode);
//                                    editor.apply();
                                    dialog.dismiss();
                                    startActivity(LaunchActivity.buildIntent(SplashActivity.this));
                                    finish();
                                }
                            });
                        } else {
                            b.setMessage(getString(R.string.version_update_force));
                            b.setNegativeButton(getString(R.string.common_cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                        }

                        b.setCancelable(false);
                        b.create().show();
                    }
                } else {
                    UserCtrl.checkPermissions(manager.getUid(),
                            new Listener<AuthListModel>() {
                                @Override
                                public void onResponse(AuthListModel authListModel) {
                                    UserProfile manager = UserProfile.getInstance();
                                    manager.setAuthList(authListModel.toJsonString());

//                                    int sVersionCode = SplashActivity.this.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).
//                                            getInt(PREF_VERSION_CODE, -1);
//
//                                    if (versionCode <= sVersionCode) {
//                                        startActivity(LaunchActivity.buildIntent(SplashActivity.this));
//                                        finish();
//                                        return;
//                                    }
                                    if (mustUpdate == VersionModel.UPDATE_NO_UPDATE) {
                                        startActivity(LaunchActivity.buildIntent(SplashActivity.this));
                                        finish();
                                    } else {
                                        AlertDialog.Builder b = new AlertDialog.Builder(SplashActivity.this);
                                        b.setTitle(getString(R.string.version_update_title));

                                        b.setPositiveButton(getString(R.string.btn_version_update_new), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
//                                                SharedPreferences.Editor editor = SplashActivity.this.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
//                                                editor.putInt(PREF_VERSION_BEFORE_UPDATE, BuildConfig.VERSION_CODE);
//                                                editor.apply();
                                                startActivity(Utils.getSystemBrowser(url));
                                                finish();
                                            }
                                        });
                                        if (mustUpdate == VersionModel.UPDATE_NO_FORCE) {
                                            b.setMessage(getString(R.string.version_update_normal));
                                            b.setNegativeButton(getString(R.string.btn_version_update_later), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
//                                                    SharedPreferences.Editor editor = SplashActivity.this.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
//                                                    editor.putInt(PREF_VERSION_CODE, versionCode);
//                                                    editor.apply();
                                                    dialog.dismiss();
                                                    startActivity(LaunchActivity.buildIntent(SplashActivity.this));
                                                    finish();
                                                }
                                            });
                                        } else {
                                            b.setMessage(getString(R.string.version_update_force));
                                            b.setNegativeButton(getString(R.string.common_cancel), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    finish();
                                                }
                                            });
                                        }

                                        b.setCancelable(false);
                                        b.create().show();
                                    }
                                }
                            });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.i(TAG, "onErrorResponse() error=" + (volleyError != null ? volleyError.getMessage() : "null"));
                startActivity(LaunchActivity.buildIntent(SplashActivity.this));
                finish();
            }
        });
    }
}
