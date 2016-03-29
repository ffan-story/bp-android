package com.feifan.bp.biz.salesmanagement;

import com.feifan.bp.base.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Frank on 16/1/7.
 */
public class GoodsStatusModel extends BaseModel {

    public Map<String, String> mGoodsStatus;

    public GoodsStatusModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        JSONObject data = new JSONObject(json);

        mGoodsStatus = new HashMap<>();
        JSONArray array = data.getJSONArray("approveStatusList");
        for (int i = 0; i < array.length(); i++) {
            try {
                mGoodsStatus.put(array.getJSONObject(i).getString("approveStatus"), String.valueOf(array.getJSONObject(i).getInt("count")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
