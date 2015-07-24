package com.feifan.bp.base;

import com.feifan.bp.Constants;

import org.json.JSONObject;

/**
 * Created by xuchunlei on 15/7/9.
 */
public abstract class BaseModel {
    private int status;
    private String msg;

    public BaseModel(JSONObject json) {
        status = json.optInt("status");
        msg = json.optString("msg");
        if(status == Constants.RESPONSE_CODE_SUCCESS) {
            parseData(json.optJSONObject("data"));
        }
    }

    public String getMsg() {
        return msg;
    }

    public int getStatus() {
        return status;
    }

    protected abstract void parseData(JSONObject data);

    @Override
    public String toString() {
        return "status=" + status + ",msg=" + msg;
    }
}
