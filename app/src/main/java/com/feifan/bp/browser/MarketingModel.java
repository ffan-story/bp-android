package com.feifan.bp.browser;

import android.text.TextUtils;

import com.feifan.bp.envir.EnvironmentManager;
import com.feifan.bp.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 判断是否签订营销合同
 * Created by congjing on 15-12-7.
 */
public class MarketingModel extends BaseModel {

    /**
     * 是否有合同
     */
    public int hasContract;

    public MarketingModel(JSONObject json) {
        super(json);
    }

    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        JSONObject data = new JSONObject(json);
        hasContract = data.optInt("hasContract");
    }

}
