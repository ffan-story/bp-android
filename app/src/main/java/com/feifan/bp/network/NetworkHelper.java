package com.feifan.bp.network;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Cookie工具类
 * Created by xuchunlei on 16/2/26.
 */
public class NetworkHelper {

    // Set-Cookie长度
    private static int COOKIE_KEY_LENGTH = 10;

    // 用于查找Cookie
    private static Pattern pattern = Pattern.compile("Set-Cookie.*/;");

    //
    private static Pattern EXPIRES_PATTERN = Pattern.compile("expires=.*GMT");
    public static final String COOKIE_KEY_EXPIRES = "expires";

    private NetworkHelper() {

    }

    /**
     * 提取Cookie值
     */
    public static String pickCookies(String headers) {

        String cookies = null;

        Matcher m = pattern.matcher(headers);
        if (m.find()) {
            cookies = m.group();
        }
        //去掉cookie末尾的分号
        if(cookies != null) {
            cookies = cookies.substring(COOKIE_KEY_LENGTH + 1, cookies.length() - 1);
        }
        return cookies;
    }

    public static String parseCookie(String cookie, String name) {
        String result = null;
        if(COOKIE_KEY_EXPIRES.equals(name)) {
            Matcher m = EXPIRES_PATTERN.matcher(cookie);
            if (m.find()) {
                cookie = m.group();
                if(cookie != null) {
                    result = cookie.split("=")[1];
                }
            }

        }
        return result;
    }

}
