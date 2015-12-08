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

    private static Handler handler;

    private static final String TAG = "FFan-Stat";

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
        FmsConstants.urlPrefix = urlPrefix;
        FmsAgent.postHistoryLog(context);
//        UmsAgent.postClientData(context);
//        UmsAgent.onError(context);
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
//                onEvent(context, event_id, 1);
            }
        });
        handler.post(thread);

    }
}
