package com.feifan.bp;

import android.app.Application;
import android.os.Build;
import android.webkit.WebView;

import com.bp.crash.BPCrashConfig;
import com.feifan.bp.network.JsonRequest;
import com.wanda.crashsdk.pub.FeifanCrashManager;

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

        // 允许WebView进行调试
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        startCrashManager();
    }


    private void startCrashManager() {
        try {
            BPCrashConfig crashConfig = new BPCrashConfig();
            final UserProfile profile = UserProfile.getInstance();
            if(profile != null && profile.getUid() != 0) {
                crashConfig.setUid(String.valueOf(profile.getUid()));
            }
            FeifanCrashManager.getInstance().init(getApplicationContext(), crashConfig);
            FeifanCrashManager.getInstance().start();
        } catch (com.wanda.crashsdk.exception.IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
