package com.feifan.bp.home.writeoff;

import com.feifan.bp.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by konta on 15-11-2.
 */
public class GoodsModel extends BaseModel {
    private final String TAG = "GoodsModel";
    public BaseInfo baseInfo;
    List<ProductInfo> productInfos;
    public GoodsModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        baseInfo = new BaseInfo();
        JSONObject jsonObject = new JSONObject(json);

        baseInfo.orderNo = jsonObject.optString("orderNo");
        baseInfo.storeName = jsonObject.getJSONObject("orderInfo").optString("storeName");
        baseInfo.singnStatus = jsonObject.optInt("signStatus");
        baseInfo.userIsWhite = jsonObject.optBoolean("userIsWhite");
        baseInfo.noticeMsg = jsonObject.optString("noticeMsg");

        JSONArray productArray = jsonObject.getJSONObject("orderInfo").getJSONArray("productList");
        productInfos = new ArrayList<>();
        for (int i = 0; i < productArray.length(); i++){
            ProductInfo productInfo = new ProductInfo();
            productInfo.title = productArray.getJSONObject(i).optString("title");
            productInfo.productCount = productArray.getJSONObject(i).optInt("productCount");
            productInfo.productPrice = productArray.getJSONObject(i).optString("productPrice");
            productInfos.add(productInfo);
        }
        baseInfo.productList = productInfos;
        baseInfo.orderAmt = jsonObject.getJSONObject("orderInfo").optString("orderAmt");
        baseInfo.usePointDiscount = jsonObject.getJSONObject("orderInfo").optString("usePointDiscount");
        baseInfo.realPayAmt = jsonObject.getJSONObject("orderInfo").optString("realPayAmt");
    }

    public class BaseInfo{
        public String orderNo;               //订单号
        public String storeName;             //门店名
        public int singnStatus;              //核销状态
        public List<ProductInfo> productList;//商品信息集合
        public String orderAmt;              //总价
        public String realPayAmt;            //实付款
        public String usePointDiscount;      //积分
        public boolean userIsWhite;          //白名单标识
        public String noticeMsg;             //白名单提示信息
    }

    /**
     * 商品信息
     */
    public class ProductInfo{
        public int productCount;//数量
        public String productPrice;//价格
        public String title;//商品名
    }

}
