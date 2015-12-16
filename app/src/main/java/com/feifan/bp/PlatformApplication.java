package com.feifan.bp;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.feifan.bp.network.JsonRequest;
import com.feifan.statlib.FmsConstants;

import java.util.List;

/**
 * Created by xuchunlei on 15/6/17.
 */
public class PlatformApplication extends Application {

    private int selectPos = 0;//门店默认选择

    public int getSelectPos() {
        return selectPos;
    }

    public void setSelectPos(int selectPos) {
        this.selectPos = selectPos;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PlatformState.setApplicationContext(this);
        PlatformState.getInstance();
        UserProfile.getInstance().initialize(this);
        JsonRequest.updateRedundantParams(UserProfile.getInstance());
        Statistics.updateClientData(UserProfile.getInstance());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
