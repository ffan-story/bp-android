package com.feifan.bp.home;



import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.feifan.bp.PlatformState;
import com.feifan.bp.network.JsonRequest;
import com.feifan.bp.network.PostRequest;
import com.feifan.bp.network.UrlFactory;
import com.feifan.bp.Constants;
import com.feifan.bp.network.GetRequest;

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
        JsonRequest<VersionModel> request = new PostRequest<VersionModel>(UrlFactory.getCheckVersionUrl(), errorListener)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    /**
     * 获取消息列表
     *
     * @param userId
     * @param pageIndex
     * @param listener
     */
    public static void messageList(String userId,  String pageIndex, Listener listener) {
        JsonRequest<MessageModel> request = new GetRequest.Builder<MessageModel>(UrlFactory.getMeaageList())
                .param("userId", userId)
                .param("userType", "1")//固定传1
                .param("pageIndex", pageIndex)
                .param("limit", Constants.LIST_MAX_LENGTH)
                .build()
                .targetClass(MessageModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }
}
