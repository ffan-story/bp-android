package com.feifan.bp.home;

import com.feifan.bp.network.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * congjing
 */
public class MessageStatusModel extends BaseModel {
    public MessageStatusModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);
    }
}
