package com.feifan.bp;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.feifan.bp.account.AccountManager;
import com.feifan.bp.login.UserProfile;

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

    private static PlatformState INSTANCE;

//    private static Context sContext;

    // 上次访问的url地址
    private String mLastUrl;

    private PlatformState(){
        Log.i(Constants.TAG, "App is running within " + BuildConfig.CURRENT_ENVIRONMENT);
    }

    public static PlatformState getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new PlatformState();
        }
        return INSTANCE;
    }

    public static void setApplicationContext(Context context) {

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
            mLastUrl = mLastUrl.replace(token, AccountManager.instance(context).getLoginToken())
                    .replace(uid, String.valueOf(AccountManager.instance(context).getUid()));
            LogUtil.i(TAG, "update last request url " + mLastUrl + " with new loginToken and uid");
        }

        return mLastUrl;
    }

    /**
     * 重置状态
     */
    public void reset(Context context) {
        mLastUrl = null;
        clearCache(context);
    }

    private void clearCache(Context context) {

        File cacheDir = context.getCacheDir().getAbsoluteFile();
        Utils.deleteFile(cacheDir, "webview");
        Utils.deleteFile(context.getDatabasePath("webview").getParentFile(), "webview");
        LogUtil.i(TAG, "WebView cache cleared");
    }

    private static class BitmapCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {
        public static int getDefaultLruCacheSize() {
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 8;

            return cacheSize;
        }

        public BitmapCache() {
            this(getDefaultLruCacheSize());
        }

        public BitmapCache(int sizeInKiloBytes) {
            super(sizeInKiloBytes);
        }

        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight() / 1024;
        }

        @Override
        public Bitmap getBitmap(String url) {
            return get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            put(url, bitmap);
        }
    }
}
