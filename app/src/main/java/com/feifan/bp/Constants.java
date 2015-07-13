package com.feifan.bp;

/**
 * Created by xuchunlei on 15/6/17.
 */
public class Constants {

    public static final String TAG = "Platform";

    // 非法的整型数值
    public static final int NO_INTEGER = -1;
    // 非法的字符串值
    public static final String NO_STRING = "n/a";

    // 网络
    public static final int RESPONSE_CODE_SUCCESS = 200;

    // 浏览器
    public static final String URL_SCHEME_PLATFORM = "platform";
    public static final String URL_PATH_LOGIN = "login";
    public static final String URL_PATH_EXIT = "exit";


    public enum Environment {
        SIT_GATEWAY,      // Gateway集成环境
        SIT_API,          // Api集成环境
    }

    /** 当前运行环境 */
    public static Environment CURRENT_ENVIRONMENT = Environment.SIT_GATEWAY;
}
