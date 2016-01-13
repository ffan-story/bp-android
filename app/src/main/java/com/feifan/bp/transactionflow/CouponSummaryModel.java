package com.feifan.bp.transactionflow;

import android.util.Log;

import com.feifan.bp.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        JSONObject data = new JSONObject(json).getJSONObject("data");
        JSONArray couponList = data.getJSONArray("list");

        couponSummary = new CouponSummary();
        couponSummary.totalCount = data.getInt("totalCount");
        couponSummary.awardAmount = data.getInt("totalAmount");
        couponSummary.linkRelative = data.getString("circleDiff");
        for(int i = 0; i < couponList.length(); i++){
            CouponDetail couponDetail = new CouponDetail();
            couponDetail.CPCode = couponList.getJSONObject(i).optString("settleNo");
            couponDetail.CPName = couponList.getJSONObject(i).optString("title");
            couponDetail.awardMoney = (float)couponList.getJSONObject(i).opt("settleNo");
            couponDetail.chargeoffTime = (float)couponList.getJSONObject(i).opt("settleNo");
            couponSummary.couponDetailList.add(couponDetail);
        }

    }

    public CouponSummary getCouponSummary() {
        return couponSummary;
    }

    public class CouponSummary{
        /**
         * 核销总笔数
         */
        public int totalCount;
        /**
         * 奖励金额
         */
        public float awardAmount;
        /**
         * 环比
         */
        public String linkRelative;
        /**
         * 核销明细
         */
        public List<CouponDetail> couponDetailList;
    }

    public class CouponDetail {
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
        public float awardMoney;
        /**
         * 核销时间
         */
        public float chargeoffTime;
    }
}
