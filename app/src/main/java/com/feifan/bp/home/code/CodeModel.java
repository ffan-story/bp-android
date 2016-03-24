package com.feifan.bp.home.code;

import com.feifan.bp.base.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        JSONArray jsonArrayObject = new JSONArray(json);
        JSONObject jsonObject = jsonArrayObject.getJSONObject(0);
        couponsData = new CouponsData();
        couponsData.setBuyTime(jsonObject.optString("buyTime"));
        couponsData.setCertificateNo(jsonObject.optString("certificateNo"));
        couponsData.setMemberId(jsonObject.optString("memberId"));
        couponsData.setRemark(jsonObject.optString("remark"));
        couponsData.setStatus(jsonObject.optInt("status"));
        couponsData.setSubTitle(jsonObject.optString("subTitle"));
        couponsData.setTitle(jsonObject.optString("title"));
        couponsData.setValidEndTime(jsonObject.optString("validEndTime"));
        couponsData.setValidStartTime(jsonObject.optString("validStartTime"));

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
