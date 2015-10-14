package com.feifan.bp.environment;

import com.feifan.bp.BuildConfig;
import com.feifan.bp.environment.HostSupplier.SitHostFactory;
import com.feifan.bp.environment.HostSupplier.ProductPreFactory;
import com.feifan.bp.environment.HostSupplier.ProductFactory;
import com.feifan.bp.environment.HostSupplier.IHostFactory;

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
