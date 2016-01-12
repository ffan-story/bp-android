package com.feifan.bp.transactionflow;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.feifan.bp.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FlashSummaryModel extends BaseModel {

    private FlashSummaryDetailModel flashSummaryDetailModel;

    public ArrayList<FlashSummaryDetailModel> flashSummaryList;

    public FlashSummaryModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        Log.e(TAG, "summary----" + json);
        JSONObject data = new JSONObject(json);
        flashSummaryList = new ArrayList<>();
        JSONArray array = data.getJSONArray("flash");
        for (int i = 0; i < array.length(); i++) {
            try {
                flashSummaryDetailModel = new FlashSummaryDetailModel(Parcel.obtain());
                flashSummaryDetailModel.setCountName(array.getJSONObject(i).getString("countName"));
                flashSummaryDetailModel.setCountValue(array.getJSONObject(i).getString("countValue"));
                flashSummaryDetailModel.setAmountName(array.getJSONObject(i).getString("amountName"));
                flashSummaryDetailModel.setAmountValue(array.getJSONObject(i).getString("amountValue"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            flashSummaryList.add(flashSummaryDetailModel);
        }
    }


    public static class FlashSummaryDetailModel implements Parcelable {

        private String countName;
        private String countValue;
        private String amountName;
        private String amountValue;

        protected FlashSummaryDetailModel(Parcel in) {
            countName = in.readString();
            countValue = in.readString();
            amountName = in.readString();
            amountValue = in.readString();
        }

        public String getCountName() {
            return countName;
        }

        public void setCountName(String countName) {
            this.countName = countName;
        }

        public String getCountValue() {
            return countValue;
        }

        public void setCountValue(String countValue) {
            this.countValue = countValue;
        }

        public String getAmountName() {
            return amountName;
        }

        public void setAmountName(String amountName) {
            this.amountName = amountName;
        }

        public String getAmountValue() {
            return amountValue;
        }

        public void setAmountValue(String amountValue) {
            this.amountValue = amountValue;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(countName);
            dest.writeString(countValue);
            dest.writeString(amountName);
            dest.writeString(amountValue);
        }

        public static final Creator<FlashSummaryDetailModel> CREATOR = new Creator<FlashSummaryDetailModel>() {
            @Override
            public FlashSummaryDetailModel createFromParcel(Parcel in) {
                return new FlashSummaryDetailModel(in);
            }

            @Override
            public FlashSummaryDetailModel[] newArray(int size) {
                return new FlashSummaryDetailModel[size];
            }
        };
    }
}
