package com.feifan.bp.FeedBack;

import com.feifan.bp.base.BaseModel;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by tianjun on 2015-10-29.
 */
public class FeedBackModel extends BaseModel {
    public FeedBackModel(JSONObject json) {
        super(json, false);
    }

    @Override
    protected void parseData(JSONObject data) {

    }

    @Override
    protected void parseArrayData(JSONArray data) {

    }
}
