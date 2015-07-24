package com.feifan.bp.net;

import com.feifan.bp.BuildConfig;
import com.feifan.bp.net.url.IUrlFactory;
import com.feifan.bp.net.url.UrlApiSitFactory;
import com.feifan.bp.net.url.UrlGatewaySitFactory;
import com.feifan.bp.net.url.UrlProductFactory;
import com.feifan.bp.net.url.UrlProductPreFactory;

/**
 * Created by maning on 15/7/24.
 */
public class NetUtils {

    /**
     * 获取Url工厂实例
     * @return
     * @author:     xuchunlei
     * created at: 2015年4月24日 下午4:24:33
     */
    public static IUrlFactory getUrlFactory() {

        switch (BuildConfig.CURRENT_ENVIRONMENT) {
            case SIT_GATEWAY:
                return new UrlGatewaySitFactory();
            case SIT_API:
                return new UrlApiSitFactory();
            case PRODUCT_PRE:
                return new UrlProductPreFactory();
            case PRODUCT:
                return new UrlProductFactory();
            default:
                return new UrlGatewaySitFactory();
        }
    }
}
