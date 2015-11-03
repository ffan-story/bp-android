/**
 * Cobub Razor
 * <p/>
 * An open source analytics android sdk for mobile applications
 *
 * @package Cobub Razor
 * @author WBTECH Dev Team
 * @copyright Copyright (c) 2011 - 2012, NanJing Western Bridge Co.,Ltd.
 * @license http://www.cobub.com/products/cobub-razor/license
 * @link http://www.cobub.com/products/cobub-razor/
 * @filesource
 * @since Version 0.1
 */
package com.wbtech.ums.dao;

import android.content.Context;
import android.text.TextUtils;

import com.wanda.stat.WClickAgentImpl;
import com.wbtech.ums.QuantitativeOrTiming.GetInfoFromFileThreadPool;
import com.wbtech.ums.QuantitativeOrTiming.QuantitativeOrTimingHelper;
import com.wbtech.ums.common.CommonUtil;
import com.wbtech.ums.common.NetworkUitlity;
import com.wbtech.ums.common.UmsConstants;
import com.wbtech.ums.objects.MyMessage;
import com.wbtech.ums.util.FileUtilLog;

public abstract class GetInfoFromFile extends Thread {
    public Context context;

    private String packageName;

    //避免点击三十次的时候恰好执行1分钟，对文件造成同步操作
    private static boolean isRuning = false;

    //用于判断是否是定时还是定量
    //应对场景：事件产生了30次出发了定量，之后到了定时的间隔事件，就是说在不到定量30次的前提下若想继续执行定时则需再原来的方法加上判断
    protected GetInfoFromFileType getInfoFromFileType;

    public GetInfoFromFile(String packageName) {

        super();
        this.packageName = packageName;
    }

    @Override
    public void run() {

        doGetInfoFromFile();

    }

    protected void doGetInfoFromFile() {
        try {
            //如果已经在执行就return掉
            if (isRuning) doReturn();
            //isRuning设置成true
            isRuning = true;
            FileUtilLog fileUtilLog = new FileUtilLog();
            String json = fileUtilLog.readFileSdcardFile();
            //若数据空返回


            if (!TextUtils.isEmpty(json)) {

                //若是定时就不执行定量的判断了，若想执行定量的错做，则不能是定时
                if (doQuantitative()) {

                    //每次普通的点击事件都会执行到此，在此处做定量30词拦截判断
                    //若不需要上传，不到30次则不往下执行http
                    boolean needPost = QuantitativeOrTimingHelper.getInstance().needPostData(json);
                    CommonUtil.printLog("needPostData+json", json);
                    isRuning = false;
                    if (!needPost) return;
                }
                //上传普通日志type是10
                MyMessage message = NetworkUitlity.post(WClickAgentImpl.URL + UmsConstants.APPSTATLOGURL, json, UmsConstants.POST_TYPE_DEFAULT);
                if (message.isFlag()) {
                    //上传完成后删除本地文件
                    fileUtilLog.deleteSDFile();
                    //运行状态转为false
                    isRuning = false;
                    doReturn();
                }
            }else
            doReturn();
        } catch (Exception e) {
            doReturn();
            e.printStackTrace();
        }


    }

    public enum GetInfoFromFileType {
        //默认，（默认暂时没用），定量，定时，用于子类指定自己是哪种类型，判断是否执行定次数的逻辑
        DEFAULT, QUANTITATIVE, TIMING

    }

    //return并发送message让thread关闭
    private void doReturn(){
        GetInfoFromFileThreadPool.getInstance().handlerForstopThread();
        return;
    }


    //是否是定量操作
    private boolean doQuantitative() {

        return getInfoFromFileType == GetInfoFromFileType.QUANTITATIVE;
    }

}
