package com.feifan.bp.home;

import com.feifan.bp.base.BaseModel;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by maning on 15/8/13.
 */
public class RefundCountModel extends BaseModel {
    private String count;
    public RefundCountModel(JSONObject json, boolean parseArrayData) {
        super(json, parseArrayData);
    }

    @Override
    protected void parseData(JSONObject data) {
    }

    @Override
    protected void parseArrayData(JSONArray data) {

    }

    @Override
    protected void parseAll(JSONObject data) {
        if (data == null) {
            return;
        }
        this.count = data.optString("totalSize");
    }

    public int getCount() {
        return Integer.parseInt(count);
    }
}
