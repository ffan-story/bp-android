package com.feifan.bp.home;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.feifan.bp.BuildConfig;
import com.feifan.bp.Constants;
import com.feifan.bp.PlatformState;
import com.feifan.bp.UserProfile;
import com.feifan.bp.base.network.DefaultErrorListener;
import com.feifan.bp.base.network.GetRequest;
import com.feifan.bp.base.network.JsonRequest;
import com.feifan.bp.base.network.PostRequest;
import com.feifan.bp.base.network.UrlFactory;
import com.feifan.bp.message.MessageStatusModel;

/**
 * 主界面控制类
 * <p/>
 * Created by xuchunlei on 15/6/19.
 */
public class HomeCtrl {

    /**
     * 检查更新
     *
     * @param listener
     */
    public static void checkVersion(Listener<VersionModel> listener, ErrorListener errorListener) {
        JsonRequest<VersionModel> request = new PostRequest<VersionModel>(UrlFactory.checkVersionUpdate(), errorListener)
                .param("currVersionCode", String.valueOf(BuildConfig.VERSION_CODE))
                .targetClass(VersionModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    public static void setMessageStatusRead(String userid,  String maillnboxid, Listener listener) {
        setMessageStatusRead(userid,  maillnboxid, listener, new DefaultErrorListener());
    }
    /**
     * 设置消息状态
     * @param userid
     * @param maillnboxid
     * @param listener
     */
    public static void setMessageStatusRead(String userid,  String maillnboxid, Listener listener, ErrorListener errorListener) {
        JsonRequest<MessageStatusModel> request = new PostRequest<MessageStatusModel>(UrlFactory.getMessgeListStatus(), errorListener)
                .param("userId", userid)
                .param("mailInboxId", maillnboxid)
                .param("mailStatus", Constants.READ)
                .targetClass(MessageStatusModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    public static final String USER_TYPE = "1";
    /**
     * 获得未读提示状态，目前包括：退款售后，消息
     * @param listener
     */
    public static void getUnReadtatus(Response.Listener listener) {
        // 获取未读提示状态
        String storeId = null;
        String merchantId = null;
        if (UserProfile.getInstance().isStoreUser()) {
            storeId = UserProfile.getInstance().getAuthRangeId();
        } else {
            merchantId = UserProfile.getInstance().getAuthRangeId();
        }

        JsonRequest<ReadMessageModel> request = new GetRequest.Builder<ReadMessageModel>(UrlFactory.getReadMessage())
                .param("merchantId", merchantId)
                .param("storeId", storeId)
                .param("userType", USER_TYPE)
                .build()
                .targetClass(ReadMessageModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }
}
