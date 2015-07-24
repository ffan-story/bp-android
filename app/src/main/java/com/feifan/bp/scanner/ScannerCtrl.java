package com.feifan.bp.scanner;

import com.feifan.bp.PlatformHelper;
import com.feifan.bp.PlatformState;

/**
 * Created by maning on 15/7/7.
 */
public class ScannerCtrl {

    public static String getSignH5Url(String signNo) {
        String path = "H5App/index.html#/goods/search_result";
        return PlatformHelper.getManagedH5Url(path).
                concat("&merchantId=").
                concat(String.valueOf(PlatformState.getInstance().getUserProfile().getAuthRangeId())).
                concat("&signNo=").concat(signNo);
    }

}
