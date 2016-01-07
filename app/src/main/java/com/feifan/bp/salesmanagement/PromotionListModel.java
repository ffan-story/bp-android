package com.feifan.bp.salesmanagement;

import android.os.Parcel;
import android.os.Parcelable;

import com.feifan.bp.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 报名活动模型
 * Created by Frank on 16/1/5.
 */
public class PromotionListModel extends BaseModel {

    public int totalSize;
    public ArrayList<PromotionDetailModel> promotionList;
    private PromotionDetailModel promotionDetailModel;

    public PromotionListModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        JSONObject data = new JSONObject(json);
        totalSize = data.getInt("totalSize");

        promotionList = new ArrayList<>();
        JSONArray array = data.getJSONArray("promotionsList");
        for (int i = 0; i < array.length(); i++) {
            try {
                promotionDetailModel = new PromotionDetailModel(Parcel.obtain());
                promotionDetailModel.setPromotionType(array.getJSONObject(i).getString("promotionType"));
                promotionDetailModel.setPromotionName(array.getJSONObject(i).getString("promotionName"));
                promotionDetailModel.setEnrollCount(array.getJSONObject(i).getString("enrollCount"));
                promotionDetailModel.setPromotionEnrollStatus(array.getJSONObject(i).getString("promotionEnrollStatus"));
                promotionDetailModel.setStartTime(array.getJSONObject(i).getString("startTime"));
                promotionDetailModel.setEndTime(array.getJSONObject(i).getString("endTime"));
                promotionDetailModel.setPromotionCode(array.getJSONObject(i).getString("promotionCode"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            promotionList.add(promotionDetailModel);
        }
    }

    public static class PromotionDetailModel implements Parcelable {

        public String promotionType;//活动类型
        public String promotionName;//活动名称
        public String enrollCount;//已报名商家数量
        public String promotionEnrollStatus;//报名状态
        public String startTime;//活动开始时间
        public String endTime;//活动结束时间
        public String promotionCode;//活动ID

        protected PromotionDetailModel(Parcel in) {
            promotionType = in.readString();
            promotionName = in.readString();
            enrollCount = in.readString();
            promotionEnrollStatus = in.readString();
            startTime = in.readString();
            endTime = in.readString();
            promotionCode = in.readString();
        }

        public static final Creator<PromotionDetailModel> CREATOR = new Creator<PromotionDetailModel>() {
            @Override
            public PromotionDetailModel createFromParcel(Parcel in) {
                return new PromotionDetailModel(in);
            }

            @Override
            public PromotionDetailModel[] newArray(int size) {
                return new PromotionDetailModel[size];
            }
        };

        public String getPromotionType() {
            return promotionType;
        }

        public void setPromotionType(String promotionType) {
            this.promotionType = promotionType;
        }

        public String getPromotionName() {
            return promotionName;
        }

        public void setPromotionName(String promotionName) {
            this.promotionName = promotionName;
        }

        public String getEnrollCount() {
            return enrollCount;
        }

        public void setEnrollCount(String enrollCount) {
            this.enrollCount = enrollCount;
        }

        public String getPromotionEnrollStatus() {
            return promotionEnrollStatus;
        }

        public void setPromotionEnrollStatus(String promotionEnrollStatus) {
            this.promotionEnrollStatus = promotionEnrollStatus;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getPromotionCode() {
            return promotionCode;
        }

        public void setPromotionCode(String promotionCode) {
            this.promotionCode = promotionCode;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(promotionType);
            dest.writeString(promotionName);
            dest.writeString(enrollCount);
            dest.writeString(promotionEnrollStatus);
            dest.writeString(startTime);
            dest.writeString(endTime);
            dest.writeString(promotionCode);
        }
    }
}
