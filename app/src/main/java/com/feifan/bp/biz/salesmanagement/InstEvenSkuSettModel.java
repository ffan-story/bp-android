package com.feifan.bp.biz.salesmanagement;

import com.feifan.bp.base.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 * 设置详情 model
 * Created by congjing
 */
public class InstEvenSkuSettModel extends BaseModel {
    public InstantEventSetDetailData mEventSetDetailData;
//    public String mStrStatus = "审核通过";//飞凡优惠金额
    public ArrayList<InstantEventSetDetailData> arryInstantEventData ;

    public String mStrCode;
    public double mDoubleFeifanDiscount ;//飞凡优惠金额
    public double mDoubleVendorDiscount ;//飞凡优惠金额
    public String mStrGoodsSn;
    public String mStrApproveDes;
    public String mStrApproveStatus;

    public InstEvenSkuSettModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray itemsArray = jsonObject.getJSONArray("goodsStockpriceSkus");
        mDoubleFeifanDiscount=Double.valueOf(jsonObject.optString("operateCutAmount"));//飞凡减免
        if (!jsonObject.isNull("merchantCutAmount")){
            mDoubleVendorDiscount=Double.valueOf(jsonObject.optString("merchantCutAmount"));//商家减免
        }
        if (!jsonObject.isNull("approveStatus")){
            mStrApproveDes = jsonObject.optString("approveDes");//－－审批描述
            mStrApproveStatus = jsonObject.optString("approveStatus");//审批状态
        }

        mStrGoodsSn = jsonObject.optString("goodsSn");
        mStrCode = jsonObject.optString("code");//--商品id
        arryInstantEventData = new ArrayList<>();
        for (int i = 0; i < itemsArray.length(); i++) {
            mEventSetDetailData = new InstantEventSetDetailData();
            try {
                mEventSetDetailData.mId = itemsArray.getJSONObject(i).optString("skuId");
                mEventSetDetailData.mStrGoodsName = itemsArray.getJSONObject(i).isNull("alias")?"":itemsArray.getJSONObject(i).optString("alias");
                mEventSetDetailData.mIntGoodsTotal = itemsArray.getJSONObject(i).optInt("stockNum");//剩余总库存
                mEventSetDetailData.mIntGoodsPartakeNumber = itemsArray.getJSONObject(i).optInt("subTotal");//参与活动商品 数量
                mEventSetDetailData.mDoubleGoodsAmount = Double.valueOf(itemsArray.getJSONObject(i).optString("price"));
                mEventSetDetailData.mSkuSn = itemsArray.getJSONObject(i).optString("skuSn");
                mEventSetDetailData.mStrEventId  = itemsArray.getJSONObject(i).optString("skuCode");
                mEventSetDetailData.setmDoubleGoodsDiscount();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            arryInstantEventData.add(mEventSetDetailData);
        }
    }

    public class InstantEventSetDetailData {
        public String mId;
        public String mStrEventId;
        public String mStrGoodsName;
        public String mSkuSn;

        /**
         * 剩余总库存
         */
        public int mIntGoodsTotal;
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
        public int mIntGoodsPartakeNumber;


        public double getmDoubleGoodsDiscount() {
            return mDoubleGoodsDiscount;
        }

        public void setmDoubleGoodsDiscount() {
            if ( mDoubleGoodsAmount-mDoubleFeifanDiscount-mDoubleVendorDiscount <0){
                this.mDoubleGoodsDiscount = -1;
            }else{
                this.mDoubleGoodsDiscount = mDoubleGoodsAmount-mDoubleFeifanDiscount-mDoubleVendorDiscount;
            }
        }
    }
}
