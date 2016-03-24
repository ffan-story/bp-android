package com.feifan.bp.transactionflowing.model;

import android.util.Log;

import com.feifan.bp.base.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 对账管理---通用券Model
 * Created by konta on 2016/1/14.
 */
public class CouponSummaryModel extends BaseModel {

    private static final String TAG = "CouponSummaryModel";
    private CouponSummary couponSummary;

    public CouponSummaryModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        Log.e(TAG, "CouponSummaryModel----" + json);
        JSONObject data = new JSONObject(json);
        JSONArray couponList = data.getJSONArray("list");

        couponSummary = new CouponSummary();
        couponSummary.totalCount = data.optInt("totalCount");
        couponSummary.awardAmount = data.optString("settleAllAmount");
        couponSummary.linkRelative = data.optString("circleDiff");
        couponSummary.couponDetailList = new ArrayList<>();
        for(int i = 0; i < couponList.length(); i++){
            CouponDetail couponDetail = new CouponDetail();
            couponDetail.CPCode = couponList.getJSONObject(i).optString("productNo");
            couponDetail.CPName = couponList.getJSONObject(i).optString("title");
            couponDetail.awardMoney = couponList.getJSONObject(i).optString("couponSettleAmount");
            couponDetail.chargeoffTime = couponList.getJSONObject(i).optString("settleTime");
            couponSummary.couponDetailList.add(couponDetail);
        }

    }

    public CouponSummary getCouponSummary() {
        return couponSummary;
    }

    public static class CouponSummary{
        /**
         * 核销总笔数
         */
        public int totalCount;
        /**
         * 奖励金额
         */
        public String awardAmount;
        /**
         * 环比
         */
        public String linkRelative;
        /**
         * 核销明细
         */
        public List<CouponDetail> couponDetailList;
    }

    public static class CouponDetail {
        /**
         * 券编码
         */
        public String CPCode;
        /**
         * 券名称
         */
        public String CPName;
        /**
         * 奖励金额
         */
        public String awardMoney;
        /**
         * 核销时间
         */
        public String chargeoffTime;
    }
}
