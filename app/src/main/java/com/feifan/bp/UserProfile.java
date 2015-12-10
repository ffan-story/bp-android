package com.feifan.bp;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.JsonReader;

import com.feifan.bp.home.StoreModel;
import com.feifan.bp.login.AuthListModel.AuthItem;
import com.feifan.bp.util.IOUtil;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maning on 15/8/6.
 */
public class UserProfile {

    private static final String TAG = UserProfile.class.getSimpleName();

    private static final UserProfile INSTANCE = new UserProfile();

    public static UserProfile getInstance(){
        return INSTANCE;
    }
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
    // 偏好项键值－权限菜单
    private static final String PREFERENCE_KEY_USER_AUTH = "permission";
    // 偏好项键值－城市ID
    private static final String PREFERENCE_KEY_CITY_ID = "cityId";

    //偏好项键值－商户门店列表
    private static final String PREFERENCE_KEY_STORE_ID = "storeId";
    private static final String PREFERENCE_KEY_STORE_NAME = "storeName";
    private static final String PREFERENCE_KEY_STORE_NUM = "storeNum";

    // 偏好项键值－验证历史
    private static final String PREFERENCE_KEY_HISTORY_URL = "history";
    // 偏好项键值－权限列表字符串
    private static final String PREFERENCE_KEY_RIGHT_STRING = "rightString";

    private Context mContext;

    private UserProfile() {

    }

    /**
     * 初始化
     * <pre>
     *     使用ApplicationContext
     * </pre>
     *
     * @param context
     */
    public void initialize(Context context) {
        mContext = context.getApplicationContext();
    }


    public void setStoreList(List<StoreModel.StoreDetailModel> storeList) {
        if (storeList == null || storeList.size() < 1) {
            return;
        }
        for (int i = 0; i < storeList.size(); i++) {
            putString(PREFERENCE_KEY_STORE_ID + i, storeList.get(i).getStoreId());
            putString(PREFERENCE_KEY_STORE_NAME + i, storeList.get(i).getStoreName());
        }
        putInt(PREFERENCE_KEY_STORE_NUM, storeList.size());
    }
    public int getStoreNum(){
        return getInt(PREFERENCE_KEY_STORE_NUM);
    }

    public String getStoreId(int position){
        return getString(PREFERENCE_KEY_STORE_ID + position);
    }

    public String getStoreName(int position){
        return getString(PREFERENCE_KEY_STORE_NAME + position);
    }

    public void setAuthList(String authList) {
        putString(PREFERENCE_KEY_USER_AUTH, authList);
    }


    public List<AuthItem> getAuthList() {
        String p = getString(PREFERENCE_KEY_USER_AUTH);
        if (TextUtils.isEmpty(p)) {
            return null;
        }
        List<AuthItem> result = new ArrayList<AuthItem>();
        JsonReader reader = new JsonReader(new StringReader(p));
        try {
            reader.beginArray();
            while (reader.hasNext()) {
                result.add(AuthItem.readFrom(reader));
            }
            reader.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeQuietly(reader);
        }

        return result;
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

    public boolean isStoreUser() {
        return getAuthRangeType().equals("store");
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

    public void setHistoryUrl(String url) {
        putString(PREFERENCE_KEY_HISTORY_URL, url);
    }

    public String getHistoryUrl() {
        return getString(PREFERENCE_KEY_HISTORY_URL);
    }

    public void setRightString(String right) {
        putString(PREFERENCE_KEY_RIGHT_STRING, right);
    }

    public String getRightString() {
        return getString(PREFERENCE_KEY_RIGHT_STRING);
    }

    public void setCityId(int value) {
        putInt(PREFERENCE_KEY_CITY_ID, value);
    }

    public int getCityId() {
        return getInt(PREFERENCE_KEY_CITY_ID);
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
