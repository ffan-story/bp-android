package com.feifan.bp.instantevent;



import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.feifan.bp.BuildConfig;
import com.feifan.bp.Constants;
import com.feifan.bp.PlatformState;
import com.feifan.bp.home.MessageModel;
import com.feifan.bp.home.MessageStatusModel;
import com.feifan.bp.home.ReadMessageModel;
import com.feifan.bp.home.VersionModel;
import com.feifan.bp.network.DefaultErrorListener;
import com.feifan.bp.network.GetRequest;
import com.feifan.bp.network.JsonRequest;
import com.feifan.bp.network.PostRequest;
import com.feifan.bp.network.UrlFactory;

/**
 * 主界面控制类
 * <p/>
 * Created by xuchunlei on 15/6/19.
 */
public class InstCtrl {

    /**
     * 添加活动商品列表
     * @param promotionCode
     * @param merchantId
     * @param storeId
     * @param pageIndex
     * @param listener
     */
    public static void getInstEventGoodsList(String promotionCode,String merchantId, String storeId, int pageIndex, Listener listener) {
        JsonRequest<InstEventGoodsListModel> request = new GetRequest.Builder<InstEventGoodsListModel>(UrlFactory.getInstEventGoodsList())
                .param("promotionCode", promotionCode)
                .param("merchantId", merchantId)
                .param("storeId", storeId)
                .param("enrollStatus", "0")
                .param("pageIndex", String.valueOf(pageIndex))
                .param("limit",String.valueOf(Constants.LIST_MAX_LENGTH))
                .build()
                .targetClass(InstEventGoodsListModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }
}
