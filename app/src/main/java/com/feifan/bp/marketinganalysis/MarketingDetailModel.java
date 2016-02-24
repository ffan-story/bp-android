package com.feifan.bp.marketinganalysis;

import android.text.TextUtils;

import com.feifan.bp.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 营销分析明细页
 */
public class MarketingDetailModel extends BaseModel {

    public static final String TAG = "MarketingDetailModel";
    public List<DetailListModel> detailList;
    public String mDetailTitle;
    public String mDetailTotal;
    public String mBeginKey;
    public String mHideOtherSubsidy;
    private DetailListModel mDetailModel;

    public MarketingDetailModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        JSONObject data = new JSONObject(json);
        detailList = new ArrayList<>();
        JSONArray array = data.optJSONArray("list");
        mDetailTitle = data.optString("couponName");
        mDetailTotal = data.optString("count");
        mBeginKey = data.optString("beginKey");
        mHideOtherSubsidy = data.optString("hideOtherSubsidy");
        if (array != null && array.length()>0) {
            for (int i = 0; i < array.length(); i++) {
                try {
                    mDetailModel = new DetailListModel();
                    mDetailModel.mDetailCouponId = array.getJSONObject(i).optString("couponId");
                    mDetailModel.mDetailFeifan = array.getJSONObject(i).optString("ffanAmount");
                    mDetailModel.mDetailThird = array.getJSONObject(i).optString("thirdAmount");
                    mDetailModel.mDetailMerchant = array.getJSONObject(i).optString("merchantAmount");
                    mDetailModel.mChargeOffStatus = array.getJSONObject(i).optString("couponStatus");
                    mDetailModel.mDetailGetTime = array.getJSONObject(i).optString("acquireTime");
                    if (TextUtils.isEmpty(array.getJSONObject(i).optString("dueTime")) ||
                            array.getJSONObject(i).optString("dueTime").trim().equals("null")) {
                        mDetailModel.mDetailValidTime = "";
                    } else {
                        mDetailModel.mDetailValidTime = array.getJSONObject(i).optString("dueTime");
                    }

                    mDetailModel.mChargeOffTime = array.getJSONObject(i).optString("verifyTime");
                    mDetailModel.mHideOtherSubsidy = mHideOtherSubsidy;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                detailList.add(mDetailModel);
            }
        }
    }

    public static class DetailListModel {
        public String mHideOtherSubsidy;

        public String mDetailCouponId;
        public String mChargeOffStatus;

        public String mDetailFeifan;
        public String mDetailThird;
        public String mDetailMerchant;

        public String mDetailGetTime;
        public String mDetailValidTime;
        public String mChargeOffTime;

    }
}
