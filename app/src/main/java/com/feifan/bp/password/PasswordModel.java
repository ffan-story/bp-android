package com.feifan.bp.password;

import com.feifan.bp.base.BaseModel;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by maning on 15/7/29.
 */
public class PasswordModel extends BaseModel {
    private String authCode;
    private String key;

    public PasswordModel(JSONObject json) {
        super(json, false);
    }

    @Override
    protected void parseData(JSONObject data) {
        if (data == null) {
            return;
        }
        setAuthCode(data.optString("authCode"));
        setKey(data.optString("key"));
    }


    @Override
    protected void parseArrayData(JSONArray data) {

    }

    private void setAuthCode(String authCode){
        this.authCode = authCode;
    }
    public String getAuthCode(){
        return  authCode;
    }

    private void setKey(String key){
        this.key = key;
    }
    public String getKey(){
        return  key;
    }
}
