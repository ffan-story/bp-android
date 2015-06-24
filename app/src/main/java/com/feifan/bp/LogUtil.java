package com.feifan.bp;

import android.util.Log;

/**
 * Created by xuchunlei on 15/6/24.
 */
public class LogUtil {

    private static boolean NEED_LOG;

    static {
        switch (Constants.CURRENT_ENVIRONMENT) {
            case GATEWAY_SIT:
            case API_SIT:
                NEED_LOG = true;
                break;
            default:
                NEED_LOG = false;
        }
    }
    private LogUtil() {

    }

    public static void i(String tag, String message) {
        if(NEED_LOG) {
            Log.i(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if(NEED_LOG) {
            Log.w(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if(NEED_LOG) {
            Log.e(tag, message);
        }
    }
}
