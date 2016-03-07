package com.feifan.bp.salesmanagement;

import com.android.volley.Response.Listener;
import com.feifan.bp.Constants;
import com.feifan.bp.PlatformState;
import com.feifan.bp.UserProfile;
import com.feifan.bp.network.GetRequest;
import com.feifan.bp.network.JsonRequest;
import com.feifan.bp.network.PostRequest;
import com.feifan.bp.network.UrlFactory;
import com.feifan.bp.network.response.ToastErrorListener;

/**
 * 闪购添加商品
 * Created by congjing on 16/1/6.
 */
public class InstCtrl {
    /**
     * 添加活动商品列表
     * @param promotionCode
     * @param pageIndex
     * @param listener
     */
    public static void getInstEventGoodsList(String promotionCode,int pageIndex, Listener listener) {
        JsonRequest<com.feifan.bp.salesmanagement.InstEventGoodsListModel> request = new GetRequest.Builder<com.feifan.bp.salesmanagement.InstEventGoodsListModel>(UrlFactory.getInstEventGoodsList())
                .param("promotionCode", promotionCode)
                .param("merchantId", UserProfile.getInstance().getMerchantId())
                .param("storeId", UserProfile.getInstance().getAuthRangeId())
                .param("pageIndex", String.valueOf(pageIndex))
                .param("limit",String.valueOf(Constants.LIST_MAX_LENGTH))
                .errorListener(new ToastErrorListener())
                .build()
                .targetClass(com.feifan.bp.salesmanagement.InstEventGoodsListModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }


    /**
     * 添加活动商品-设置详情
     * @param promotionCode
     * @param goodsCode
     * @param goodsAction  回显商户参与活动商品数量传：edit
     */
    public static void getInstEventGoodsSetingDeta(String promotionCode,String goodsCode,String goodsAction, Listener listener) {
        JsonRequest<com.feifan.bp.salesmanagement.InstEvenSkuSettModel> request = new GetRequest.Builder<com.feifan.bp.salesmanagement.InstEvenSkuSettModel>(UrlFactory.getInstEventGoodsSettingDetail(goodsCode))
                .param("promotionCode", promotionCode)
                .param("goodsCode", goodsCode)
                .param("goods_action", goodsAction)
                .errorListener(new ToastErrorListener())
                .build()
                .targetClass(com.feifan.bp.salesmanagement.InstEvenSkuSettModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    /**
     * 添加活动商品-保存或者提交-设置详情
     * @param goodsAction
     * @param promotionCode
     * @param commitFlag
     * @param goodsCode
     * @param goodsSn
     * @param goodsSkuList
     * @param merchantCutAmount
     * @param listener
     */
    public static void postInstGoodsDetaSaveAndComm(String goodsAction,String promotionCode,
                                                   String commitFlag,String goodsCode,
                                                   String goodsSn,String goodsSkuList,
                                                   String merchantCutAmount,
                                                   Listener listener) {
        JsonRequest<com.feifan.bp.salesmanagement.InstEvenSkuSettModel> request = new PostRequest<com.feifan.bp.salesmanagement.InstEvenSkuSettModel>(UrlFactory.getInstEventGoodsCommAndSave(), new ToastErrorListener())
                .param("goods_action", goodsAction)
                .param("promotionCode", promotionCode)
                .param("merchantId", UserProfile.getInstance().getMerchantId())
                .param("storeId", UserProfile.getInstance().getAuthRangeId())
                .param("commitFlag", commitFlag)
                .param("goodsCode", goodsCode)
                .param("goodsSn", goodsSn)
                .param("goodsSkuList", goodsSkuList)
                .param("merchantCutAmount", merchantCutAmount)
                .targetClass(com.feifan.bp.salesmanagement.InstEvenSkuSettModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }
}
