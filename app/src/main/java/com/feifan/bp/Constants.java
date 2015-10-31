package com.feifan.bp;

/**
 * Created by xuchunlei on 15/6/17.
 */
public class Constants {
    public static final String TAG = "Platform";

    //密码长度范围
    public static int PASSWORD_MIN_LENGTH = 8;
    public static int PASSWORD_MAX_LENGTH = 20;

    // 图片
    public static int IMAGE_MAX_WIDTH = 1280;
    public static int IMAGE_MAX_HEIGHT = 720;
    public static int IMAGE_MAX_BYTES = 1536;

    // 非法的整型数值
    public static final int NO_INTEGER = -1;
    // 非法的字符串值
    public static final String NO_STRING = "n/a";

    // 网络
    public static final int RESPONSE_CODE_SUCCESS = 200;

    // 浏览器
    public static final String URL_SCHEME_PLATFORM = "platform";
    public static final String URL_SCHEME_ERROR = "error";
    public static final String URL_PATH_LOGIN = "login";
    public static final String URL_PATH_EXIT = "exit";
    public static final String URL_PATH_HOME = "home";
    public static final String URL_LOCAL_IMAGE = "getLocalImage";


    public enum Environment {
        SIT,              // Api集成环境
        PRODUCT_PRE,      // 预生产环境
        PRODUCT,          // 生产环境
    }

    // 字符串长度
    public static int FEED_BACK_MAX_lENGTH = 300;

    /** 当前运行环境 */
//    public static Environment CURRENT_ENVIRONMENT = Environment.PRODUCT_PRE;
}
