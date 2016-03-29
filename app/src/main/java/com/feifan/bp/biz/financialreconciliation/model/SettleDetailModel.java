package com.feifan.bp.biz.financialreconciliation.model;

import android.util.Log;

import com.feifan.bp.base.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单详情
 * Created by konta on 2016/3/24.
 */
public class SettleDetailModel extends BaseModel {

    public String mTotalCount;  //总笔数
    public List<SettleDetail> settleDetails;

    public SettleDetailModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        JSONObject jsonObject = new JSONObject(json);
        Log.e("SettleDetailModel","we got json" + json);
        mTotalCount = jsonObject.optString("totalCount");
        JSONArray array = jsonObject.optJSONArray("list");
        if(array !=  null && array.length() > 0){
            settleDetails = new ArrayList<>();
            SettleDetail settleDetail;
            for(int i = 0; i < array.length(); i++){
                settleDetail = new SettleDetail();
                try{
                    settleDetail.mOrderNo = array.getJSONObject(i).optString("orderNo");
                    settleDetail.mPayAmount = array.getJSONObject(i).optString("payAmt");
                    settleDetail.mRefundAmount = array.getJSONObject(i).optString("refundAmt");
                    settleDetail.mPlatformSub = array.getJSONObject(i).optString("operatorCutAmt");
                    settleDetail.mThirdSub = array.getJSONObject(i).optString("thirdCutAmt");
                    settleDetail.mFeeAmount = array.getJSONObject(i).optString("fee");
                    settleDetail.mSettleMoney = array.getJSONObject(i).optString("settleMoney");
                    settleDetail.orderType = array.getJSONObject(i).optString("orderType");
                    settleDetails.add(settleDetail);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static class SettleDetail{
        public String mOrderNo;         //订单号
        public String mPayAmount;       //用户付款金额
        public String mRefundAmount;    //用户退款金额
        public String mPlatformSub;     //平台补贴金额
        public String mThirdSub;        //第三方补贴金额
        public String mFeeAmount;       //佣金
        public String mSettleMoney;     //结算金额
        public String orderType;        //订单类型  1:正向  2:逆向(退款)
    }
}
