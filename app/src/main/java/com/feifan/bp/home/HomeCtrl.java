package com.feifan.bp.home;



import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.feifan.bp.BuildConfig;
import com.feifan.bp.Constants;
import com.feifan.bp.PlatformState;
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

    /**
     * 获取消息列表
     * @param userId
     * @param pageIndex
     * @param listener
     */
    public static void messageList(String userId,  int pageIndex, Listener listener, ErrorListener errorListener) {
        JsonRequest<MessageModel> request = new GetRequest.Builder<MessageModel>(UrlFactory.getMessgeList()).errorListener(errorListener)
                .param("userId", userId)
                .param("userType", "1")//固定传1
                .param("pageIndex", Integer.toString(pageIndex))
                .param("limit", Integer.toString(Constants.LIST_MAX_LENGTH))
                .build()
                .targetClass(MessageModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    /**
     * 设置消息状态
     * @param userid
     * @param maillnboxid
     * @param listener
     */
    public static void setMessageStatusRead(String userid,  String maillnboxid, Listener listener, ErrorListener errorListener) {
        JsonRequest<MessageStatusModel> request = new PostRequest<MessageStatusModel>(UrlFactory.getMessgeListStatus(), new DefaultErrorListener())
                .param("userId", userid)
                .param("mailInboxId", maillnboxid)
                .param("mailStatus", Constants.READ)
                .targetClass(MessageStatusModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    /**
     * 是否显示红点
     * @param merchantId
     * @param storeId
     * @param userType
     * @param listener
     */

//    agid = 340;
//    appType = bpMobile;
//    clientAgent = "iPhone/iPhone OS/9.1/750*1334";
//    clientType = iOS;
//    loginToken = 86bdbcb585d32a4d19f54e30d4e001ef;
//    merchantId = "";
//    storeId = 9050588;
//    uid = 13980;
//    userType = 1;
//    version = 7;

    public static void isShowRedDot(String merchantId, String storeId, String userType, Listener listener, ErrorListener errorListener) {
        JsonRequest<ReadMessageModel> request = new GetRequest.Builder<ReadMessageModel>(UrlFactory.getReadMessage()).errorListener(errorListener)
                .param("merchantId", merchantId)
                .param("storeId", storeId)
                .param("userType", userType)
                .build()
                .targetClass(ReadMessageModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }
}
