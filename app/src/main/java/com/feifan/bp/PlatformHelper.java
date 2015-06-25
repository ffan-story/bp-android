package com.feifan.bp;

import android.util.Log;

import com.feifan.bp.factory.FactorySet;

/**
 * Created by xuchunlei on 15/6/23.
 */
public class PlatformHelper {

    private PlatformHelper() {

    }

    /**
     * 获取业务管理项的URL全路径
     * @param path
     * @return
     */
    public static String getManageUrl(String path) {
        Log.e("xuchunlei", PlatformState.getInstance().getUserProfile().getLoginToken());
        return FactorySet.getUrlFactory().getH5HostUrl().concat(path).
                concat("?loginToken=").concat(PlatformState.getInstance().getUserProfile().getLoginToken());
    }
}
