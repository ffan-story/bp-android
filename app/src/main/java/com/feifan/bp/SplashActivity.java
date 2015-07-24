package com.feifan.bp;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;

import com.feifan.bp.base.BaseActivity;

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

        new Handler() {}.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(LaunchActivity.buildIntent());
                finish();
            }
        }, SHOW_TIME);
    }

    @Override
    protected boolean isShowToolbar() {
        return false;
    }
}
