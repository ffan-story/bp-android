package com.feifan.statlib;

import android.content.Context;

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
//        CobubLog.i(tag, "postHistoryLog");
//        if (CommonUtil.isNetworkAvailable(context)) {
//            if (UmsAgent.isPostFile) {
//                Thread thread = new UploadHistoryLog(context);
//                handler.post(thread);
//                UmsAgent.isPostFile = false;
//            }
//
//        }
    }

    /**
     * 统计事件
     *
     * @param context
     */
    public static void onEvent(final Context context, final String event_id) {
//        Thread thread = new Thread(new Runnable() {
//            public void run() {
//                CobubLog.i(tag, "Call onEvent(context,event_id)");
//                onEvent(context, event_id, 1);
//            }
//        });
//        handler.post(thread);
        StatLog.i(TAG, event_id);
    }
}
