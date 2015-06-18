/**
 *
 */
package com.feifan.bp.factory;

/**
 * 商家后台Url抽象工厂
 *
 * @author xuchunlei
 *         create at: 2015年6月18日 下午4:05:28
 */
public interface IUrlFactory {

    /**
     * 获取飞凡服务器主机地址
     * @return
     */
    String getFFanHostUrl();

}
