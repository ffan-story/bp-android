package com.feifan.bp;

import org.json.JSONObject;

/**
 * Created by xuchunlei on 15/7/9.
 */
public abstract class BaseModel {
    public int status;
    public String msg;

    public BaseModel(JSONObject json) {
        status = json.optInt("status");
        msg = json.optString("msg");
        if(status == Constants.RESPONSE_CODE_SUCCESS) {
            parseData(json.optJSONObject("data"));
        }
    }

    protected abstract void parseData(JSONObject data);

    @Override
    public String toString() {
        return "status=" + status + ",msg=" + msg;
    }
}
