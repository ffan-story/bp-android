package com.feifan.bp.marketinganalysis;

import com.android.volley.Response;
import com.feifan.bp.Constants;
import com.feifan.bp.PlatformState;
import com.feifan.bp.UserProfile;
import com.feifan.bp.base.network.GetRequest;
import com.feifan.bp.base.network.JsonRequest;
import com.feifan.bp.base.network.UrlFactory;

/**
 * 营销分析二期
 * Created by konta on 2016/2/16.
 */
public class MarketingCtrl  {
    /**
     * 获取首页及汇总页数据
     * @param sdate
     * @param edate
     * @param type  汇总类型 ：红包，摇一摇，优惠券
     * @param listener
     */
    public static void getSummary(String sdate,String edate,String type,Response.Listener listener,Response.ErrorListener errorListener){
        JsonRequest<MarketingSummaryModel> request = new GetRequest.Builder<MarketingSummaryModel>(UrlFactory.getAnalysisRedList())
                .errorListener(errorListener)
                .param("sdate", sdate)
                .param("edate",edate)
                .param("type",type)
                .param("storeId", UserProfile.getInstance().getAuthRangeId())
                .build()
                .targetClass(MarketingSummaryModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    /**
     * 获取明细页数据
     * @param couponId
     * @param sdate
     * @param edate
     * @param beginKey  游标
     * @param listener
     */
    public static void getDetailList(String type,String couponId,String sdate, String edate, String beginKey,Response.Listener listener,Response.ErrorListener errorListener){
        JsonRequest<MarketingDetailModel> request = new GetRequest.Builder<MarketingDetailModel>(UrlFactory.getAnalysisRedListDetail())
                .errorListener(errorListener)
                .param("type", type)
                .param("sdate", sdate)
                .param("edate", edate)
                .param("couponId", couponId)
                .param("beginKey",beginKey)
                .param("limit", Constants.LIST_LIMIT)
                .param("storeId", UserProfile.getInstance().getAuthRangeId())
                .build()
                .targetClass(MarketingDetailModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    /**
     * 获取通用券汇总&详情
     * @param sdate
     * @param edate
     * @param type
     * @param pageIndex
     * @param listener
     */
    public static void getCommonSummaryAndDetail(String sdate,String edate,String type,String pageIndex,Response.Listener listener,Response.ErrorListener errorListener){
        JsonRequest<CommonModel> request = new GetRequest.Builder<CommonModel>(UrlFactory.getAnalysisRedList())
                .errorListener(errorListener)
                .param("sdate", sdate)
                .param("edate", edate)
                .param("type",type)
                .param("moffset", pageIndex)
                .param("mlimit", Constants.LIST_LIMIT)
                .param("storeId", UserProfile.getInstance().getAuthRangeId())
                .build()
                .targetClass(CommonModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }
}
