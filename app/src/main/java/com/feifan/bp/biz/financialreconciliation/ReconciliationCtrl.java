package com.feifan.bp.biz.financialreconciliation;

import com.android.volley.Response;
import com.feifan.bp.PlatformState;
import com.feifan.bp.UserProfile;
import com.feifan.bp.base.network.GetRequest;
import com.feifan.bp.base.network.JsonRequest;
import com.feifan.bp.base.network.UrlFactory;
import com.feifan.bp.biz.financialreconciliation.model.AdjustOrderModel;
import com.feifan.bp.biz.financialreconciliation.model.FinancialSummaryModel;
import com.feifan.bp.biz.financialreconciliation.model.SettleDetailModel;
import com.feifan.bp.biz.financialreconciliation.model.SettleOrdersModel;

/**
 * 财务对账
 * Created by konta on 2016/3/23.
 */
public class ReconciliationCtrl {

    /**
     * 获取对账首页列表
     * @param page
     * @param startDate
     * @param endDate
     * @param listener
     * @param errorListener
     */
    public static void getSettleDetail(String page, String startDate, String endDate,
                                       Response.Listener listener, Response.ErrorListener errorListener){
        JsonRequest<FinancialSummaryModel> request = new GetRequest.Builder<FinancialSummaryModel>(UrlFactory.getReconciliationSummary())
                .errorListener(errorListener)
                .param("page", page)
                .param("startDate",startDate)
                .param("endDate",endDate)
                .param("storeId", UserProfile.getInstance().getAuthRangeId())
                .build()
                .targetClass(FinancialSummaryModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    /**
     * 获取对账单详情页列表  对账单
     * @param reNo
     * @param tab   1：对账单  2：调账
     * @param listener
     * @param errorListener
     */
    public static void getReconciliationSettleOrders(String reNo, String tab,
                                                     Response.Listener listener, Response.ErrorListener errorListener){
        JsonRequest<SettleOrdersModel> request = new GetRequest.Builder<SettleOrdersModel>(UrlFactory.getReConciliationOrders())
                .errorListener(errorListener)
                .param("reNo", reNo)
                .param("tab",tab)
                .param("storeId", UserProfile.getInstance().getAuthRangeId())
                .build()
                .targetClass(SettleOrdersModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    /**
     * 获取对账单详情页列表 调账信息
     * @param reNo
     * @param tab   1：对账单  2：调账
     * @param listener
     * @param errorListener
     */
    public static void getReconciliationAdjustOrders(String reNo,String tab,
                                               Response.Listener listener,Response.ErrorListener errorListener){
        JsonRequest<AdjustOrderModel> request = new GetRequest.Builder<AdjustOrderModel>(UrlFactory.getReConciliationOrders())
                .errorListener(errorListener)
                .param("reNo", reNo)
                .param("tab",tab)
                .param("storeId", UserProfile.getInstance().getAuthRangeId())
                .build()
                .targetClass(AdjustOrderModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    /**
     * 获取订单详情
     * @param page
     * @param settleNo
     * @param listener
     * @param errorListener
     */
    public static void getSettleDetail(String page, String settleNo,
                                       Response.Listener listener, Response.ErrorListener errorListener){
        JsonRequest<SettleDetailModel> request = new GetRequest.Builder<SettleDetailModel>(UrlFactory.getSettleDetail())
                .errorListener(errorListener)
                .param("page",page)
                .param("settleNo",settleNo)
                .param("storeId", UserProfile.getInstance().getAuthRangeId())
                .build()
                .targetClass(SettleDetailModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

}