package com.feifan.bp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.feifan.bp.base.ui.PlatformBaseActivity;
import com.feifan.bp.base.envir.EnvironmentManager;
import com.feifan.bp.home.HomeCtrl;
import com.feifan.bp.home.VersionModel;
import com.feifan.bp.login.AuthListModel;
import com.feifan.bp.login.LoginFragment;
import com.feifan.bp.login.UserCtrl;
import com.feifan.bp.util.LogUtil;
import com.feifan.material.MaterialDialog;
import com.feifan.statlib.FmsAgent;
import com.networkbench.agent.impl.NBSAppAgent;
import com.wanda.crashsdk.pub.FeifanCrashManager;

/**
 * 欢迎界面
 * <p/>
 * Created by maning on 15/7/29.
 */
public class SplashActivity extends PlatformBaseActivity {

    private MaterialDialog mDialog;
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        setContentView(R.layout.activity_splash);

        // 统计埋点----用户启动APP
        FmsAgent.onEvent(getApplicationContext(), Statistics.USER_OPEN_APP);

        //启动动画
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

        //内存/位置权限检查
        onPermissionCheck();

        //统计埋点初始化
        FmsAgent.init(getApplicationContext(), EnvironmentManager.getHostFactory().getFFanApiPrefix() + "mxlog");
        FeifanCrashManager.getInstance().reportActive();

        //权限检查对话框
        mDialog = new MaterialDialog(this);
        mDialog.setTitle(getString(R.string.apply))
                .setMessage(getString(R.string.apply_storage))
                .setCanceledOnTouchOutside(false)
                .setNegativeButton(R.string.common_cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                        SplashActivity.this.finish();
                    }
                })
                .setPositiveButton(R.string.home_settings_text, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
                        startActivity(intent);
                        mDialog.dismiss();
                    }
                });
        // 听云
        if (BuildConfig.CURRENT_ENVIRONMENT.equals(Constants.Environment.PRODUCT)
                && !BuildConfig.DEBUG) {
            NBSAppAgent.setLicenseKey(getString(R.string.tingyun_key)).withLocationServiceEnabled(true).start(this);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        onPermissionCheck();
    }

    private void onPermissionCheck() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
                && Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.MY_PERMISSIONS_REQUEST_STORAGE);
        } else {
            //防闪烁
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkVersion();
                }
            }, 600);
        }
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
//                        startActivity(LaunchActivity.buildIntent(SplashActivity.this));
//                        finish();
                        PlatformTopbarActivity.startActivityFromOther(PlatformState.getApplicationContext(), LoginFragment.class.getName(), getString(R.string.login_login_text));
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Constants.MY_PERMISSIONS_REQUEST_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {//授权通过
                checkVersion();
            } else {//授权拒绝
                mDialog.show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
