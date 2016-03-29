package com.feifan.bp.biz.financialreconciliation.model;

import android.util.Log;

import com.feifan.bp.base.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 对账单详情 -- 调账信息
 * Created by konta on 2016/3/23.
 */
public class AdjustOrderModel extends BaseModel {

    public String mTotalCount;      //总笔数
    public List<AdjustOrder> adjustOrders; //调账单列表

    public AdjustOrderModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        JSONObject jsonObject = new JSONObject(json).getJSONObject("adjustData");
        Log.e("AdjustOrderModel","we got json--" + json);
        mTotalCount = jsonObject.optString("totalCount");
        JSONArray array = jsonObject.optJSONArray("list");
        if(array != null && array.length() > 0){
            adjustOrders = new ArrayList<>();
            AdjustOrder adjustOrder;
            for (int i = 0; i < array.length(); i++){
                adjustOrder = new AdjustOrder();
                try {
                    adjustOrder.mAdjustNo = array.getJSONObject(i).optString("adjustNo");
                    adjustOrder.mSettleNo = array.getJSONObject(i).optString("settleNo");
                    adjustOrder.mAdjustAmount = array.getJSONObject(i).optString("adjustAmt");
                    adjustOrder.mAdjustExplain = array.getJSONObject(i).optString("adjustRemark");
                    adjustOrders.add(adjustOrder);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static class AdjustOrder{
        public String mAdjustNo;        //调账流水
        public String mSettleNo;        //结算单号
        public String mAdjustAmount;    //调账金额
        public String mAdjustExplain;   //调账说明
    }

}
