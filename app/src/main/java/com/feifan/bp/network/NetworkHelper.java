package com.feifan.bp.network;

import com.feifan.bp.util.LogUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Cookie工具类
 * Created by xuchunlei on 16/2/26.
 */
public class NetworkHelper {

    private static Pattern pattern = Pattern.compile("Set-Cookie.*/;");

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
            cookies = cookies.substring(11, cookies.length() - 1);
        }
        return cookies;
    }

}
