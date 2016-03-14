package com.feifan.bp.marketinganalysis;

import android.util.Log;

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
    public String mVerifyNum;   //核销总笔数
    public String mAwardAmount; //奖励金额
    public List<CommonDetail> mCommonDetails;   //明细列表
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
        public String mCouponId;    //券码
        public String mAwardMoney;  //奖励金额
        public String mCouponName;  //券名称
        public String mVerifyTime;  //核销时间
    }
}
