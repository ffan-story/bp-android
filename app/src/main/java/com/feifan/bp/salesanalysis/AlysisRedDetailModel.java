package com.feifan.bp.salesanalysis;

import com.feifan.bp.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 红包核销明细
 */
//public class AlysisRedDetailModel{
   public class AlysisRedDetailModel extends BaseModel {

    public List<RedDetailModel> redDetailList;
    public String mStrRedTitle;
    public String mStrRedTotal;
    public String mStrBeginKey;
    public String  mStride2ndRow;
    private RedDetailModel mRedDetailModel;

    public AlysisRedDetailModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        JSONObject data = new JSONObject(json);

        redDetailList = new ArrayList<>();
        JSONArray array = data.optJSONArray("list");
        mStrRedTitle = data.optString("couponName");
        mStrRedTotal = data.optString("count");
        mStrBeginKey = data.optString("beginKey");
        mStride2ndRow = data.optString("hideOtherSubsidy");
        for (int i = 0; i < array.length(); i++){
            try {
                mRedDetailModel = new RedDetailModel();
                mRedDetailModel.mStrCouponId = array.getJSONObject(i).optString("couponId");
                mRedDetailModel.mStrFeifan = array.getJSONObject(i).optString("ffanAmount");
                mRedDetailModel.mStrThird = array.getJSONObject(i).optString("thirdAmount");
                mRedDetailModel.mStrVendor = array.getJSONObject(i).optString("merchantAmount");
                mRedDetailModel.mStrStatus = array.getJSONObject(i).optString("couponStatus");
                mRedDetailModel.mStrGetTime = array.getJSONObject(i).optString("acquireTime");
                mRedDetailModel.mStrValidTime = array.getJSONObject(i).optString("dueTime");
                mRedDetailModel.mStrUseTime = array.getJSONObject(i).optString("verifyTime");
                mRedDetailModel.mStride2ndRow = mStride2ndRow;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            redDetailList.add(mRedDetailModel);
        }
    }

    public static class RedDetailModel{
        public String  mStride2ndRow;

        public String mStrCouponId;
        public String mStrStatus;

        public String mStrFeifan;
        public String mStrThird;
        public String mStrVendor;

        public String mStrGetTime;
        public String mStrValidTime;
        public String mStrUseTime;

    }
}
