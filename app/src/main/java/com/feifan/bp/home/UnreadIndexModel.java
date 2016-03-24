package com.feifan.bp.home;

import com.feifan.bp.base.network.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 消息未读数
 * Created by congjing
 */
public class UnreadIndexModel extends BaseModel {
    public UnreadIndexModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        JSONObject data = new JSONObject(json);
    }


}
