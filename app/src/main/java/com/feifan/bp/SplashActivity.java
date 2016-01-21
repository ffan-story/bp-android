package com.feifan.bp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.feifan.bp.base.BaseActivity;
import com.feifan.bp.base.PlatformBaseActivity;
import com.feifan.bp.home.HomeCtrl;
import com.feifan.bp.home.VersionModel;
import com.feifan.bp.login.AuthListModel;
import com.feifan.bp.login.UserCtrl;
import com.feifan.bp.util.LogUtil;
import com.feifan.statlib.FmsAgent;

/**
 * 欢迎界面
 *
 * Created by maning on 15/7/29.
 */
public class SplashActivity extends PlatformBaseActivity {

    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        setContentView(R.layout.activity_splash);
        checkVersion();

        //统计埋点----用户启动APP
        FmsAgent.onEvent(getApplicationContext(), Statistics.USER_OPEN_APP);
    }

    private void checkVersion() {

        HomeCtrl.checkVersion(new Listener<VersionModel>() {
            @Override
            public void onResponse(VersionModel versionModel) {
                final int mustUpdate = versionModel.getMustUpdate();
                final String url = versionModel.getVersionUrl();

                UserProfile manager = UserProfile.getInstance();
                int uid = manager.getUid();

                if (uid == Constants.NO_INTEGER) {
                    if (mustUpdate == VersionModel.UPDATE_NO_UPDATE) {
                        startActivity(LaunchActivity.buildIntent(SplashActivity.this));
                        finish();
                    } else {
                        AlertDialog.Builder b = new AlertDialog.Builder(SplashActivity.this);
                        b.setTitle(getString(R.string.version_update_title));

                        b.setPositiveButton(getString(R.string.btn_version_update_new), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PlatformState.getInstance().reset();
                                UserProfile.getInstance().clear();
                                startActivity(Utils.getSystemBrowser(url));
                                finish();
                            }
                        });
                        if (mustUpdate == VersionModel.UPDATE_NO_FORCE) {
                            b.setMessage(getString(R.string.version_update_normal));
                            b.setNegativeButton(getString(R.string.btn_version_update_later), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
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
                                    if (mustUpdate == VersionModel.UPDATE_NO_UPDATE) {
                                        startActivity(LaunchActivity.buildIntent(SplashActivity.this));
                                        finish();
                                    } else {
                                        AlertDialog.Builder b = new AlertDialog.Builder(SplashActivity.this);
                                        b.setTitle(getString(R.string.version_update_title));

                                        b.setPositiveButton(getString(R.string.btn_version_update_new), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                PlatformState.getInstance().reset();
                                                UserProfile.getInstance().clear();
                                                startActivity(Utils.getSystemBrowser(url));
                                                finish();
                                            }
                                        });
                                        if (mustUpdate == VersionModel.UPDATE_NO_FORCE) {
                                            b.setMessage(getString(R.string.version_update_normal));
                                            b.setNegativeButton(getString(R.string.btn_version_update_later), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
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
