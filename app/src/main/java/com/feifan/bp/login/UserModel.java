package com.feifan.bp.login;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by xuchunlei on 15/6/18.
 */
public class UserModel {

    public int uid;
    public int agId;
    public String user;
    public String name;
    public String authRange;
    public String authRangeType;
    public String authRangeId;
    public String loginToken;

    public UserModel(JSONObject json) {
        uid = json.optInt("uid");
        agId = json.optInt("agId");
        user = json.optString("user");
        name = json.optString("name");
        authRange = json.optString("authRange");
        authRangeType = json.optString("authRangeType");
        authRangeId = json.optString("authRangeId");
        loginToken = json.optString("loginToken");
        Log.i("xuchunlei", toString());
    }

    @Override
    public String toString() {
        return "uid=" + uid + ",agId" + agId + ",user=" + user + ",name=" + name + ",authRange=" + authRange
                + ",authRangeType=" + authRangeType + ",authRangeId=" + authRangeId + ",loginToken=" + loginToken;
    }
}
