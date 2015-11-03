package com.wbtech.ums.QuantitativeOrTiming;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.wanda.base.config.GlobalConfig;
import com.wbtech.ums.common.CommonUtil;
import com.wbtech.ums.common.UmsConstants;
import com.wbtech.ums.dao.TimingThread;


/**
 * Created by dupengfei on 15/8/4.
 * 定时启动这个服务，在这个服务中去做普通日志上传逻辑
 * <p/>
 * 两种模式一种用QuantitativeOrTimingHelper中的openAlarm用系统的alarm
 * 一种使用Handler的定时操作，现使用Handler模式
 */
public class TimingService extends Service {

    //时间间隔为60秒
    public static final int INTERVAL = UmsConstants.TIMING_INTERVAL;
    //第一次执行间隔
    private static final int FIRST_INTERVAL = 0;

    //用Handler去定时
    private Handler mHandler;

    private Runnable mRunnable;


    @Override
    public void onCreate() {
        super.onCreate();
        //开始定时日志操作
        timingByHandler();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {

        //结束定时器
        mHandler.removeCallbacks(mRunnable);
        CommonUtil.printLog("#######TimingService", "onDestroy");
        super.onDestroy();
    }

    //初始化
    private void timingByHandler() {

        CommonUtil.printLog("#####timingByHandler", "begin");

        mHandler = new Handler();

        mRunnable = new Runnable() {

            @Override
            public void run() {

                doTiming();
                mHandler.postDelayed(this, INTERVAL);

            }

        };
        //第一次是立即执行
        mHandler.postDelayed(mRunnable, FIRST_INTERVAL);

    }

    private void doTiming() {
        CommonUtil.printLog("######doTiming", "begin");
        //执行定时日志上传操作
        TimingThread timingThread = new TimingThread(GlobalConfig.getAppContext().getPackageName());
        timingThread.start();
        GetInfoFromFileThreadPool.getInstance().putThread(timingThread);
    }



}
