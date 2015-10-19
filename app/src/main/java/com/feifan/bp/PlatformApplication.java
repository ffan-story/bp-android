package com.feifan.bp;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.feifan.bp.network.HttpService;

import java.util.List;

/**
 * Created by xuchunlei on 15/6/17.
 */
public class PlatformApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PlatformState.setApplicationContext(this);
        PlatformState.getInstance();

        HttpService.getInstance().initialize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
