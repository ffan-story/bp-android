package com.feifan.bp.widget;

import com.feifan.bp.factory.FactorySet;

/**
 * Created by xuchunlei on 15/6/23.
 */
public class PlatformHelper {

    private PlatformHelper() {

    }

    /**
     * 获取业务管理项的URL
     * @param path
     * @return
     */
    public static String getManageUrl(String path) {
        return FactorySet.getUrlFactory().getH5HostUrl().concat(path);
    }
}
