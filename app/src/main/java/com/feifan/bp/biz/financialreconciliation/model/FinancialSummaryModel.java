package com.feifan.bp.biz.financialreconciliation.model;

import android.util.Log;

import com.feifan.bp.base.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 对账首页
 * Created by konta on 2016/3/23.
 */
public class FinancialSummaryModel extends BaseModel {

    public String mTotalCount;      //总笔数
    public List<FinancialSummary> summaries;    //详情列表
    public FinancialSummaryModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        JSONObject jsonObject = new JSONObject(json);
        Log.e("FinancialSummaryModel","we got json --- " + json);
        mTotalCount = jsonObject.optString("totalCount");
        JSONArray array = jsonObject.optJSONArray("list");
        if(array != null && array.length() > 0){
            summaries = new ArrayList<>();
            FinancialSummary summary;
            for (int i = 0; i < array.length(); i++){
                summary = new FinancialSummary();
                try {
                    summary.mReNo = array.getJSONObject(i).optString("reNo");
                    summary.mSettleSub = array.getJSONObject(i).optString("settleBody");
                    summary.mSettleCount = array.getJSONObject(i).optString("releaseNum");
                    summary.mSettleAmount = array.getJSONObject(i).optString("releaseAmt");
                    summary.mAdjustAmount = array.getJSONObject(i).optString("transferAmt");
                    summaries.add(summary);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static class FinancialSummary{
        public String mReNo;            //对账单号
        public String mSettleSub;       //结算主体
        public String mSettleCount;     //结算笔数
        public String mSettleAmount;    //结算金额
        public String mAdjustAmount;    //调账金额
    }
}
