package com.feifan.bp.envir;

import com.feifan.bp.BuildConfig;
import com.feifan.bp.envir.HostSupplier.SitHostFactory;
import com.feifan.bp.envir.HostSupplier.ProductPreFactory;
import com.feifan.bp.envir.HostSupplier.ProductFactory;
import com.feifan.bp.envir.HostSupplier.IHostFactory;

/**
 * Created by xuchunlei on 15/10/14.
 */
public class EnvironmentManager {
    private final static IHostFactory sHostFactory;

    static {
        switch (BuildConfig.CURRENT_ENVIRONMENT){
            case SIT:
                sHostFactory = new SitHostFactory();
                break;
            case PRODUCT_PRE:
                sHostFactory = new ProductPreFactory();
                break;
            case PRODUCT:
                sHostFactory = new ProductFactory();
                break;
            default:
                sHostFactory = new SitHostFactory();
        }
    }

    /**
     * 获得主机域名工厂
     * @return
     */
    public static IHostFactory getHostFactory(){
        return sHostFactory;
    }
}
