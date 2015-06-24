package com.feifan.bp;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.feifan.bp.login.UserProfile;

/**
 * 状态类
 * <p>
 *     存取平台相关的状态变量，目前包括登录信息
 * </p>
 * Created by xuchunlei on 15/6/17.
 */
public class PlatformState {

    private static final String TAG = "PlatformState";

    private static PlatformState INSTANCE;
    // 供全局使用的Context
    private static Context sContext;

    private UserProfile mProfile;

    // 网络请求队列
    private RequestQueue mQueue;

    // 图片加载器实例
    private ImageLoader mLoader;

    private PlatformState(){
        mProfile = new UserProfile(sContext);
        mQueue = Volley.newRequestQueue(sContext);
        mLoader = new ImageLoader(mQueue, new BitmapCache());

    }

    public static PlatformState getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new PlatformState();
        }
        return INSTANCE;
    }

    public static void setApplicationContext(Context context) {
        if (sContext != null) {
            Log.w(TAG, "setApplicationContext called twice! old=" + sContext + " new=" + context);
        }
        sContext = context.getApplicationContext();
    }

    public static Context getApplicationContext() {
        return sContext;
    }

    public UserProfile getUserProfile() {
        return mProfile;
    }

    public RequestQueue getRequestQueue() {
        return mQueue;
    }

    public ImageLoader getImageLoader() {
        return mLoader;
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
