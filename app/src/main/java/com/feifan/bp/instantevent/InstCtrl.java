package com.feifan.bp.instantevent;

import com.android.volley.Response.Listener;
import com.feifan.bp.Constants;
import com.feifan.bp.PlatformState;
import com.feifan.bp.UserProfile;
import com.feifan.bp.network.DefaultErrorListener;
import com.feifan.bp.network.GetRequest;
import com.feifan.bp.network.JsonRequest;
import com.feifan.bp.network.PostRequest;
import com.feifan.bp.network.UrlFactory;

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
        JsonRequest<InstEventGoodsListModel> request = new GetRequest.Builder<InstEventGoodsListModel>(UrlFactory.getInstEventGoodsList())
                .param("promotionCode", promotionCode)
                .param("merchantId", UserProfile.getInstance().getMerchantId())
                .param("storeId", UserProfile.getInstance().getAuthRangeId())
                .param("pageIndex", String.valueOf(pageIndex))
                .param("limit",String.valueOf(Constants.LIST_MAX_LENGTH))
                .build()
                .targetClass(InstEventGoodsListModel.class)
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
        JsonRequest<InstEvenSekSettModel> request = new GetRequest.Builder<InstEvenSekSettModel>(UrlFactory.getInstEventGoodsSettingDetail(goodsCode))
                .param("promotionCode", promotionCode)
                .param("goodsCode", goodsCode)
                .param("goods_action",goodsAction)
                .build()
                .targetClass(InstEvenSekSettModel.class)
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
        JsonRequest<InstEvenSekSettModel> request = new PostRequest<InstEvenSekSettModel>(UrlFactory.getInstEventGoodsCommAndSave(), new DefaultErrorListener())
                .param("goods_action", goodsAction)
                .param("promotionCode", promotionCode)
                .param("merchantId", UserProfile.getInstance().getMerchantId())
                .param("storeId", UserProfile.getInstance().getAuthRangeId())
                .param("commitFlag", commitFlag)
                .param("goodsCode", goodsCode)
                .param("goodsSn", goodsSn)
                .param("goodsSkuList", goodsSkuList)
                .param("merchantCutAmount", merchantCutAmount)
                .targetClass(InstEvenSekSettModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }
}
