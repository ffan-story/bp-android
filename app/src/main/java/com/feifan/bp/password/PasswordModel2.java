package com.feifan.bp.password;

import com.feifan.bp.base.BaseModel;

import org.json.JSONObject;

/**
 * Created by maning on 15/7/29.
 */
public class PasswordModel2 extends BaseModel {
    public String authCode = "";
    public String key = "";

    public PasswordModel2(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(JSONObject data) {
        if (data == null) {
            return;
        }
        authCode = data.optString("authCode");
        key = data.optString("key");
    }
}
