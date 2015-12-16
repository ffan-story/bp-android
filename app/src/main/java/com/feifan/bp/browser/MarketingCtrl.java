package com.feifan.bp.browser;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.feifan.bp.PlatformState;
import com.feifan.bp.network.GetRequest;
import com.feifan.bp.network.JsonRequest;
import com.feifan.bp.network.UrlFactory;


public class MarketingCtrl {
    /**
     *
     * @param stroeId
     * @param listener
     * @param errorListener
     */
    public static void marketingStatus(String stroeId, Listener listener, ErrorListener errorListener) {
        JsonRequest<MarketingModel> request = new GetRequest.Builder<MarketingModel>(UrlFactory.getMarketingContract()).errorListener(errorListener)
                .param("storeid", stroeId)
                .build()
                .targetClass(MarketingModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

}
