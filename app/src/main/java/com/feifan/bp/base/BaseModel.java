package com.feifan.bp.base;

import com.feifan.bp.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by xuchunlei on 15/7/9.
 */
public abstract class BaseModel {
    private int status;
    private String msg;

    public BaseModel(JSONObject json, boolean parseArrayData) {
        status = json.optInt("status");
        if (status == 0) {
            status = Constants.RESPONSE_CODE_SUCCESS;
        }
        msg = json.optString("msg");
        if(status == Constants.RESPONSE_CODE_SUCCESS) {
            parseAll(json);
            if (parseArrayData) {
                parseArrayData(json.optJSONArray("data"));
            } else {
                parseData(json.optJSONObject("data"));
            }

        }
    }

    public String getMsg() {
        return msg;
    }

    public int getStatus() {
        return status;
    }

    protected abstract void parseData(JSONObject data);

    protected abstract void parseArrayData(JSONArray data);

    @Override
    public String toString() {
        return "status=" + status + ",msg=" + msg;
    }

    protected void parseAll(JSONObject data) {

    }
}
