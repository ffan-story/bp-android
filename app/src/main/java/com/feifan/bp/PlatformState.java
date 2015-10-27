package com.feifan.bp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.feifan.bp.util.LogUtil;

import java.io.File;

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

    private static PlatformState INSTANCE;

    private static Context sContext;

    // 网络请求队列
    private RequestQueue mQueue;

    // 上次访问的url地址
    private String mLastUrl;

    private PlatformState(){
        mQueue = Volley.newRequestQueue(sContext);
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
        LogUtil.i(TAG, "save last request url " + url);
        mLastUrl = url;
    }

    public String getLastUrl(Context context) {
        if(mLastUrl != null) {
            final String standardUrl = mLastUrl.replace("#", "");
            Uri uri = Uri.parse(standardUrl);
            String token = uri.getQueryParameter("loginToken");
            String uid = uri.getQueryParameter("uid");
            mLastUrl = mLastUrl.replace(token, UserProfile.instance(context).getLoginToken())
                    .replace(uid, String.valueOf(UserProfile.instance(context).getUid()));
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

//        File cacheDir = context.getCacheDir().getAbsoluteFile();
//        Utils.deleteFile(cacheDir, "webview");
//        Utils.deleteFile(context.getDatabasePath("webview").getParentFile(), "webview");
//        Utils.deleteFile(cacheDir, "ApplicationCache.db");

        SharedPreferences sp = sContext.getSharedPreferences(STATE_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(CLEAR_CACHE_FLAG, true);
        editor.apply();

        LogUtil.i(TAG, "Enable clearing webview'cache.");
    }

    public RequestQueue getRequestQueue() {
        return mQueue;
    }

}
