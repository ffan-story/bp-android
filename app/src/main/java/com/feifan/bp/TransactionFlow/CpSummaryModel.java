package com.feifan.bp.TransactionFlow;

import com.feifan.bp.network.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Frank on 15/11/10.
 */
public class CpSummaryModel extends BaseModel {

    public String total;
    public String total_text;
    public String totalAmount;
    public String totalAmount_text;

    public CpSummaryModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        JSONObject data = new JSONObject(json);

        total = data.optString("total");
        total_text = data.optString("total_text");
        totalAmount = data.optString("totalAmount");
        totalAmount_text = data.optString("totalAmount_text");
    }
}
