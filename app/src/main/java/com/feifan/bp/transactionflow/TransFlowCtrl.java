package com.feifan.bp.transactionflow;

import com.android.volley.Response;
import com.feifan.bp.PlatformState;
import com.feifan.bp.UserProfile;
import com.feifan.bp.network.GetRequest;
import com.feifan.bp.network.JsonRequest;
import com.feifan.bp.network.UrlFactory;

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
                .param("storeId", "9053027")
                .build()
                .targetClass(InstantDetailModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    /**
     * 获取订单明细列表
     * @param startDate 开始时间
     * @param endDate   截止时间
     * @param pageIndex 页码
     * @param limit     每页行数
     * @param goodsId   商品
     * @param listener
     * @param errorListener
     */
    public static void getInstantOrderDetailList(String startDate,
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
                .param("limit",limit+"")
                .param("includeRefundTime","true")
                .param("includeRefundAuditTime","true")
                .param("merchantId","")
                .param("storeId", "9053027")
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
    public static void getCouponSummary(String type,
                                        String month,
                                        int pageIndex,
                                        int limit,
                                         Response.Listener listener,
                                         Response.ErrorListener errorListener){
        JsonRequest<CouponSummaryModel> request = new GetRequest.Builder<CouponSummaryModel>(UrlFactory.getCouponsUrl())
                .errorListener(errorListener)
                .param("type", type)
                .param("month",month)
                .param("pageIndex", pageIndex + "")
                .param("limit",limit+"")
                .param("storeId", "9053027")
                .build()
                .targetClass(CouponSummaryModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }
}
