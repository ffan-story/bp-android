package com.feifan.bp.login;

import android.content.Context;
import android.content.SharedPreferences;

import com.feifan.bp.Constants;

/**
 * 用户配置文件
 * <p>
 *     缓存用户登录信息，退出登录时需要清空该缓存
 * </p>
 * <p>
 *     不要任意创建该对象的实例，请使用PlatformState提供的UserProfile实例
 * </p>
 * Created by xuchunlei on 15/6/17.
 */
public class UserProfile {

    private Context mContext;

    // 偏好文件名
    private static final String PREFERENCE_NAME = "profile";
    // 偏好项键值－用户编号
    private static final String PREFERENCE_KEY_UID = "uid";
    // 偏好项键值－账号
    private static final String PREFERENCE_KEY_USER = "user";
    // 偏好项键值－商户或门店ID
    private static final String PREFERENCE_KEY_AUTH_RANGE_ID = "authRangeId";
    // 偏好项键值－用户类型
    private static final String PREFERENCE_KEY_AUTH_RANGE_TYPE = "authRangeType";
    // 偏好项键值－权限组ID
    private static final String PREFERENCE_KEY_AGID = "agId";
    // 偏好项键值－访问token
    private static final String PREFERENCE_KEY_LOGIN_TOKEN = "loginToken";

    public UserProfile(Context context){
        mContext = context;
    }

    public void setUid(int uid) {
        doSetInteger(PREFERENCE_KEY_UID, uid);
    }

    public int getUid() {
        return doGetInteger(PREFERENCE_KEY_UID);
    }

    public void setUser(String user) {
        doSetString(PREFERENCE_KEY_USER, user);
    }

    public String getUser() {
        return doGetString(PREFERENCE_KEY_USER);
    }

    public void setAuthRangeId(String authRangeId) {
        doSetString(PREFERENCE_KEY_AUTH_RANGE_ID, authRangeId);
    }

    public String getAuthRangeId() {
        return doGetString(PREFERENCE_KEY_AUTH_RANGE_ID);
    }

    public void setAuthRangeType(String authRangeType) {
        doSetString(PREFERENCE_KEY_AUTH_RANGE_TYPE, authRangeType);
    }

    public String getAuthRangeType() {
        return doGetString(PREFERENCE_KEY_AUTH_RANGE_TYPE);
    }

    public void setAgId(int agId) {
        doSetInteger(PREFERENCE_KEY_AGID, agId);
    }

    public int getAgId() {
        return doGetInteger(PREFERENCE_KEY_AGID);
    }

    public void setLoginToken(String token) {
        doSetString(PREFERENCE_KEY_LOGIN_TOKEN, token);
    }

    public String getLoginToken(){
        return doGetString(PREFERENCE_KEY_LOGIN_TOKEN);
    }

    public void clear() {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }

    /**
     * 设置整型数据
     * @param key
     * @param value
     */
    private void  doSetInteger(String key, int value) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * 获取整型数据
     * @param key
     * @return
     */
    private int doGetInteger(String key) {
        SharedPreferences prefs = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(key, Constants.NO_INTEGER);
    }
    /**
     * 设置整型数据
     * @param key
     * @param value
     */
    private void doSetString(String key, String value) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 获取整型数据
     * @param key
     * @return
     */
    private String doGetString(String key) {
        SharedPreferences prefs = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(key, Constants.NO_STRING);
    }
}
