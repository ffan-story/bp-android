package com.feifan.bp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;

import com.android.volley.VolleyError;
import com.feifan.bp.account.AccountManager;
import com.feifan.bp.base.BaseActivity;
import com.feifan.bp.home.CheckVersionModel;
import com.feifan.bp.home.CheckVersionRequest;
import com.feifan.bp.login.PermissionModel;
import com.feifan.bp.login.UserCtrl;
import com.feifan.bp.net.BaseRequest;
import com.feifan.bp.net.BaseRequestProcessListener;
import com.feifan.bp.net.HttpEngine;

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

        int versionBeforeUpdate = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).
                getInt(PREF_VERSION_BEFORE_UPDATE, -1);
        if (versionBeforeUpdate != -1 && versionBeforeUpdate < BuildConfig.VERSION_CODE) {
            AccountManager.instance(this).clear();
        }

        checkVersion();

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

        VersionUpdateRequest.Params parameters = BaseRequest.newParams(VersionUpdateRequest.Params.class);
        parameters.setCurrVersionCode(Utils.getVersionCode(this) + "");
        HttpEngine.Builder builder = HttpEngine.Builder.newInstance(this);
        builder.setRequest(new VersionUpdateRequest(parameters,
                new BaseRequestProcessListener<VersionUpdateModel>(this, false, false) {
                    @Override
                    public void onResponse(VersionUpdateModel model) {

                        final int mustUpdate = model.getMustUpdate();
                        final String url = model.getVersionUrl();
                        final int versionCode = model.getVersionCode();

                        AccountManager manager = AccountManager.instance(SplashActivity.this);
                        int uid = manager.getUid();

                        if (uid == Constants.NO_INTEGER) {
                            int sVersionCode = SplashActivity.this.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).
                                    getInt(PREF_VERSION_CODE, -1);

                            if (versionCode <= sVersionCode) {
                                startActivity(LaunchActivity.buildIntent(SplashActivity.this));
                                finish();
                                return;
                            }
                            if (mustUpdate == VersionUpdateModel.UPDATE_NO_UPDATE) {
                                startActivity(LaunchActivity.buildIntent(SplashActivity.this));
                                finish();
                            } else {
                                AlertDialog.Builder b = new AlertDialog.Builder(SplashActivity.this);
                                b.setTitle(getString(R.string.version_update_title));

                                b.setPositiveButton(getString(R.string.btn_version_update_new), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SharedPreferences.Editor editor = SplashActivity.this.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
                                        editor.putInt(PREF_VERSION_BEFORE_UPDATE, BuildConfig.VERSION_CODE);
                                        editor.apply();
                                        startActivity(Utils.getSystemBrowser(url));
                                        finish();
                                    }
                                });
                                if (mustUpdate == VersionUpdateModel.UPDATE_NO_FORCE) {
                                    b.setMessage(getString(R.string.version_update_normal));
                                    b.setNegativeButton(getString(R.string.btn_version_update_later), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            SharedPreferences.Editor editor = SplashActivity.this.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
                                            editor.putInt(PREF_VERSION_CODE, versionCode);
                                            editor.apply();
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

                            UserCtrl.checkPermission(SplashActivity.this, manager.getUid() + "",
                                    new BaseRequestProcessListener<PermissionModel>(SplashActivity.this, false, false) {
                                        @Override
                                        public void onResponse(PermissionModel permissionModel) {
                                            AccountManager manager = AccountManager.instance(SplashActivity.this);
                                            manager.setPermissionList(permissionModel.getPermissionList());
                                            manager.setPermissionUrlMap(permissionModel.getUrlMap());

                                            int sVersionCode = SplashActivity.this.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).
                                                    getInt(PREF_VERSION_CODE, -1);

                                            if (versionCode <= sVersionCode) {
                                                startActivity(LaunchActivity.buildIntent(SplashActivity.this));
                                                finish();
                                                return;
                                            }
                                            if (mustUpdate == VersionUpdateModel.UPDATE_NO_UPDATE) {
                                                startActivity(LaunchActivity.buildIntent(SplashActivity.this));
                                                finish();
                                            } else {
                                                AlertDialog.Builder b = new AlertDialog.Builder(SplashActivity.this);
                                                b.setTitle(getString(R.string.version_update_title));

                                                b.setPositiveButton(getString(R.string.btn_version_update_new), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        SharedPreferences.Editor editor = SplashActivity.this.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
                                                        editor.putInt(PREF_VERSION_BEFORE_UPDATE, BuildConfig.VERSION_CODE);
                                                        editor.apply();
                                                        startActivity(Utils.getSystemBrowser(url));
                                                        finish();
                                                    }
                                                });
                                                if (mustUpdate == VersionUpdateModel.UPDATE_NO_FORCE) {
                                                    b.setMessage(getString(R.string.version_update_normal));
                                                    b.setNegativeButton(getString(R.string.btn_version_update_later), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            SharedPreferences.Editor editor = SplashActivity.this.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
                                                            editor.putInt(PREF_VERSION_CODE, versionCode);
                                                            editor.apply();
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

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        LogUtil.i(TAG, "onErrorResponse() error=" + (error != null ? error.getMessage() : "null"));
                        startActivity(LaunchActivity.buildIntent(SplashActivity.this));
                        finish();
                    }
                })).build().start();
    }
}
