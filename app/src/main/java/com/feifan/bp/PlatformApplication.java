package com.feifan.bp;

import android.app.Application;

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
