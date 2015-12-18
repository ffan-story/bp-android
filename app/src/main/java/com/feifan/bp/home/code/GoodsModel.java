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
public class GoodsModel extends BaseModel {
    private final String TAG = "GoodsModel";
    private GoodsData goodsData;
    List<ProductInfo> productInfos;
    public GoodsModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        goodsData = new GoodsData();
        JSONObject jsonObject = new JSONObject(json);

        goodsData.setOrderNo(jsonObject.optString("orderNo"));
        goodsData.setStoreName(jsonObject.getJSONObject("orderInfo").optString("storeName"));
        goodsData.setSingnStatus(jsonObject.optInt("signStatus"));

        JSONArray productArray = jsonObject.getJSONObject("orderInfo").getJSONArray("productList");
        productInfos = new ArrayList<>();
        for (int i = 0; i < productArray.length(); i++){
            ProductInfo productInfo = new ProductInfo();
            productInfo.setTitle(productArray.getJSONObject(i).optString("title"));
            productInfo.setProductCount(productArray.getJSONObject(i).optInt("productCount"));
            productInfo.setProductPrice(productArray.getJSONObject(i).optString("productPrice"));
            productInfos.add(productInfo);
        }
        goodsData.setProductList(productInfos);
        goodsData.setOrderAmt(jsonObject.getJSONObject("orderInfo").optString("orderAmt"));
        goodsData.setUsePointDiscount(jsonObject.getJSONObject("orderInfo").optString("usePointDiscount"));
        goodsData.setRealPayAmt(jsonObject.getJSONObject("orderInfo").optString("realPayAmt"));
    }

    public GoodsData getGoodsData(){
        return goodsData;
    }
    public class GoodsData{
        private String orderNo;//订单号
        private String storeName;//门店名
        private int singnStatus;//核销状态
        private List<ProductInfo> productList;//商品信息集合
        private String orderAmt;//总价
        private String realPayAmt;//实付款
        private String usePointDiscount;//积分

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public int getSingnStatus() {
            return singnStatus;
        }

        public void setSingnStatus(int singnStatus) {
            this.singnStatus = singnStatus;
        }

        public List<ProductInfo> getProductList() {
            return productList;
        }

        public void setProductList(List<ProductInfo> productList) {
            this.productList = productList;
        }

        public String getOrderAmt() {
            return orderAmt;
        }

        public void setOrderAmt(String orderAmt) {
            this.orderAmt = orderAmt;
        }

        public String getRealPayAmt() {
            return realPayAmt;
        }

        public void setRealPayAmt(String realPayAmt) {
            this.realPayAmt = realPayAmt;
        }

        public String getUsePointDiscount() {
            return usePointDiscount;
        }

        public void setUsePointDiscount(String usePointDiscount) {
            this.usePointDiscount = usePointDiscount;
        }
    }

    /**
     * 商品信息
     */
    public class ProductInfo{
        private int productCount;//数量
        private String productPrice;//价格
        private String title;//商品名

        public int getProductCount() {
            return productCount;
        }

        public void setProductCount(int productCount) {
            this.productCount = productCount;
        }

        public String getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(String productPrice) {
            this.productPrice = productPrice;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

}
