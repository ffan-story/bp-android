package com.feifan.bp.util;

import android.content.Context;
import android.util.DisplayMetrics;

import com.feifan.bp.PlatformState;

/**
 * 设备相关工具类
 *
 * Created by xuchunlei on 15/12/6.
 */
public class DeviceUtil {

    private DeviceUtil() {

    }

    /**
     * 获取设备屏幕高度
     * @return
     */
    public static int getDeviceHeight() {
        return PlatformState.getApplicationContext()
                            .getResources()
                            .getDisplayMetrics()
                            .heightPixels;
    }

    /**
     * 获取设备屏幕宽度
     * @return
     */
    public static int getDeviceWidth() {
        return PlatformState.getApplicationContext()
                .getResources()
                .getDisplayMetrics()
                .widthPixels;
    }

    /**
     * 获取设备屏幕密度
     * @return
     */
    public static float getDeviceDensity() {
        return PlatformState.getApplicationContext()
                .getResources()
                .getDisplayMetrics()
                .density;
    }
}
