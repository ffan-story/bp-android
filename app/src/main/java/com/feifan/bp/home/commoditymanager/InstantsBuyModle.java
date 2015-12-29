package com.feifan.bp.home.commoditymanager;

import com.feifan.bp.network.BaseModel;
import com.feifan.bp.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by konta on 2015/12/25.
 */
public class InstantsBuyModle extends BaseModel{
    public static final String TAG = "InstantsBuyModle";
    public InstantsBuyModle(JSONObject json) {
        super(json);
    }
    private List<Commodity> commodities;
    @Override
    protected void parseData(String json) throws JSONException {
        LogUtil.e(TAG, "we get json ===========" + json);
        commodities = new ArrayList<Commodity>();
        JSONArray data = new JSONObject(json).getJSONArray("data");
        for (int i = 0;i < data.length();i++){
            Commodity commodity  = new Commodity();
            commodity.status = data.getJSONObject(i).optString("status");
            commodity.totalCount = data.getJSONObject(i).optInt("totalCount");
            commodities.add(commodity);
        }
    }
    public List<Commodity> getCommodities(){
        return commodities;
    }
    public class Commodity{
        public String status;
        public int totalCount;
    }
}
