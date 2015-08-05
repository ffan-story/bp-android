package com.feifan.bp.password;

import com.feifan.bp.base.BaseModel;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by maning on 15/7/29.
 */
public class PasswordModel extends BaseModel {
    public String authCode = "";
    public String key = "";

    public PasswordModel(JSONObject json) {
        super(json, false);
    }

    @Override
    protected void parseData(JSONObject data) {
        if (data == null) {
            return;
        }
        authCode = data.optString("authCode");
        key = data.optString("key");
    }

    @Override
    protected void parseArrayData(JSONArray data) {

    }
}
