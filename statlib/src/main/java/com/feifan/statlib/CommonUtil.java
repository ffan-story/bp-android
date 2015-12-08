package com.feifan.statlib;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 通用工具类
 *
 * Created by xuchunlei on 15/12/8.
 */
public class CommonUtil {

    private static final String TAG = "CommonUtil";

    private CommonUtil() {

    }

    /**
     * Testing equipment networking and networking WIFI
     *
     * @param context
     * @return true or false
     */
    public static boolean isNetworkAvailable(Context context) {
        if (checkPermissions(context, "android.permission.INTERNET")) {
            ConnectivityManager cManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cManager == null)
                return false;

            NetworkInfo info = cManager.getActiveNetworkInfo();
            if (info != null && info.isAvailable()) {
                StatLog.i(TAG, "Network is available.");
                return true;
            } else {
                StatLog.i(TAG, "Network is not available.");
                return false;
            }

        } else {
            StatLog.e(TAG, "android.permission.INTERNET permission should be added into AndroidManifest.xml.");
            return false;
        }

    }

    /**
     * 检查权限
     *
     * @param context
     * @param permission
     * @return true or false
     */
    public static boolean checkPermissions(Context context, String permission) {
        PackageManager pm = context.getPackageManager();
        return pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED;
    }
}
