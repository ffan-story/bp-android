package com.feifan.bp;

import android.util.Log;

/**
 * Created by xuchunlei on 15/6/24.
 */
public class LogUtil {

    private LogUtil() {

    }

    public static void i(String tag, String message) {
        if(BuildConfig.DEBUG) {
            Log.i(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if(BuildConfig.DEBUG) {
            Log.w(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if(BuildConfig.DEBUG) {
            Log.e(tag, message);
        }
    }
}
