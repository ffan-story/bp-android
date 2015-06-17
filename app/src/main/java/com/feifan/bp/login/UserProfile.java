package com.feifan.bp.login;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 用户配置文件
 * <p>
 *     缓存用户登录信息，退出登录时需要清空该缓存
 * </p>
 * Created by xuchunlei on 15/6/17.
 */
public class UserProfile {

    // 偏好文件名
    private static final String PREFERENCE_NAME = "profile";
    // 偏好项键值－用户编号
    private static final String PREFERENCE_KEY_UID = "uid";

    private UserProfile(){

    }
/*
	data->uid		int
        data->agId               int
	data->user		string
	data->name		string
	data->authRange	string
	data->authRangeType	string
	data->authRangeId	string
	data->loginToken	string md5(uid.user.agid.auth_range_type.auth_range_id.时间戳)


	uid 用户编号 ， agId 权限组的ID

authRange 权限名称 （具体的商户名称或者门店名称）

15-06-17 15:36:27
authRangeType 权限的类型， merchant 代表的事权限类型事商户类型， store 代表的事门店的类型

authRangeId 代表的事 merchant ID， 或者门店的id 。

 */
    public static void setUid(Context context, int uid) {
        doSetInteger(context, PREFERENCE_KEY_UID, uid);
    }

    /**
     * 设置整型数据
     * @param context
     * @param key
     * @param value
     */
    private static void  doSetInteger(Context context, String key, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();
    }
}
