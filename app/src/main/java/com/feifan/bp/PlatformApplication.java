package com.feifan.bp;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;

import com.feifan.bp.base.network.JsonRequest;

/**
 * 定制的应用类
 * <pre>
 *     在这里初始化一些全局变量，尽量不在这里进行方法扩展
 * </pre>
 *
 * Created by xuchunlei on 15/6/17.
 */
public class PlatformApplication extends Application {

    private int selectPos = 0;//门店默认选择

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
        PlatformState.getInstance().startCrashManager();
        PlatformState.getInstance().onAccountChange();
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

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
