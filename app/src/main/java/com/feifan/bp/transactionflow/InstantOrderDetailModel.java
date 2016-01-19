package com.feifan.bp.transactionflow;

import android.util.Log;

import com.feifan.bp.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 对账管理 闪购订单明细Model
 * Created by konta on 2016/1/15.
 */
public class InstantOrderDetailModel extends BaseModel {

    List<OrderDetail> orderDetails;
    List<OrderDetail> refundDetails;

    public InstantOrderDetailModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        Log.e(TAG,"InstantOrderDetailModel-----" + json);
        JSONArray orders = new JSONObject(json).getJSONArray("flash");
        orderDetails = new ArrayList<>();
        refundDetails = new ArrayList<>();
        for (int i = 0; i < orders.length(); i++){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.totalCount = (int)new JSONObject(json).opt("totalCount");
            orderDetail.orderNumber = orders.getJSONObject(i).optString("orderNo");
            orderDetail.tradeMoney = orders.getJSONObject(i).optString("paySuccessAmt");
            orderDetail.tradeTime = orders.getJSONObject(i).optString("orderTradeSuccessTime");
            orderDetail.refundTime = orders.getJSONObject(i).optString("orderRefundAuditTime");
            orderDetails.add(orderDetail);
            if(orderDetail.refundTime == ""){
                refundDetails.add(orderDetail);
            }
        }
    }

    public List<OrderDetail> getOrderList(){
        return orderDetails;
    }

    public List<OrderDetail> getRefundOrderList(){
        return refundDetails;
    }

    public static class OrderDetail{
        /**
         * 总条数
         */
        public int totalCount;
        /**
         * 订单号
         */
        public String orderNumber;
        /**
         * 交易金额（也是退款金额）
         */
        public String tradeMoney;
        /**
         * 交易成功时间
         */
        public String tradeTime;
        /**
         * 退款时间
         */
        public String refundTime;

    }
}
