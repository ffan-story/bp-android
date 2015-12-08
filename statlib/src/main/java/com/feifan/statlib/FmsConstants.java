package com.feifan.statlib;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import com.feifan.statlib.FmsAgent.SendPolicy;

/**
 * 统计常量
 *
 * Created by xuchunlei on 15/12/8.
 */
public class FmsConstants {

    /**
     * 统计地址Url前缀变量，必须以“?”结束，形如"http://localhost/index.php?"
     */
    public static String urlPrefix = "";

    public static final Map<String, Object> sClientDataMap = new HashMap<String, Object>();

    /**
     * 统计事件缓存文件
     */
    public static final String CACHE_FILE_NAME = "/fms.cache";

    /**
     * 发送策略
     * 默认为REALTIME
     */
    public static SendPolicy mReportPolicy = SendPolicy.REALTIME;
}
