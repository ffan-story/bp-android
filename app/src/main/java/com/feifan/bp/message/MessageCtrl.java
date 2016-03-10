package com.feifan.bp.message;

import com.android.volley.Response;
import com.feifan.bp.Constants;
import com.feifan.bp.PlatformState;
import com.feifan.bp.network.GetRequest;
import com.feifan.bp.network.JsonRequest;
import com.feifan.bp.network.UrlFactory;

/**
 * Created by apple on 16/3/3.
 */
public class MessageCtrl {
    /**
     * 获取消息列表
     * @param userId
     * @param pageIndex
     * @param listener
     */
    public static void getMessageCategoryList(String userId,  int pageIndex,String messType, Response.Listener listener, Response.ErrorListener errorListener) {
        JsonRequest<MessageModel> request = new GetRequest.Builder<MessageModel>(UrlFactory.getMessgeList()).errorListener(errorListener)
                .param("userId", userId)
                .param("userType", "1")//固定传1
                .param("pageIndex", Integer.toString(pageIndex))
                .param("subId", messType)
                .param("limit", Integer.toString(Constants.LIST_MAX_LENGTH))
                .build()
                .targetClass(MessageModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }
}
