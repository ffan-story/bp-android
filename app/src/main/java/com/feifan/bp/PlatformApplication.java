package com.feifan.bp;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Intent;
import android.util.Log;

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
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
