package com.feifan.bp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;

import com.feifan.bp.base.BaseActivity;
import com.feifan.bp.home.CheckVersionModel;
import com.feifan.bp.home.CheckVersionRequest;
import com.feifan.bp.net.BaseRequest;
import com.feifan.bp.net.BaseRequestProcessListener;
import com.feifan.bp.net.HttpEngine;

/**
 * Created by maning on 15/7/29.
 */
public class SplashActivity extends BaseActivity {

    private static final int SHOW_TIME = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        setContentView(R.layout.activity_splash);

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

                        if (mustUpdate == VersionUpdateModel.UPDATE_NO_UPDATE) {
                            startActivity(LaunchActivity.buildIntent(SplashActivity.this));
                            finish();
                        } else {
                            AlertDialog.Builder b = new AlertDialog.Builder(SplashActivity.this);
                            b.setTitle(getString(R.string.version_update_title));

                            b.setPositiveButton(getString(R.string.btn_version_update_new), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(Utils.getSystemBrowser(url));
                                    finish();
                                }
                            });
                            if (mustUpdate == VersionUpdateModel.UPDATE_NO_FORCE) {
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
                })).build().start();
    }
}
