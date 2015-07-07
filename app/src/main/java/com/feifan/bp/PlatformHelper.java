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
     * 获取受托管的Html5页面的URL全路径
     * @param path
     * @return
     */
    public static String getManagedH5Url(String path) {
        return FactorySet.getUrlFactory().getH5HostUrl().concat(path).
                concat("?loginToken=").
                concat(PlatformState.getInstance().getUserProfile().getLoginToken()).
                concat("&uid=").
                concat(String.valueOf(PlatformState.getInstance().getUserProfile().getUid())).
                concat("&app_type=bpMobile");
    }
}
