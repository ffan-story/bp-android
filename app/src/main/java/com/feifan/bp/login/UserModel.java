package com.feifan.bp.login;

import android.util.Log;

import com.feifan.bp.base.BaseModel;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by xuchunlei on 15/6/18.
 */
public class UserModel extends BaseModel {

    public int uid;
    public int agId;
    public String user;
    public String name;
    public String authRange;
    public String authRangeType;
    public String authRangeId;
    public String loginToken;

    public UserModel(JSONObject json) {
        super(json, false);
    }

    @Override
    protected void parseData(JSONObject data) {
        uid = data.optInt("uid");
        agId = data.optInt("agId");
        user = data.optString("user");
        name = data.optString("name");
        authRange = data.optString("authRange");
        authRangeType = data.optString("authRangeType");
        authRangeId = data.optString("authRangeId");
        loginToken = data.optString("loginToken");
    }

    @Override
    protected void parseArrayData(JSONArray data) {

    }

    @Override
    public String toString() {
        return "uid=" + uid + ",agId" + agId + ",user=" + user + ",name=" + name + ",authRange=" + authRange
                + ",authRangeType=" + authRangeType + ",authRangeId=" + authRangeId + ",loginToken=" + loginToken;
    }
}
