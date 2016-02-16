package com.feifan.bp.marketinganalysis;

import com.android.volley.Response.Listener;
import com.feifan.bp.Constants;
import com.feifan.bp.PlatformState;
import com.feifan.bp.UserProfile;
import com.feifan.bp.network.GetRequest;
import com.feifan.bp.network.JsonRequest;
import com.feifan.bp.network.UrlFactory;

/**
 * 闪购添加商品
 * Created by congjing on 16/1/6.
 */
public class AlysisCtrl {
    /**
     * 红包分类
     * @param sdate
     * @param edate
     * @param listener
     * ?action=collect
     * "10007505"
     *  UserProfile.getInstance().getAuthRangeId()
     */
    public static void getRedTypeList(String sdate, String edate, Listener listener) {
        JsonRequest<AlysisRedSubTotalListModel> request = new GetRequest.Builder<AlysisRedSubTotalListModel>(UrlFactory.getAnalysisRedList())
                .param("storeId",UserProfile.getInstance().getAuthRangeId())
                .param("sdate", sdate)
                .param("edate", edate)
                .param("type","redbag")
                .build()
                .targetClass(AlysisRedSubTotalListModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }


    /**
     * 红包-红包核销明细
     * @param couponId
     * @param sdate
     * @param edate
     * @param beginKey
     * @param listener
     * UserProfile.getInstance().getAuthRangeId()
     */
     public static void getRedTypeListDetail(String couponId,String sdate, String edate, String beginKey,Listener listener) {
        JsonRequest<DetailModel> request = new GetRequest.Builder<DetailModel>(UrlFactory.getAnalysisRedListDetail())
                .param("storeId",UserProfile.getInstance().getAuthRangeId())
                .param("sdate", sdate)
                .param("edate",edate)
                .param("limit",Constants.LIST_LIMIT)
                .param("beginKey",beginKey)
                .param("couponId",couponId)
                .build()
                .targetClass(DetailModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }
}
