package com.feifan.statlib;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

/**
 * 飞凡统计代理类
 * <pre>
 *     发送统计消息
 * </pre>
 *
 * Created by xuchunlei on 15/12/8.
 */
public class FmsAgent {



    private static final String TAG = "FFan-Stat";

    /**
     * 发送策略
     * 目前支持
     * 1.BATCH方式，缓存所有日志，下次启动时批量发送
     * 2.REALTIME方式，实时发送
     */
    public enum SendPolicy {
        BATCH,
        REALTIME
    }

    /** 异步发送消息的Handler */
    private static Handler handler;

    static {
        HandlerThread localHandlerThread = new HandlerThread("FmsAgent");
        localHandlerThread.start();
        handler = new Handler(localHandlerThread.getLooper());
    }

    private FmsAgent() {

    }

    /**
     * 初始化
     *
     * <pre>
     *     统计服务Url前缀必须以“?”结束，如"http://localhost/index.php?"
     * </pre>
     * @param urlPrefix: 统计服务Url前缀字符串
     * @throws Exception
     */
    public static void init(Context context, final String urlPrefix) {

        //初始化
        DeviceInfo.init(context);
        AppInfo.init(context);

        FmsConstants.urlPrefix = urlPrefix;
        FmsAgent.postHistoryLog(context);
        StatLog.i(TAG, "Call init();BaseURL = " + urlPrefix);
    }

    /**
     * @param context
     */
    static void postHistoryLog(final Context context) {
        StatLog.i(TAG, "Post HistoryLog to " + FmsConstants.urlPrefix);
        if (CommonUtil.isNetworkAvailable(context)) {
//            if (UmsAgent.isPostFile) {
//                Thread thread = new UploadHistoryLog(context);
//                handler.post(thread);
//                UmsAgent.isPostFile = false;
//            }
//
        }
    }

    /**
     * 统计事件
     *
     * @param context
     */
    public static void onEvent(final Context context, final String event_id) {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                StatLog.i(TAG, "Send " + event_id + " to " + FmsConstants.urlPrefix);
                EventManager em = new EventManager(context, event_id);
                em.postEventInfo();
            }
        });
        handler.post(thread);

    }
}
