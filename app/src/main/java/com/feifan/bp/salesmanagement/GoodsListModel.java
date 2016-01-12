package com.feifan.bp.salesmanagement;

import android.os.Parcel;
import android.os.Parcelable;

import com.feifan.bp.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frank on 16/1/8.
 */
public class GoodsListModel extends BaseModel {

    public List<GoodsDetailModel> goodsList;
    private GoodsDetailModel mGoodsModel;

    public GoodsListModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        JSONObject data = new JSONObject(json);

        goodsList = new ArrayList<>();
        JSONArray array = data.getJSONArray("goodsInfoList");
        for (int i = 0; i < array.length(); i++){
            try {
                mGoodsModel = new GoodsDetailModel(Parcel.obtain());
                mGoodsModel.setApproveStatus(array.getJSONObject(i).optString("approveStatus"));
                mGoodsModel.setGoodsName(array.getJSONObject(i).optString("goodsName"));
                mGoodsModel.setGoodsCode(array.getJSONObject(i).optString("goodsCode"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            goodsList.add(mGoodsModel);
        }
    }

    public static class GoodsDetailModel implements Parcelable{

        private String approveStatus;
        private String goodsName;
        private String goodsCode;

        protected GoodsDetailModel(Parcel in) {
            approveStatus = in.readString();
            goodsName = in.readString();
            goodsCode = in.readString();
        }

        public static final Creator<GoodsDetailModel> CREATOR = new Creator<GoodsDetailModel>() {
            @Override
            public GoodsDetailModel createFromParcel(Parcel in) {
                return new GoodsDetailModel(in);
            }

            @Override
            public GoodsDetailModel[] newArray(int size) {
                return new GoodsDetailModel[size];
            }
        };

        public String getApproveStatus() {
            return approveStatus;
        }

        public void setApproveStatus(String approveStatus) {
            this.approveStatus = approveStatus;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getGoodsCode() {
            return goodsCode;
        }

        public void setGoodsCode(String goodsCode) {
            this.goodsCode = goodsCode;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(approveStatus);
            dest.writeString(goodsName);
            dest.writeString(goodsCode);
        }
    }
}
