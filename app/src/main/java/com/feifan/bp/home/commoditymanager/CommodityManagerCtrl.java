package com.feifan.bp.home.commoditymanager;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.feifan.bp.PlatformState;
import com.feifan.bp.UserProfile;
import com.feifan.bp.network.GetRequest;
import com.feifan.bp.network.JsonRequest;
import com.feifan.bp.network.UrlFactory;

/**
 * 商品管理界面控制类
 * Created by kontar on 2015/12/22.
 */
public class CommodityManagerCtrl {
    /**
     * 获取闪购商品列表
     * http://api.sit.ffan.com/goods/v1/goods?outlet=9053066&status=00&limit=10&offset=0&appSource=2&redundancy02=1
     * @param listener
     * @param errorListener
     */
    public static void getCommodityInfo(Listener listener,ErrorListener errorListener){
        JsonRequest<InstantsBuyModle> request = new GetRequest.Builder<InstantsBuyModle>(UrlFactory.getInstantsBuyCommodity())
                .errorListener(errorListener)
                .param("storeId", UserProfile.getInstance().getAuthRangeId())
                .param("appSource","2")
                .param("redundancy02","1")
                .build()
                .targetClass(InstantsBuyModle.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }
}
