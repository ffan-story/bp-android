package com.feifan.bp.envir;

import com.feifan.bp.BuildConfig;
import com.feifan.bp.envir.HostSupplier.SitHostFactory;
import com.feifan.bp.envir.HostSupplier.ProductPreFactory;
import com.feifan.bp.envir.HostSupplier.ProductFactory;
import com.feifan.bp.envir.HostSupplier.IHostFactory;
import com.feifan.bp.envir.AuthSupplier.IAuthFactory;
import com.feifan.bp.envir.AuthSupplier.SitAuthFactory;
import com.feifan.bp.envir.AuthSupplier.ProductAuthFactory;

/**
 * Created by xuchunlei on 15/10/14.
 */
public class EnvironmentManager {
    private final static IHostFactory sHostFactory;

    private final static IAuthFactory sAuthFactory;

    static {
        switch (BuildConfig.CURRENT_ENVIRONMENT){
            case SIT:
                sHostFactory = new SitHostFactory();
                sAuthFactory = new SitAuthFactory();
                break;
            case PRODUCT_PRE:
//                sHostFactory = new ProductPreFactory();
                sHostFactory = new ProductFactory();
                sAuthFactory = new ProductAuthFactory();
                break;
            case PRODUCT:
                sHostFactory = new ProductFactory();
                sAuthFactory = new ProductAuthFactory();
                break;
            default:
                sHostFactory = new SitHostFactory();
                sAuthFactory = new SitAuthFactory();
        }
    }

    /**
     * 获得主机域名工厂
     * @return
     */
    public static IHostFactory getHostFactory(){
        return sHostFactory;
    }

    /**
     * 获得权限工厂
     * @return
     */
    public static IAuthFactory getAuthFactory(){
        return sAuthFactory;
    }
}
