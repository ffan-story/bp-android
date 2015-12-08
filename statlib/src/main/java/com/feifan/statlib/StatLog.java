package com.feifan.statlib;

import android.util.Log;

/**
 * 本地统计日志类
 *
 * Created by xuchunlei on 15/12/8.
 */
class StatLog {

    public static void i(String tag, String msg) {

//        if (!UmsConstants.DebugEnabled)
//            return;
//
//        if (UmsConstants.DebugLevel == LogLevel.Warn
//                || UmsConstants.DebugLevel == LogLevel.Error)
//            return;

        Log.i(tag, msg);
    }
}
