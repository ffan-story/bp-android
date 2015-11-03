package com.feifan.bp.home;

import com.feifan.bp.network.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * congjing
 */
public class MessageStatusModel extends BaseModel {
    private String status;
    private String message;

    public MessageStatusModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        JSONObject data = new JSONObject(json);
        setMessage(data.optString("message"));
        setStatus(data.optString("status"));
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
