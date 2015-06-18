/**
 * 
 */
package com.feifan.bp.factory;

import com.feifan.bp.Constants;

/**
 * 工厂集
 * <p>
 *     用来获取项目使用的各类工厂
 * </p>
 * @author    xuchunlei
 * create at: 2015年4月24日 下午4:08:52
 *
 */
public class FactorySet {
    private FactorySet() {
        
    }
    
    /**
     * 获取Url工厂实例
     * @return
     * @author:     xuchunlei
     * created at: 2015年4月24日 下午4:24:33
     */
    public static IUrlFactory getUrlFactory() {

        switch (Constants.CURRENT_ENVIRONMENT) {
            case GATEWAY_SIT:
                return UrlFactory.UrlGatewaySitFactory.getInstance();
            case API_SIT:
                return UrlFactory.UrlApiSitFactory.getInstance();
            default:
                return UrlFactory.UrlGatewaySitFactory.getInstance();
        }
    }
    

}
