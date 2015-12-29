package com.feifan.bp.home.commoditymanager;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.feifan.bp.PlatformState;
import com.feifan.bp.UserProfile;
import com.feifan.bp.network.GetRequest;
import com.feifan.bp.network.JsonRequest;
import com.feifan.bp.network.UrlFactory;

/**
 * 商品管理界面控制类
 * http://sop.sit.ffan.com/goods/goods_app/ajax_status_list
 * Created by kontar on 2015/12/22.
 */
public class CommodityManagerCtrl {
    public static void getCommodityInfo(Listener listener,ErrorListener errorListener){
        JsonRequest<InstantsBuyModle> request = new GetRequest.Builder<InstantsBuyModle>(UrlFactory.getInstantsBuyCommodity())
                .errorListener(errorListener)
                .build()
                .header("uid", String.valueOf(UserProfile.getInstance().getUid()))
                .header("loginToken", UserProfile.getInstance().getLoginToken())
                .header("appType", "bpMobile")
                .targetClass(InstantsBuyModle.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }
}
