package com.feifan.bp.marketinganalysis;

import android.text.TextUtils;
import android.util.Log;

import com.feifan.bp.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 红包分类汇总
 */
    public class MarketingSummaryModel extends BaseModel {

    public static final String TAG = "SummaryModel";
    /**
     * home页数据
     */
    public String mHomeAllVerifyNum;
    public String mHomeRedVerifyNum;
    public String mHomeShakeVerifyNum;
    public String mHomeCommonVerifyNum;
    public String mHomeCouponVerifyNum;
    /**
     * 汇总页数据
     */
    public String mSummaryAllTotal;
    public String mSummaryAllFeifan;
    public String mSummaryAllThird;
    public String mSummaryAllMerchantr;
    public String mSummaryHideOtherSubsidy;
    public List<SummaryListModel> summaryList;

    public SummaryListModel mRedModel;

    public MarketingSummaryModel(JSONObject json) {
            super(json);
     }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        JSONObject data = new JSONObject(json);
        Log.e(TAG,"we got json : " + json);

        mHomeAllVerifyNum = data.optString("allVerifyNum");
        mHomeRedVerifyNum = data.optString("hbVerifyNum");
        mHomeShakeVerifyNum = data.optString("yyVerifyNum");
        mHomeCommonVerifyNum = data.optString("tyVerifyNum");
        mHomeCouponVerifyNum = data.optString("yhVerifyNum");

        summaryList = new ArrayList<>();
        JSONArray array = data.optJSONArray("list");
        mSummaryAllTotal = TextUtils.isEmpty(data.optString("verifyNum"))? "0":data.optString("verifyNum");
        mSummaryAllFeifan = data.optString("ffanAmount");
        mSummaryAllThird = data.optString("thirdAmount");
        mSummaryAllMerchantr = data.optString("merchantAmount");
        mSummaryHideOtherSubsidy = data.optString("hideOtherSubsidy");

        if (array != null && array.length()>0){
            for (int i = 0; i < array.length(); i++){
                try {
                    mRedModel = new SummaryListModel();
                    if("0".equals(array.getJSONObject(i).optString("verifyNum"))){
                        continue;
                    }
                    mRedModel.mListCouponId = array.getJSONObject(i).optString("couponId");
                    mRedModel.mListCouponName = array.getJSONObject(i).optString("couponName");
                    mRedModel.mListChargeOffCount = array.getJSONObject(i).optString("verifyNum");
                    mRedModel.mListFeifan = array.getJSONObject(i).optString("ffanAmount");
                    mRedModel.mListThird = array.getJSONObject(i).optString("thirdAmount");
                    mRedModel.mListMerchant = array.getJSONObject(i).optString("merchantAmount");
                    mRedModel.mListHideOtherSubsidy = mSummaryHideOtherSubsidy;
                    mRedModel.mVerifyTime = array.getJSONObject(i).optString("");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                summaryList.add(mRedModel);
            }
        }
    }

    public static class SummaryListModel {
        public String mListCouponId;
        public String mListCouponName;
        public String mListChargeOffCount;
        public String mListFeifan;
        public String mListThird;
        public String mListMerchant;
        public String mListHideOtherSubsidy;
        public String mVerifyTime;
    }
}
