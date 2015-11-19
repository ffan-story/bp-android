package com.feifan.bp.password;

import com.feifan.bp.network.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by maning on 15/7/29.
 */
public class PasswordModel extends BaseModel {
    private String authCode;
    private String key;

    public PasswordModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        JSONObject data = new JSONObject(json);
        setAuthCode(data.optString("authCode"));
        setKey(data.optString("key"));
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

    @Override
    public String toString() {
        return "authCode=" + authCode + ",key=" + key;
    }
}
