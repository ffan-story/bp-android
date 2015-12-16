package com.feifan.statlib;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by xuchunlei on 15/12/8.
 */
class AppInfo {
    private static Context context;
    private static final String TAG = "AppInfo";
    private static final String UMS_APPKEY = "UMS_APPKEY";

    static void init(Context context) {
        AppInfo.context = context;
    }

    static String getAppKey() {
        String umsAppkey = "";
        try {
            PackageManager pm = context.getPackageManager();

            ApplicationInfo ai = pm.getApplicationInfo(
                    context.getPackageName(),
                    PackageManager.GET_META_DATA);

            if (ai != null) {
                umsAppkey = ai.metaData.getString(UMS_APPKEY);
                if (umsAppkey == null)
                    StatLog.e(TAG, "Could not read UMS_APPKEY meta-data from AndroidManifest.xml.");
            }
        } catch (Exception e) {
            StatLog.e(TAG, "Could not read UMS_APPKEY meta-data from AndroidManifest.xml.");
            StatLog.e(TAG, e.toString());
            e.printStackTrace();
        }
        return umsAppkey;
    }

    static String getAppVersion() {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null)
                versionName = "";
        } catch (Exception e) {
            StatLog.e(TAG, e.toString());
        }
        return versionName;
    }

    /**
     * 下载来源
     * @return
     */
    static String getAppSource() {
        return "";
    }
}