package com.feifan.bp.account;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.feifan.bp.Constants;
import com.feifan.bp.LogUtil;
import com.feifan.bp.net.AuthList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by maning on 15/8/6.
 */
public class AccountManager {

    private static final String TAG = "AccountManager";

    private static AccountManager sAccountManager;

    public static AccountManager instance(Context context) {
        if (sAccountManager == null) {
            sAccountManager = new AccountManager(context);
        }
        return sAccountManager;
    }

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

    private static final String PREFERENCE_KEY_PERMISSIONS = "permission";

    private AccountManager(Context context) {
        mContext = context.getApplicationContext();
    }

    public void setPermissionList(List<String> permissionList) {
        if (permissionList == null || permissionList.size() < 1) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (String p : permissionList) {
            sb.append(p).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);

        putString(PREFERENCE_KEY_PERMISSIONS, sb.toString());
    }

    public void setPermissionUrlMap(Map<String, String> map) {
        if (map == null || map.size() < 1) {
            return;
        }
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            LogUtil.i(TAG, "id=" + key);
            if (AuthList.AUTH_LIST.containsKey(key) || AuthList.HISTORY_ID.equals(key)) {
                putString(key, map.get(key));
            }
        }

    }

    public String getPermissionUrl(String key) {
        return getString(key);
    }

    public List<String> getPermissionList() {
        String p = getString(PREFERENCE_KEY_PERMISSIONS);
        if (TextUtils.isEmpty(p)) {
            return null;
        }
        String[] permissions = p.split(",");
        return Arrays.asList(permissions);
    }

    public void setUid(int uid) {
        putInt(PREFERENCE_KEY_UID, uid);
    }

    public int getUid() {
        return getInt(PREFERENCE_KEY_UID);
    }

    public void setUser(String user) {
        putString(PREFERENCE_KEY_USER, user);
    }

    public String getUser() {
        return getString(PREFERENCE_KEY_USER);
    }

    public void setAuthRangeId(String authRangeId) {
        putString(PREFERENCE_KEY_AUTH_RANGE_ID, authRangeId);
    }

    public String getAuthRangeId() {
        return getString(PREFERENCE_KEY_AUTH_RANGE_ID);
    }

    public void setAuthRangeType(String authRangeType) {
        putString(PREFERENCE_KEY_AUTH_RANGE_TYPE, authRangeType);
    }

    public String getAuthRangeType() {
        return getString(PREFERENCE_KEY_AUTH_RANGE_TYPE);
    }

    public void setAgId(int agId) {
        putInt(PREFERENCE_KEY_AGID, agId);
    }

    public int getAgId() {
        return getInt(PREFERENCE_KEY_AGID);
    }

    public void setLoginToken(String token) {
        putString(PREFERENCE_KEY_LOGIN_TOKEN, token);
    }

    public String getLoginToken() {
        return getString(PREFERENCE_KEY_LOGIN_TOKEN);
    }

    public void clear() {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }


    private void putInt(String key, int value) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private int getInt(String key) {
        SharedPreferences prefs = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(key, Constants.NO_INTEGER);
    }

    private void putString(String key, String value) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    private String getString(String key) {
        SharedPreferences prefs = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(key, Constants.NO_STRING);
    }
}
