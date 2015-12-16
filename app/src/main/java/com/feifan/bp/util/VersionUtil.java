package com.feifan.bp.util;

import android.os.Build;

import com.feifan.bp.BuildConfig;

/**
 * Created by xuchunlei on 15/12/2.
 */
public class VersionUtil {

    private VersionUtil() {

    }

    /**
     * sdk版本号高于15
     * @return
     */
    public static boolean isHigherThanICS_MR1() {
        return BuildConfig.VERSION_CODE > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1;
    }
}
