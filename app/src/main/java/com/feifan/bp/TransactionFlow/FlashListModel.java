package com.feifan.bp.TransactionFlow;

import android.os.Parcel;
import android.os.Parcelable;

import com.feifan.bp.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Frank on 15/11/9.
 */
public class    FlashListModel extends BaseModel {

    public int totalCount;

    private FlashDetailModel flashDetailModel;

    public ArrayList<FlashDetailModel> flashDetailList;

    public FlashListModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        JSONObject data = new JSONObject(json);
        totalCount = data.optInt("totalCount");
        JSONArray array = data.getJSONArray("flash");
        flashDetailList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                flashDetailModel = new FlashDetailModel(Parcel.obtain());
                flashDetailModel.setBillNo(array.getJSONObject(i).getString("billNo"));
                flashDetailModel.setOrderNo(array.getJSONObject(i).getString("orderNo"));
                flashDetailModel.setMobile(array.getJSONObject(i).getString("mobile"));
                flashDetailModel.setOrderTradeSuccessTime(array.getJSONObject(i).getString("orderTradeSuccessTime"));
                flashDetailModel.setOrderRefundAuditTime(array.getJSONObject(i).getString("orderRefundAuditTime"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            flashDetailList.add(flashDetailModel);
        }
    }

    public static class FlashDetailModel implements Parcelable {

        private String billNo;
        private String orderNo;
        private String mobile;
        private String orderTradeSuccessTime;
        private String orderRefundAuditTime;

        protected FlashDetailModel(Parcel in) {
            billNo = in.readString();
            orderNo = in.readString();
            mobile = in.readString();
            orderTradeSuccessTime = in.readString();
            orderRefundAuditTime = in.readString();
        }

        public static final Creator<FlashDetailModel> CREATOR = new Creator<FlashDetailModel>() {
            @Override
            public FlashDetailModel createFromParcel(Parcel in) {
                return new FlashDetailModel(in);
            }

            @Override
            public FlashDetailModel[] newArray(int size) {
                return new FlashDetailModel[size];
            }
        };

        public String getBillNo() {
            return billNo;
        }

        public void setBillNo(String billNo) {
            this.billNo = billNo;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getOrderRefundAuditTime() {
            return orderRefundAuditTime;
        }

        public void setOrderRefundAuditTime(String orderRefundAuditTime) {
            this.orderRefundAuditTime = orderRefundAuditTime;
        }

        public String getOrderTradeSuccessTime() {
            return orderTradeSuccessTime;
        }

        public void setOrderTradeSuccessTime(String orderTradeSuccessTime) {
            this.orderTradeSuccessTime = orderTradeSuccessTime;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(billNo);
            dest.writeString(orderNo);
            dest.writeString(mobile);
            dest.writeString(orderTradeSuccessTime);
            dest.writeString(orderRefundAuditTime);
        }
    }
}

