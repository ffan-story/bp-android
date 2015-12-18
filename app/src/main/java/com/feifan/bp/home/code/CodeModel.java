package com.feifan.bp.home.code;

import com.feifan.bp.network.BaseModel;
import com.feifan.bp.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by konta on 15-11-2.
 */
public class CodeModel extends BaseModel {
    private final String TAG = "CodeModel";
    private CouponsData couponsData;

    public CodeModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONObject dataJson = jsonObject.getJSONObject("data");
        couponsData = new CouponsData();
        couponsData.setBuyTime(dataJson.getString("buyTime"));
        couponsData.setCertificateNo(dataJson.getString("certificateNo"));
        couponsData.setMemberId(dataJson.getString("memberId"));
        couponsData.setRemark(dataJson.getString("remark"));
        couponsData.setStatus(dataJson.getInt("status"));
        couponsData.setSubTitle(dataJson.getString("subTitle"));
        couponsData.setTitle(dataJson.getString("title"));
        couponsData.setValidEndTime(dataJson.getString("validEndTime"));
        couponsData.setValidStartTime(dataJson.getString("validStartTime"));

        LogUtil.e(TAG, couponsData.getBuyTime());
    }
    public CouponsData getCouponsData(){
        return couponsData;
    }

    public class CouponsData{
        private String buyTime;
        private String certificateNo;
        private String memberId;
        private String remark;
        private int status;
        private String subTitle;
        private String title;
        private String validEndTime;
        private String validStartTime;

        public void setBuyTime(String buyTime) {
            this.buyTime = buyTime;
        }

        public void setCertificateNo(String certificateNo) {
            this.certificateNo = certificateNo;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setValidEndTime(String validEndTime) {
            this.validEndTime = validEndTime;
        }

        public void setValidStartTime(String validStartTime) {
            this.validStartTime = validStartTime;
        }

        public String getBuyTime() {
            return buyTime;
        }

        public String getCertificateNo() {
            return certificateNo;
        }

        public String getMemberId() {
            return memberId;
        }

        public String getRemark() {
            return remark;
        }

        public int getStatus() {
            return status;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public String getTitle() {
            return title;
        }

        public String getValidEndTime() {
            return validEndTime;
        }

        public String getValidStartTime() {
            return validStartTime;
        }
    }
}
