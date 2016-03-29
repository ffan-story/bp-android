package com.feifan.bp.biz.transactionflowing;

import com.android.volley.Response;
import com.feifan.bp.PlatformState;
import com.feifan.bp.UserProfile;
import com.feifan.bp.base.network.GetRequest;
import com.feifan.bp.base.network.JsonRequest;
import com.feifan.bp.base.network.UrlFactory;
import com.feifan.bp.biz.transactionflowing.model.CouponSummaryModel;
import com.feifan.bp.biz.transactionflowing.model.InstantDetailModel;
import com.feifan.bp.biz.transactionflowing.model.InstantOrderDetailModel;
import com.feifan.bp.biz.transactionflowing.model.InstantSummaryModel;

/**
 * 对账管理界面控制类
 * Created by konta on 2016/1/12.
 */
public class TransFlowCtrl {
    /**
     * 获取闪购交易流水明细统计
     * @param listener
     * @param errorListener
     * @param startDate 起始时间
     * @param endDate   截止时间
     */
    public static void getInstantSummary(String startDate,
                                         String endDate,
                                         Response.Listener listener,
                                         Response.ErrorListener errorListener){
        JsonRequest<InstantSummaryModel> request = new GetRequest.Builder<InstantSummaryModel>(UrlFactory.getInstantSummary())
                .errorListener(errorListener)
                .param("startDate",startDate)
                .param("endDate",endDate)
                .param("storeId", UserProfile.getInstance().getAuthRangeId())
                .build()
                .targetClass(InstantSummaryModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    /**
     * 获取闪购商品汇总列表
     * @param startDate 开始日期
     * @param endDate   截止日期
     * @param pageIndex 页码
     * @param limit     每页行数
     * @param listener
     * @param errorListener
     */
    public static void getInstantDetailList(String startDate,
                                             String endDate,
                                            int pageIndex,
                                            int limit,
                                             Response.Listener listener,
                                             Response.ErrorListener errorListener){
        JsonRequest<InstantDetailModel> request = new GetRequest.Builder<InstantDetailModel>(UrlFactory.getInstantDetailList())
                .errorListener(errorListener)
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("pageIndex", pageIndex + "")
                .param("limit",limit+"")
                .param("merchantId","")
                .param("storeId", UserProfile.getInstance().getAuthRangeId())
                .build()
                .targetClass(InstantDetailModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    /**
     * 获取订单明细列表
     * @param isOnlyRefund
     * @param startDate 开始时间
     * @param endDate   截止时间
     * @param pageIndex 页码
     * @param limit     每页行数
     * @param goodsId   商品
     * @param listener
     * @param errorListener
     */
    public static void getInstantOrderDetailList(String isOnlyRefund, String startDate,
                                                 String endDate,
                                                 int pageIndex,
                                                 int limit,
                                                 String goodsId,
                                                 Response.Listener listener,
                                                 Response.ErrorListener errorListener){
        JsonRequest<InstantOrderDetailModel> request = new GetRequest.Builder<InstantOrderDetailModel>(UrlFactory.getFlashBuyUrl())
                .errorListener(errorListener)
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("goodsId",goodsId)
                .param("pageIndex", pageIndex + "")
                .param("onlyrefund",isOnlyRefund)
                .param("limit", limit + "")
                .param("merchantId","")
                .param("storeId", UserProfile.getInstance().getAuthRangeId())
                .build()
                .targetClass(InstantOrderDetailModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }


    /**
     * 获取通用券明细统计
     * @param month     月份
     * @param listener
     * @param errorListener
     */
    public static void getCouponSummary(String month,
                                        int pageIndex,
                                        int limit,
                                         Response.Listener listener,
                                         Response.ErrorListener errorListener){
        JsonRequest<CouponSummaryModel> request = new GetRequest.Builder<CouponSummaryModel>(UrlFactory.getCouponsUrl())
                .errorListener(errorListener)
                .param("type", "1")
                .param("month",month)
                .param("pageIndex", pageIndex + "")
                .param("limit",limit+"")
                .param("storeId", UserProfile.getInstance().getAuthRangeId())
                .build()
                .targetClass(CouponSummaryModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }
}
