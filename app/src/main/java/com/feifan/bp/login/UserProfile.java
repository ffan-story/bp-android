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

    public UserProfile(Context context){
        mContext = context;
    }



/*
        data->agId               int
	data->user		string
	data->authRange	string
	data->authRangeType	string
	data->loginToken	string md5(uid.user.agid.auth_range_type.auth_range_id.时间戳)


	uid 用户编号 ， agId 权限组的ID

authRange 权限名称 （具体的商户名称或者门店名称）

15-06-17 15:36:27
authRangeType 权限的类型， merchant 代表的事权限类型事商户类型， store 代表的事门店的类型

authRangeId 代表的事 merchant ID， 或者门店的id 。

 */
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
