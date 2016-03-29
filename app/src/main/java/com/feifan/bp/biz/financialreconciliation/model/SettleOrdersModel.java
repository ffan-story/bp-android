package com.feifan.bp.biz.financialreconciliation.model;

import android.util.Log;

import com.feifan.bp.base.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 对账详情 结算单
 * Created by konta on 2016/3/23.
 */
public class SettleOrdersModel extends BaseModel {

    public List<SettleOrder> settleDetails; //订单列表
    public SettleOrdersModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        JSONObject jsonObject = new JSONObject(json).getJSONObject("settleData");
        Log.e("SettleDetailModel", "we got json" + json);
        JSONArray array = jsonObject.optJSONArray("list");
        if(array !=  null && array.length() > 0){
            settleDetails = new ArrayList<>();
            SettleOrder settleDetail;
            for(int i = 0; i < array.length(); i++){
                settleDetail = new SettleOrder();
                try{
                    settleDetail.mSettleOrderNo = array.getJSONObject(i).optString("settleNo");
                    settleDetail.mSettleCycle = array.getJSONObject(i).optString("settleCycle");
                    settleDetail.mSettleCount = array.getJSONObject(i).optString("billNum");
                    settleDetail.mPayAmount = array.getJSONObject(i).optString("payAmt");
                    settleDetail.mRefundAmount = array.getJSONObject(i).optString("refundAmt");
                    settleDetail.mPlatformSub = array.getJSONObject(i).optString("operatorCutAmt");
                    settleDetail.mThirdSub = array.getJSONObject(i).optString("thirdCutAmt");
                    settleDetail.mFeeAmount = array.getJSONObject(i).optString("fee");
                    settleDetail.mSettleMoney = array.getJSONObject(i).optString("settleAmt");
                    settleDetails.add(settleDetail);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static class SettleOrder{
        public String mSettleOrderNo;   //结算单号
        public String mSettleCycle;     //结算周期
        public String mSettleCount;     //交易笔数
        public String mPayAmount;       //用户付款金额
        public String mRefundAmount;    //用户退款金额
        public String mPlatformSub;     //平台补贴金额
        public String mThirdSub;        //第三方补贴金额
        public String mFeeAmount;       //佣金
        public String mSettleMoney;     //结算金额
    }
}
