package com.feifan.statlib;

import android.util.Log;

/**
 * 本地统计日志类
 *
 * Created by xuchunlei on 15/12/8.
 */
class StatLog {

    public static void d(String tag, String msg) {
        if(msg != null) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {

//        if (!UmsConstants.DebugEnabled)
//            return;
//
//        if (UmsConstants.DebugLevel == LogLevel.Warn
//                || UmsConstants.DebugLevel == LogLevel.Error)
//            return;
        if (msg != null) {
            Log.i(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if(msg != null) {
            Log.e(tag, msg);
        }
    }
}
