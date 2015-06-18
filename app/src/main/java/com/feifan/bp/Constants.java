package com.feifan.bp;

/**
 * Created by xuchunlei on 15/6/17.
 */
public class Constants {
    // 非法的整型数值
    public static final int NO_INTEGER = -1;
    // 非法的字符串值
    public static final String NO_STRING = "n/a";

    // ffan-sit gateway地址
    public static final String URL_HOST_GATEWAY_FFAN_SIT = "http://api.sit.ffan.com/";

    public enum Environment {
        GATEWAY_SIT,      // Gateway集成环境
        API_SIT,          // Api集成环境
    }

    /** 当前运行环境 */
    public static Environment CURRENT_ENVIRONMENT = Environment.API_SIT;
}
