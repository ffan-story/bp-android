package com.feifan.bp;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;

import com.bp.crash.BPCrashConfig;
import com.feifan.bp.base.network.JsonRequest;
import com.feifan.bp.util.SystemUtil;
import com.feifan.bp.xgpush.NotificationUtils;
import com.feifan.bp.xgpush.XGPushManger;
import com.feifan.bp.xgpush.XGPushUtils;
import com.feifan.statlib.FmsAgent;
import com.tencent.android.tpush.XGNotifaction;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushNotifactionCallback;
import com.wanda.crashsdk.pub.FeifanCrashManager;

/**
 * 定制的应用类
 * <pre>
 *     在这里初始化一些全局变量，尽量不在这里进行方法扩展
 * </pre>
 *
 * Created by xuchunlei on 15/6/17.
 */
public class PlatformApplication extends Application {

    private XGPushManger mXGPushManger;

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
            if (profile != null && profile.getUid() != 0) {
                crashConfig.setUid(String.valueOf(profile.getUid()));
            }
            FeifanCrashManager.getInstance().init(getApplicationContext(), crashConfig);
            FeifanCrashManager.getInstance().start();
        } catch (com.wanda.crashsdk.exception.IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void onAccountChange() {
        final UserProfile profile = UserProfile.getInstance();
        mXGPushManger.unRegister();
        if (profile != null && profile.getUid() != 0) {
            String uid = String.valueOf(profile.getUid());
            if (!TextUtils.isEmpty(uid)) {
                mXGPushManger.registerApp(uid);
                XGPushManager.setNotifactionCallback(new XGPushNotifactionCallback() {
                    @Override
                    public void handleNotify(XGNotifaction xgNotifaction) {
                        if (xgNotifaction == null) {
                            return;
                        }
                        FmsAgent.onEvent(PlatformState.getApplicationContext(), Statistics.FB_PUSHMES_RECEIVE);
                        String title = xgNotifaction.getTitle();
                        if (TextUtils.isEmpty(title)) {
                            title = getString(R.string.app_name);
                        }
                        String content = xgNotifaction.getContent();
                        String customContent = xgNotifaction.getCustomContent();
                        if(TextUtils.isEmpty(customContent)){
                            return;
                        }
                        String payflowid = XGPushUtils.getPayFlowId(customContent, getApplicationContext());
                        String type = XGPushUtils.getTypeId(customContent, getApplicationContext());
                    if (XGPushUtils.PAYFLOW_TYPE.equals(type) && !TextUtils.isEmpty(payflowid)) {
                            if(SystemUtil.isBPActivities(PlatformState.getApplicationContext())) {
                                NotificationUtils.showNotification(PlatformState.getApplicationContext(), XGPushUtils.getPayFlowIntent(payflowid), content, title, R.mipmap.ic_logo,false,false,false,-1);
                            }else {
                                NotificationUtils.showNotification(PlatformState.getApplicationContext(), XGPushUtils.getPayFlowIntents(payflowid), content, title, R.mipmap.ic_logo,false,false,false,-1);
                            }
                        }
                    }
                });
            }
        }
    }

    public void unRegisterPush() {
        if (mXGPushManger != null) {
            mXGPushManger.unRegister();
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
