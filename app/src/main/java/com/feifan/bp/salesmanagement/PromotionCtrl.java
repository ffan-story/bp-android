package com.feifan.bp.salesmanagement;

import com.android.volley.Response;
import com.feifan.bp.PlatformState;
import com.feifan.bp.base.network.BaseModel;
import com.feifan.bp.base.network.GetRequest;
import com.feifan.bp.base.network.JsonRequest;
import com.feifan.bp.base.network.PostRequest;
import com.feifan.bp.base.network.UrlFactory;
import com.feifan.bp.base.network.response.ToastErrorListener;

/**
 * Created by Frank on 16/1/5.
 */
public class PromotionCtrl {

    /**
     * 营销活动管理活动列表
     *
     * @param storeId       门店ID
     * @param merchantId    商户ID
     * @param plazaId       广场ID
     * @param ifEnroll      是否报名 0:可报名 1:已报名
     * @param pageIndex     页码
     * @param limit         页面条目数
     * @param listener
     * @param errorListener
     */
    public static void getPromotionList(String storeId,
                                        String merchantId,
                                        String plazaId,
                                        int ifEnroll,
                                        int pageIndex,
                                        int limit,
                                        Response.Listener<PromotionListModel> listener,
                                        Response.ErrorListener errorListener) {
        JsonRequest<PromotionListModel> request = new GetRequest.Builder<PromotionListModel>(UrlFactory.getPromotionListUrl())
                .param("storeId", storeId)
                .param("merchantId", merchantId)
                .param("plazaId", plazaId)
                .param("ifEnroll", String.valueOf(ifEnroll))
                .param("pageIndex", String.valueOf(pageIndex))
                .param("limit", String.valueOf(limit))
                .errorListener(errorListener)
                .build()
                .targetClass(PromotionListModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    /**
     * 营销活动获取商品状态
     *
     * @param storeId       门店ID
     * @param merchantId    商户ID
     * @param promotionCode 活动ID
     * @param listener
     */
    public static void getGoodsStatus(String storeId, String merchantId, String promotionCode,
                                      Response.Listener<GoodsStatusModel> listener) {
        JsonRequest<GoodsStatusModel> request = new GetRequest.Builder<GoodsStatusModel>(UrlFactory.getGoodsStatus())
                .param("storeId", storeId)
                .param("merchantId", merchantId)
                .param("promotionCode", promotionCode)
                .param("pageIndex", "1")
                .param("limit", "10")
                .errorListener(new ToastErrorListener())
                .build()
                .targetClass(GoodsStatusModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    /**
     * 营销活动获取商品列表
     *
     * @param storeId       门店ID
     * @param merchantId    商户ID
     * @param promotionCode 活动ID
     * @param listener
     */
    public static void getGoodsList(String storeId, String merchantId, String promotionCode, String status,
                                    Response.Listener<GoodsListModel> listener) {
        JsonRequest<GoodsListModel> request = new GetRequest.Builder<GoodsListModel>(UrlFactory.getGoodsList())
                .param("storeId", storeId)
                .param("merchantId", merchantId)
                .param("promotionCode", promotionCode)
                .param("approveStatus", status)
                .errorListener(new ToastErrorListener())
                .build()
                .targetClass(GoodsListModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    /**
     * 报名详情提交商品审核
     *
     * @param storeId       门店id
     * @param merchantId    商户id
     * @param applicant     当前登陆者id
     * @param promotionCode 活动id
     * @param goodsCode     商品id
     * @param listener
     */
    public static void goodsAudit(String storeId, String merchantId, String applicant, String promotionCode, String goodsCode,
                                  Response.Listener<BaseModel> listener) {
        JsonRequest<BaseModel> request = new PostRequest<>(UrlFactory.auditGoodsUrl(), new ToastErrorListener())
                .param("storeId", storeId)
                .param("merchantId", merchantId)
                .param("applicant", applicant)
                .param("promotionCode", promotionCode)
                .param("goodsCode", goodsCode)
                .targetClass(BaseModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    /**
     * 报名详情提交商品删除
     *
     * @param storeId       门店id
     * @param merchantId    商户id
     * @param applicant     当前登陆者id
     * @param promotionCode 活动id
     * @param goodsCode     商品id
     * @param listener
     */
    public static void goodsDelete(String storeId, String merchantId, String applicant, String promotionCode, String goodsCode,
                                   Response.Listener<BaseModel> listener) {
        JsonRequest<BaseModel> request = new PostRequest<>(UrlFactory.deleteGoodsUrl(), new ToastErrorListener())
                .param("storeId", storeId)
                .param("merchantId", merchantId)
                .param("applicant", applicant)
                .param("promotionCode", promotionCode)
                .param("goodsCode", goodsCode)
                .targetClass(BaseModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }
}
