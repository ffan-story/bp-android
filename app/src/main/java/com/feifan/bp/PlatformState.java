package com.feifan.bp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.util.SparseArray;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.feifan.bp.network.HttpsUrlStack;
import com.feifan.bp.util.LogUtil;

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

    private PlatformState(){
        mQueue = Volley.newRequestQueue(sContext, new HttpsUrlStack());
        Log.i(Constants.TAG, "App is running within " + BuildConfig.CURRENT_ENVIRONMENT);
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
}
