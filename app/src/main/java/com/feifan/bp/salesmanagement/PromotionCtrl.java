package com.feifan.bp.salesmanagement;

import com.android.volley.Response;
import com.feifan.bp.PlatformState;
import com.feifan.bp.network.GetRequest;
import com.feifan.bp.network.JsonRequest;
import com.feifan.bp.network.UrlFactory;

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
}
