package com.feifan.bp;

/**
 * Created by xuchunlei on 15/6/17.
 */
public class Constants {

    //密码长度范围
    public static int PASSWORD_MIN_LENGTH = 8;
    public static int PASSWORD_MAX_LENGTH = 20;

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
    public static final String URL_PATH_HOME = "home";


    public enum Environment {
        SIT_GATEWAY,      // Gateway集成环境
        SIT_API,          // Api集成环境
        PRODUCT_PRE,      // 预生产环境
        PRODUCT,          // 生产环境
    }

    /** 当前运行环境 */
//    public static Environment CURRENT_ENVIRONMENT = Environment.PRODUCT_PRE;
}
