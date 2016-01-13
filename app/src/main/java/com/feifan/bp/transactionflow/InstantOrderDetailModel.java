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

    public InstantOrderDetailModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        Log.e("InstantOrderDetailModel", "InstantOrderDetailModel---------" + json);
        JSONArray orders = new JSONObject(json).getJSONArray("flash");
        orderDetails = new ArrayList<>();
        for (int i = 0; i < orders.length(); i++){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.totalCount = new JSONObject(json).optString("totalCount");
            orderDetail.orderNumber = orders.getJSONObject(i).optString("orderNo");
            orderDetail.trdeMoney = orders.getJSONObject(i).optString("payAmt");
            orderDetail.tradeTime = orders.getJSONObject(i).optString("orderTradeSuccessTime");
            orderDetail.refundTime = orders.getJSONObject(i).optString("orderRefundTime");
            orderDetails.add(orderDetail);
        }
    }

    public List<OrderDetail> getOrderList(){
        return orderDetails;
    }

    public static class OrderDetail{
        /**
         * 总条数
         */
        public String totalCount;
        /**
         * 订单号
         */
        public String orderNumber;
        /**
         * 交易金额（也是退款金额）
         */
        public String trdeMoney;
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
