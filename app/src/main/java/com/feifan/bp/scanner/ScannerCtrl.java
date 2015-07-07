package com.feifan.bp.scanner;

import com.feifan.bp.PlatformState;
import com.feifan.bp.factory.FactorySet;

/**
 * Created by maning on 15/7/7.
 */
public class ScannerCtrl {

    public static String getSignH5Url(String signNo) {
        String path = "H5App/index.html#/goods/search_result";
        return FactorySet.getUrlFactory().getH5HostUrl().concat(path).
                concat("?loginToken=").
                concat(PlatformState.getInstance().getUserProfile().getLoginToken()).
                concat("&merchantId=").
                concat(String.valueOf(PlatformState.getInstance().getUserProfile().getAuthRangeId())).
                concat("&signNo=").concat(signNo);
    }

}
