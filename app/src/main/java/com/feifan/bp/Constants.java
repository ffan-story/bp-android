package com.feifan.bp;

/**
 * Created by xuchunlei on 15/6/17.
 */
public class Constants {
    // 非法的整型数值
    public static final int NO_INTEGER = -1;
    // 非法的字符串值
    public static final String NO_STRING = "n/a";

    // 网络
    public static final int RESPONSE_CODE_SUCCESS = 200;

    // 用户类型-商户
    public static final String AUTH_RANGE_TYPE_MERCHANT = "merchant";
    // 用户类型－门店
    public static final String AUTH_RANGE_TYPE_STORE = "store";

    public enum Environment {
        GATEWAY_SIT,      // Gateway集成环境
        API_SIT,          // Api集成环境
    }

    /** 当前运行环境 */
    public static Environment CURRENT_ENVIRONMENT = Environment.GATEWAY_SIT;
}
