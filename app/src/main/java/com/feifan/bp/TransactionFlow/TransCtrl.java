package com.feifan.bp.TransactionFlow;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.feifan.bp.PlatformState;
import com.feifan.bp.network.GetRequest;
import com.feifan.bp.network.JsonRequest;
import com.feifan.bp.network.UrlFactory;

/**
 * Created by Frank on 15/11/13.
 */
public class TransCtrl {

    /**
     * 闪购对账流水单明细统计
     *
     * @param startDate     开始时间
     * @param endDate       结束时间
     * @param storeId       门店ID
     * @param listener
     * @param errorListener
     */
    public static void getFlashSummary(String startDate,
                                       String endDate,
                                       String storeId,
                                       Listener<FlashSummaryModel> listener,
                                       Response.ErrorListener errorListener) {
        JsonRequest<FlashSummaryModel> request = new GetRequest.Builder<FlashSummaryModel>(UrlFactory.getFlashBuyUrl())
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("storeId", storeId)
                .param("action", "flashsummary")
                //.errorListener(errorListener)
                .build()
                .targetClass(FlashSummaryModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    /**
     * 闪购对账流水单明细列表
     *
     * @param startDate     开始时间
     * @param endDate       结束时间
     * @param storeId       门店ID
     * @param pageIndex     页码
     * @param limit         页数
     * @param listener
     * @param errorListener
     */
    public static void getFlashList(String startDate,
                                    String endDate,
                                    String storeId,
                                    String pageIndex,
                                    String limit,
                                    Listener<FlashListModel> listener,
                                    Response.ErrorListener errorListener) {
        JsonRequest<FlashListModel> request = new GetRequest.Builder<FlashListModel>(UrlFactory.getFlashBuyUrl())
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("storeId", storeId)
                .param("pageIndex", pageIndex)
                .param("limit", limit)
                //.errorListener(errorListener)
                .build()
                .targetClass(FlashListModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    /**
     * 优惠券对账流水单明细统计
     *
     * @param type          类型
     * @param month         月份
     * @param storeId       门店ID
     * @param listener
     * @param errorListener
     */
    public static void getCouponsSummary(String type,
                                         String month,
                                         String storeId,
                                         Listener<CpSummaryModel> listener,
                                         Response.ErrorListener errorListener) {
        JsonRequest<CpSummaryModel> request = new GetRequest.Builder<CpSummaryModel>(UrlFactory.getCouponsUrl())
                .param("type", type)
                .param("month", month)
                .param("storeId", storeId)
                //.errorListener(errorListener)
                .build()
                .targetClass(CpSummaryModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }
}
