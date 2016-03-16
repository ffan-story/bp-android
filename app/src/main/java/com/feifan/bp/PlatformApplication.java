package com.feifan.bp;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;

import com.bp.crash.BPCrashConfig;
import com.feifan.bp.network.JsonRequest;
import com.ffan.xg.XGPushManger;
import com.wanda.crashsdk.pub.FeifanCrashManager;

/**
 * Created by xuchunlei on 15/6/17.
 */
public class PlatformApplication extends Application {

    private int selectPos = 0;//门店默认选择
    private XGPushManger mXGPushManger;
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
        mXGPushManger = XGPushManger.getInstance(this);
        // 允许WebView进行调试
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        startCrashManager();
        onAccountChange();
        // 更新当前显示的Activity
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                PlatformState.getInstance().setCurrentActivity(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
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

    public void onAccountChange(){
        String phone = PlatformState.getInstance().getCurrentPhone();
        if(!TextUtils.isEmpty(phone)){
            mXGPushManger.unRegister();
            mXGPushManger.registerApp(phone);
        }
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
