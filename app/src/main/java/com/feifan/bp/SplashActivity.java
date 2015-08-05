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

                        boolean isUpdate = false;
                        final int verisonCode = model.getVersionCode();
                        if (verisonCode > Utils.getVersionCode(SplashActivity.this)) {
                            isUpdate = true;
                        }

                        final int mustUpdate = model.getMustUpdate();
                        final String url = model.getVersionUrl();

                        if (isUpdate) {
                            AlertDialog.Builder b = new AlertDialog.Builder(SplashActivity.this);
                            b.setTitle(getString(R.string.version_update_title));
                            b.setMessage(getString(R.string.version_update_message));
                            b.setPositiveButton(getString(R.string.common_confirm), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(Utils.getSystemBrowser(url));
                                    finish();
                                }
                            });
                            b.setNegativeButton(getString(R.string.common_cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    if (mustUpdate == 0) {
                                        startActivity(LaunchActivity.buildIntent(SplashActivity.this));
                                    }
                                    finish();
                                }
                            });
                            b.setCancelable(false);
                            b.create().show();

                        } else {
                            startActivity(LaunchActivity.buildIntent(SplashActivity.this));
                            finish();
                        }


                    }
                })).build().start();
    }
}
