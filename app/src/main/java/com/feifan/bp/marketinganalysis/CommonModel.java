package com.feifan.bp.marketinganalysis;

import com.feifan.bp.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kontar on 2016/2/17.
 */
public class CommonModel extends BaseModel {

    public static final String TAG = "CommonModel";
    public String mVerifyNum;
    public String mAwardAmount;
    public List<CommonDetail> mCommonDetails;
    private CommonDetail detail;
    public CommonModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        JSONObject data = new JSONObject(json);
        JSONArray details = data.getJSONArray("list");
        mVerifyNum = data.optString("verifyNum");
        mAwardAmount = data.optString("ffanAmount");
        mCommonDetails = new ArrayList<>();
        if(null != details && details.length() > 0){
            for(int i = 0; i < details.length(); i++){
                detail= new CommonDetail();
                detail.mCouponId = details.getJSONObject(i).optString("couponId");
                detail.mAwardMoney = details.getJSONObject(i).optString("ffanAmount");
                detail.mCouponName = details.getJSONObject(i).optString("couponName");
                detail.mVerifyTime = details.getJSONObject(i).optString("verifyTime");
                mCommonDetails.add(detail);
            }
        }
    }

    public class CommonDetail{
        public String mCouponId;
        public String mAwardMoney;
        public String mCouponName;
        public String mVerifyTime;
    }
}
