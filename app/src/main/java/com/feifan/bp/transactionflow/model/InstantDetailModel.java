package com.feifan.bp.transactionflow.model;

import com.feifan.bp.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品汇总Model
 * Created by konta on 2016/1/14.
 */
public class InstantDetailModel extends BaseModel {

    private List<InstantDetail> details;

    public InstantDetailModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        JSONArray detailList = new JSONObject(json).getJSONArray("list");
        details = new ArrayList<>();
        for(int i = 0; i < detailList.length(); i++){
            InstantDetail detail = new InstantDetail();
            detail.googsId = detailList.getJSONObject(i).optString("goodsId");
            detail.goodsName = detailList.getJSONObject(i).optString("goodsName");
            detail.payAmount = detailList.getJSONObject(i).optString("payAmt");
            detail.payCount = detailList.getJSONObject(i).optString("payCount");
            detail.refundAmount = detailList.getJSONObject(i).optString("refundAmt");
            detail.refundCount = detailList.getJSONObject(i).optString("refundCount");
            details.add(detail);
        }
    }

    public List<InstantDetail> getInstantDetailList(){
        return details;
    }
    public static class InstantDetail{
        /**
         * 商品id
         */
        public String googsId;
        /**
         * 商品名称
         */
        public String goodsName;
        /**
         * 交易金额
         */
        public String payAmount;
        /**
         * 交易笔数
         */
        public String payCount;
        /**
         * 退款金额
         */
        public String refundAmount;
        /**
         * 退款笔数
         */
        public String refundCount;

    }
}
