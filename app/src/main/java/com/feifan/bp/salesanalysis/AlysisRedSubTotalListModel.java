package com.feifan.bp.salesanalysis;

import com.feifan.bp.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 红包分类汇总
 */
    public class AlysisRedSubTotalListModel extends BaseModel {

    public List<RedListModel> redList;
    public String mStrRedAllTotal ;
    public String  mStrRedAllFeifan;
    public String  mStrRedAllThird;
    public String  mStrRedAllVendor;
    public String  mStride2ndRow;

    private RedListModel mRedModel;

    public AlysisRedSubTotalListModel(JSONObject json) {
            super(json);
     }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        JSONObject data = new JSONObject(json);
        redList = new ArrayList<>();
        JSONArray array = data.optJSONArray("list");
        mStrRedAllTotal = data.optString("verifyNum");
        mStrRedAllFeifan = data.optString("ffanAmount");
        mStrRedAllThird = data.optString("thirdAmount");
        mStrRedAllVendor = data.optString("merchantAmount");
        mStride2ndRow = data.optString("hideOtherSubsidy");
        for (int i = 0; i < array.length(); i++){
            try {
                mRedModel = new RedListModel();
                mRedModel.mStrCouponId = array.getJSONObject(i).optString("couponId");
                mRedModel.mStrRedCouponName = array.getJSONObject(i).optString("couponName");
                mRedModel.mStrChargeOffCount = array.getJSONObject(i).optString("verifyNum");
                mRedModel.mStrRedFeifan = array.getJSONObject(i).optString("ffanAmount");
                mRedModel.mStrRedThird = array.getJSONObject(i).optString("thirdAmount");
                mRedModel.mStrRedVendor = array.getJSONObject(i).optString("merchantAmount");
                mRedModel.mStride2ndRow = mStride2ndRow;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            redList.add(mRedModel);
        }
    }

    public static class RedListModel{
        public String mStrCouponId;
        public String mStrRedCouponName;
        public String mStrChargeOffCount;
        public String mStrRedFeifan;
        public String mStrRedThird;
        public String mStrRedVendor;
        public String mStride2ndRow;
    }
}
