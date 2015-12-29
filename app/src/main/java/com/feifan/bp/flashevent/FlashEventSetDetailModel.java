package com.feifan.bp.flashevent;

import com.feifan.bp.network.BaseModel;
import com.feifan.bp.settings.helpcenter.HelpCenterModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bp.feifan.com.codescanner.decoding.Intents;

/**
 *
 * 设置详情 model
 * Created by congjing
 */
//public class FlashEventSetDetailModel extends BaseModel {
public class FlashEventSetDetailModel {

    public FlashEventSetDetailData mEventSetDetailData;
    public int totalCount;
    public String mStrStatus = "审核通过";//飞凡优惠金额
    public ArrayList<FlashEventSetDetailData> arryFlashEventData = new ArrayList<>();

    public FlashEventSetDetailModel(long discount) {
            mEventSetDetailData = new FlashEventSetDetailData();
        for (int i = 0; i < 3; i++) {
            mEventSetDetailData.mLongVendorDiscount =discount;
            mEventSetDetailData.mLongGoodsAmount = 800;//商品原价
            mEventSetDetailData.mIntGoodsPartakeNumber = 0;
            mEventSetDetailData.mStrGoodsName="绿色植物";
            mEventSetDetailData.mIntGoodsNumber=300;//库存
            mEventSetDetailData.mLongFeifanDiscount = 10;//飞凡优惠金额
            mEventSetDetailData.setmLongGoodsDiscount(discount);//优惠后金额
            arryFlashEventData.add(mEventSetDetailData);
        }
    }


//    public FlashEventSetDetailModel(JSONObject json) {
//        super(json);
//    }
//
//
//    @Override
//    protected void parseData(String json) throws JSONException {
//        arryFlashEventData = new ArrayList<FlashEventSetDetailData>();
//        JSONObject jsonObject = new JSONObject(json);
//        totalCount = (jsonObject.optInt("totalCount"));
//        JSONArray itemsArray = jsonObject.getJSONArray("items");
//        for (int i = 0; i < itemsArray.length(); i++) {
//           mEventSetDetailData = new FlashEventSetDetailData();
//            try {
//                mEventSetDetailData.mStrGoodsName = (itemsArray.getJSONObject(i).optString("title"));
//                mEventSetDetailData.mStrGoodsNumber = (itemsArray.getJSONObject(i).optString("id"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            arryFlashEventData.add(mEventSetDetailData);
//        }
//    }



    public class FlashEventSetDetailData {
        public String mStrGoodsName;
        public int mIntGoodsNumber;

        /**
         * 商品原价
         */
        public long mLongGoodsAmount;
        /**
         * 飞凡优惠价格
         */
        public long mLongFeifanDiscount;
        /**
         * 商户优惠价格
         */
        public long mLongVendorDiscount;
        /**优惠后的价格
         *
         */
        private long mLongGoodsDiscount;

        /**
         * 参与活动数量
         */
        public int mIntGoodsPartakeNumber;


        public long getmLongGoodsDiscount() {
            return mLongGoodsDiscount;
        }

        public void setmLongGoodsDiscount(long discount) {
            this.mLongGoodsDiscount = mLongGoodsAmount-mLongFeifanDiscount-discount;
        }
    }
}
