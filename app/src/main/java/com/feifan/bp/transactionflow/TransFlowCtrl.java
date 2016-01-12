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
    public static void getInstantSummary(String startDate,String endDate,Response.Listener listener,Response.ErrorListener errorListener){
        JsonRequest<InstantSummaryModle> request = new GetRequest.Builder<InstantSummaryModle>(UrlFactory.getFlashBuyUrl())
                .errorListener(errorListener)
                .param("startDate",startDate)
                .param("endDate",endDate)
                .param("storeId", UserProfile.getInstance().getAuthRangeId())
                .param("action", "flashsummary")
                .build()
                .targetClass(InstantSummaryModle.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }
}
