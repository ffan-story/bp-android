package com.feifan.bp;

/**
 * Created by xuchunlei on 15/6/17.
 */
public class Constants {
    public static final String TAG = "Platform";

    /**
     * 客服电话   400-950-6655
     */
    public static String SERVICE_TEL = "4009506655";

    public static int CODE_LENGTH_TEN = 10;
    public static int CODE_LENGTH_TWENTY = 20;
    public static String LIST_LIMIT = "10";
    public static int LIST_MAX_LENGTH = 15;
    //密码长度范围
    public static int PASSWORD_MIN_LENGTH = 8;
    public static int PASSWORD_MAX_LENGTH = 20;

    // 图片
    public static int IMAGE_MAX_WIDTH = 1280;
    public static int IMAGE_MAX_HEIGHT = 720;
    public static int IMAGE_MAX_BYTES = 1536;

    /**
     * 消息列表（已读未读）状态
     */
    public static String UNREAD="0";
    public static String READ="1";
    public static String IGNORE="2";

    // 非法的整型数值
    public static final int NO_INTEGER = -1;
    // 非法的字符串值
    public static final String NO_STRING = "n/a";

    // 网络
    public static final int RESPONSE_CODE_SUCCESS = 200;

    // 浏览器
    public static final String URL_SCHEME_PLATFORM = "platform";
    public static final String URL_SCHEME_PLATFORM_LOGIN = "login";
    public static final String URL_SCHEME_PLATFORM_EXIT= "exit";
    public static final String URL_SCHEME_PLATFORM_HOME = "home";
    public static final String URL_SCHEME_PLATFORM_IMAGE = "getLocalImage";
    public static final String URL_SCHEME_PLATFORM_CLOSE = "close";
    public static final String URL_SCHEME_ACTION = "action";
    public static final String URL_SCHEME_ERROR = "error";
    public static final String URL_SCHEME_LOGIN_INVALID = "login";

    //Cookie key
    public static final String COOKIE_KEY = "Cookie";
    public static final String COOKIE_VALUE = "MAPPUIF";
    public static final String COOKIE_RESPONSE_KEY = "Set-Cookie";

    //权限申请
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    public static final int MY_PERMISSIONS_REQUEST_STORAGE = 2;
    public static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 3;

    public enum Environment {
        TEST,             // RD联调环境
        SIT,              // QA测试环境
        PRODUCT_PRE,      // 灰度测试环境
        PRODUCT,          // 生产环境
    }

    // 字符串长度
    public static int FEED_BACK_MAX_lENGTH = 300;

    /**
     * 员工管理
     *
     */
    public static int REQUEST_CODE = 1000;

    /**
     * 闪购活动-商品-返回值
     */
    public static String RETURN_STATUS ="ReturnStatus";

    /**
     * 闪购活动-商品-提交返回
     *
     */
    public static int RETURN_COMMIT = 1;

    /**
     * 闪购活动-商品-保存返回
     *
     */
    public static int RETURN_SAVE = 0;


    /**
     * 通用参数键值－title
     */
    public static final String EXTRA_KEY_TITLE = "title";
    /**
     * 通用参数键值－to
     */
    public static final String EXTRA_KEY_TO = "to";
    /**
     * 通用参数键值－url
     */
    public static final String EXTRA_KEY_URL = "url";
    /**
     * 通用参数键值－fragments
     */
    public static final String EXTRA_KEY_FRAGMENTS = "fragments";

    /**
     * 隐藏其它补贴金额  1:隐藏 0:显示
     */
    public  static final String MARKETING_HIDE_OTHER_SUBSIDY = "1";

    /**
     * 标题显示的最大长度
     */
    public static final int TITLE_LIMIT_MAX = 15;
}
