package com.wbtech.ums.dao;

import com.wbtech.ums.common.CommonUtil;

/**
 * Created by dupengfei on 15/8/4.
 *
 * GetInfoFromFile子类，执行定量操作
 */
public class QuantitativeThread  extends GetInfoFromFile {

    public QuantitativeThread(String str) {
        super(str);
        //定量
        getInfoFromFileType = GetInfoFromFileType.QUANTITATIVE;
        CommonUtil.printLog("QuantitativeThread",str);
    }

}
