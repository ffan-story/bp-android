package com.feifan.bp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bp.crash.BPCrashConfig;
import com.feifan.bp.base.network.FullTrustManager;
import com.feifan.bp.base.network.HttpsUrlStack;
import com.feifan.bp.util.LogUtil;
import com.feifan.bp.util.SystemUtil;
import com.feifan.bp.xgpush.NotificationUtils;
import com.feifan.bp.xgpush.XGPushManger;
import com.feifan.bp.xgpush.XGPushUtils;
import com.feifan.statlib.FmsAgent;
import com.tencent.android.tpush.XGNotifaction;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushNotifactionCallback;
import com.wanda.crashsdk.pub.FeifanCrashManager;

import java.lang.ref.WeakReference;

/**
 * 状态类
 * <p>
 *     存取平台相关的状态变量
 * </p>
 * Created by xuchunlei on 15/6/17.
 */
public class PlatformState {

    private static final String TAG = "PlatformState";
    // 状态相关的偏好项名称
    private static final String STATE_PREFERENCES_NAME = "state";
    // 偏好项键值－清除缓存标记
    private static final String CLEAR_CACHE_FLAG = "CLEAR_CACHE_FLAG";
    // 偏好项键值－Cookie
    private static final String PREFERENCE_KEY_COOKIE = "COOKIE";

    private static PlatformState INSTANCE;

    private static Context sContext;

    // 网络请求队列
    private RequestQueue mQueue;

    // 上次访问的url地址
    private String mLastUrl;

    // 当前登录手机号
    private String mCurrentPhone;

    // 未读状态集合
    private SparseArray<Boolean> mUnreadMap = new SparseArray<Boolean>();

    // 当前窗口
    private WeakReference<Activity> mCurrentActivity;

    private XGPushManger mXGPushManger;

    private PlatformState(){
        FullTrustManager.trustAll();
        mQueue = Volley.newRequestQueue(sContext, new HttpsUrlStack());
        Log.i(Constants.TAG, "App is running within " + BuildConfig.CURRENT_ENVIRONMENT);
        mXGPushManger = XGPushManger.getInstance(sContext);
    }

    public static PlatformState getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new PlatformState();
        }
        return INSTANCE;
    }

    public static void setApplicationContext(Context context) {
        sContext = context;
    }

    public static Context getApplicationContext() {
        return sContext;
    }

    public static String getStatePreferenceName(){
        return STATE_PREFERENCES_NAME;
    }

    public void setLastUrl(String url) {
        Utils.logUrlFormat(url);
        mLastUrl = url;
    }

    public String getLastUrl() {
        if(mLastUrl != null) {
            final String standardUrl = mLastUrl.replace("#", "");
            Uri uri = Uri.parse(standardUrl);
            String token = uri.getQueryParameter("loginToken");
            String uid = uri.getQueryParameter("uid");
            mLastUrl = mLastUrl.replace(token, UserProfile.getInstance().getLoginToken())
                    .replace(uid, String.valueOf(UserProfile.getInstance().getUid()));
            LogUtil.i(TAG, "update last request url " + mLastUrl + " with new loginToken and uid");
        }

        return mLastUrl;
    }

    /**
     * 重置状态
     */
    public void reset() {
        mLastUrl = null;
        clearCache();

        //清除Cookie缓存
        SharedPreferences sp = sContext.getSharedPreferences(STATE_PREFERENCES_NAME, Context.MODE_PRIVATE);
        sp.edit().remove(PREFERENCE_KEY_COOKIE).apply();
    }

    public void exit() {
        mLastUrl = null;
        clearCache();
    }

    /**
     * 缓存是否可被清除
     * @return
     */
    public boolean isCacheClearable() {
        SharedPreferences sp = sContext.getSharedPreferences(STATE_PREFERENCES_NAME, Context.MODE_PRIVATE);
        boolean flag = sp.getBoolean(CLEAR_CACHE_FLAG, false);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(CLEAR_CACHE_FLAG);
        editor.apply();
        return flag;
    }

    /**
     * 清除缓存
     * <pre>
     *     将清除缓存标记置为true，当下次进入webview界面时执行清除缓存操作
     * </pre>
     */
    public void clearCache() {

        SharedPreferences sp = sContext.getSharedPreferences(STATE_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(CLEAR_CACHE_FLAG, true);
        editor.apply();

        LogUtil.i(TAG, "Enable clearing webview'cache.");
    }

    public RequestQueue getRequestQueue() {
        return mQueue;
    }

    /**
     * 获取指定key项的未读状态
     * @param key
     * @return
     */
    public boolean getUnreadStatus(int key){
        return mUnreadMap.get(key, false);
    }

    /**
     * 更新指定key项的未读状态
     * @param key
     * @param value
     */
    public void updateUnreadStatus(int key, boolean value) {
        mUnreadMap.put(key, value);
    }


    /**
     * 更新指定key项的未读状态
     * @param key
     * @param value
     */
    public int updateUnreadMessCount(int count) {
       return count;
    }
    /**
     * 更新Cookie
     * @param cookie
     */
    public void updateCookie(String cookie) {

        if(cookie == null) {
            return;
        }

        SharedPreferences sp = sContext.getSharedPreferences(STATE_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREFERENCE_KEY_COOKIE, cookie);
        editor.apply();
    }

    /**
     * 获取Cookie
     */
    public String retrieveCookie() {
        SharedPreferences sp = sContext.getSharedPreferences(STATE_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getString(PREFERENCE_KEY_COOKIE, Constants.NO_STRING);
    }

    /**
     * 设置当前登录手机号
     * @param phone
     */
    public void setCurrentPhone(String phone) {
        mCurrentPhone = phone;
    }

    /**
     * 获取当前登录手机号
     * @return
     */
    public String getCurrentPhone() {
        return mCurrentPhone;
    }

    public void setCurrentActivity(Activity activity) {
        mCurrentActivity = new WeakReference<Activity>(activity);
    }

    public Activity getCurrentActivity() {
        Activity activity = null;
        if(mCurrentActivity != null) {
            activity = mCurrentActivity.get();
        }
        return activity;
    }

    /**
     * 用于进行信鸽push的注册以及切换
     */
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
                        if (xgNotifaction == null || sContext == null) {
                            return;
                        }
                        FmsAgent.onEvent(PlatformState.getApplicationContext(), Statistics.FB_PUSHMES_RECEIVE);
                        String title = xgNotifaction.getTitle();
                        if (TextUtils.isEmpty(title)) {
                            title = sContext.getString(R.string.app_name);
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


    public void startCrashManager() {
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
}
