package com.wbtech.ums.dao;



import com.wbtech.ums.common.CommonUtil;

/**
 * Created by dupengfei on 15/8/4.
 *
 * GetInfoFromFile子类，执行定时操作
 */
public class TimingThread extends  GetInfoFromFile {

    public TimingThread(String str) {
        //是定时
        super(str);
        getInfoFromFileType = GetInfoFromFileType.TIMING;
        CommonUtil.printLog("TimingThread", str);
    }

}
