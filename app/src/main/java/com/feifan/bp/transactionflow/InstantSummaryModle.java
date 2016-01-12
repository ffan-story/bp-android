package com.feifan.bp.transactionflow;

import com.feifan.bp.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by konta on 2016/1/12.
 */
public class InstantSummaryModle extends BaseModel{

    InstantSummary instantSummary;

    public InstantSummaryModle(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);

        JSONArray array = new JSONObject(json).getJSONArray("flash");

        instantSummary = new InstantSummary();
        instantSummary.tradeMoney = array.getJSONObject(1).getString("amountValue");
        instantSummary.tradeCount = array.getJSONObject(1).getString("countValue");
        instantSummary.refundMoney = array.getJSONObject(3).getString("amountValue");
        instantSummary.refundCount = array.getJSONObject(3).getString("countValue");
    }

    public InstantSummary getInstantSummary(){
        return instantSummary;
    }

    public class InstantSummary{
        /**
         * 交易成功金额
         */
        public String tradeMoney;
        /**
         * 交易成功笔数
         */
        public String tradeCount;
        /**
         * 退款总金额
         */
        public String refundMoney;
        /**
         * 退款总笔数
         */
        public String refundCount;
    }
}
