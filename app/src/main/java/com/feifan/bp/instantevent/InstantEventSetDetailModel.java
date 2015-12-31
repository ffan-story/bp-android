package com.feifan.bp.instantevent;

import java.util.ArrayList;

/**
 *
 * 设置详情 model
 * Created by congjing
 */
//public class InstantEventSetDetailModel extends BaseModel {
public class InstantEventSetDetailModel {

    public InstantEventSetDetailData mEventSetDetailData;
    public int totalCount;
    public String mStrStatus = "审核通过";//飞凡优惠金额
    public ArrayList<InstantEventSetDetailData> arryInstantEventData = new ArrayList<>();

    public String[] s = {"绿色植物","风信子","绿萝"};

    double mDoubleFeifanDiscount = 10;//飞凡优惠金额
    double mDoubleVendorDiscount = 10;//飞凡优惠金额

    public InstantEventSetDetailModel() {
        for (int i = 0; i < 3; i++) {
            mEventSetDetailData = new InstantEventSetDetailData();
            mEventSetDetailData.mDoubleGoodsAmount = 800;//商品原价
            mEventSetDetailData.mDoubleGoodsPartakeNumber = 0;
            mEventSetDetailData.mStrGoodsName=s[i];
            mEventSetDetailData.mIntGoodsNumber=300;//库存
            mEventSetDetailData.setmDoubleGoodsDiscount();//优惠后金额
            arryInstantEventData.add(mEventSetDetailData);
        }
    }


//    public InstantEventSetDetailModel(JSONObject json) {
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



    public class InstantEventSetDetailData {
        public String mStrGoodsName;
        public int mIntGoodsNumber;
        /**
         * 商品原价
         */
        public double mDoubleGoodsAmount;

        /**
         * 优惠后的价格
         */
        private double mDoubleGoodsDiscount;

        /**
         * 参与活动数量
         */
        public double mDoubleGoodsPartakeNumber;


        public double getmDoubleGoodsDiscount() {
            return mDoubleGoodsDiscount;
        }

        public void setmDoubleGoodsDiscount() {
            if ( mDoubleGoodsAmount-mDoubleFeifanDiscount-mDoubleVendorDiscount <0){
                this.mDoubleGoodsDiscount = 0;
            }else{
                this.mDoubleGoodsDiscount = mDoubleGoodsAmount-mDoubleFeifanDiscount-mDoubleVendorDiscount;
            }
        }
    }
}
