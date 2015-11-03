package com.wbtech.ums.QuantitativeOrTiming;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.google.gson.Gson;
import com.wbtech.ums.common.CommonUtil;
import com.wbtech.ums.common.UmsConstants;

import com.wbtech.ums.model.AppstatlogModel;

/**
 * Created by dupengfei on 15/8/4.
 * <p/>
 * 定时定量帮助类
 */
public class QuantitativeOrTimingHelper {

    //一次普通日志上传定时1分钟执行
    private static final int INTERVAL = TimingService.INTERVAL;

    /**
     * 闹钟类型暂不使用，使用handler的定时
     */
    //系统闹钟
    private AlarmManager mAlarmManager;
    //系统闹钟需要的Intent
    private PendingIntent mPendingIntent;
    //当前类单例对象
    private static QuantitativeOrTimingHelper mQuantitativeOrTimingHelper;
    //bindsevice需要的连接对象
    private ServiceConnection mServiceConnection;


    //单例，为了对闹钟进行开启和取消
    public static synchronized QuantitativeOrTimingHelper getInstance() {


        if (mQuantitativeOrTimingHelper == null) {
            synchronized (QuantitativeOrTimingHelper.class) {
                if (mQuantitativeOrTimingHelper == null) {
                    mQuantitativeOrTimingHelper = new QuantitativeOrTimingHelper();
                }
            }
        }


        return mQuantitativeOrTimingHelper;
    }

    /**
     * 判断本地数据是否达到30条
     */

    public boolean needPostData(String json) {
        //假设没有数据
        boolean needPostData = false;
        //获取appstatlogModel数据对象
        Gson gson = new Gson();
        AppstatlogModel appstatlogModel = gson.fromJson(json, AppstatlogModel.class);
        //若从本地读取的json转成的appstatlogModel数据对象为空返回false
        if (appstatlogModel == null) return needPostData;
        //若获取到的事件List为空或者size小于设置的定量数返回false
        CommonUtil.printLog("appstatlogModel.getEvent_log().size()", appstatlogModel.getEvent_log().size() + "");
        if (appstatlogModel.getEvent_log() == null || appstatlogModel.getEvent_log().size() < UmsConstants.QUANTITATIVE_NUMBER)
            return needPostData;
        //若上面条件都通过则需要上传
        needPostData = true;
        return needPostData;
    }

    /**
     * 开始定时，调到TimingService
     */

    @Deprecated
    public void openAlarm(Context context) {

        Intent intent = new Intent(context, TimingService.class);
        mPendingIntent = PendingIntent.getService(context,
                0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        mAlarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.cancel(mPendingIntent);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                INTERVAL, mPendingIntent);
    }

    @Deprecated
    /**
     *  取消闹钟，当应用退出的时候就不会有日志生成就无需再去间隔的去上传
     */

    public void cancelAlarm() {

        mAlarmManager.cancel(mPendingIntent);

    }

    /**
     * 已handler定时器的模式开启定时操作
     */

    public void openHandlerTiming(Context context) {
        mServiceConnection = new ServiceConnection() {
            /** 获取服务对象时的操作 */
            public void onServiceConnected(ComponentName name, IBinder service) {

            }

            /** 无法获取到服务对象时的操作 */
            public void onServiceDisconnected(ComponentName name) {
            }

        };

        //用bindService模式开启服务，与activity绑定，当应用退出是服务也跟着关闭
        Intent intent = new Intent(context, TimingService.class);
        if (context != null)
            context.getApplicationContext().bindService(intent, mServiceConnection,
                    Service.BIND_AUTO_CREATE);

    }

    /**
     * 解除服务绑定
     */

    public void unBindTimingService(Context context) {

        if (context != null)
            context.getApplicationContext().unbindService(mServiceConnection);

    }


}
