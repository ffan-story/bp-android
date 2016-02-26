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
    public String mHomeAllVerifyNum;    //核销总笔数
    public String mHomeRedVerifyNum;    //红包核销笔数
    public String mHomeShakeVerifyNum;  //摇一摇核销笔数
    public String mHomeCommonVerifyNum; //通用券核销笔数
    public String mHomeCouponVerifyNum; //优惠券核销笔数
    /**
     * 汇总页数据
     */
    public String mSummaryAllTotal;     //核销总笔数
    public String mSummaryAllFeifan;    //非凡总补贴
    public String mSummaryAllThird;     //第三方总补贴
    public String mSummaryAllMerchantr; //商户总补贴
    public String mSummaryHideOtherSubsidy; //显示隐藏第三方和商户补贴 0：显示  1：隐藏
    public List<SummaryListModel> summaryList;  //明细列表

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
        public String mListCouponId;        //券码
        public String mListCouponName;      //券名称
        public String mListChargeOffCount;  //核销笔数
        public String mListFeifan;          //非凡补贴
        public String mListThird;           //第三方补贴
        public String mListMerchant;        //商户补贴
        public String mListHideOtherSubsidy;  //显示隐藏第三方和商户补贴 0：显示  1：隐藏
        public String mVerifyTime;      //核销时间
    }
}
