package com.feifan.bp.settings.helpcenter;


import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.feifan.bp.Constants;
import com.feifan.bp.PlatformState;
import com.feifan.bp.base.network.GetRequest;
import com.feifan.bp.base.network.JsonRequest;
import com.feifan.bp.base.network.UrlFactory;

/**
 * 帮助中心
 * Created by congjing
 */
public class HelpCenterCtrl {
    /**
     * 获取帮助中心列表
     * @param action  列表：getList  详情：getDetail（详情目前H5开发 未用）
     * @param pageIndex  页码
     * @param listener
     * @param errorListener
     */
    public static void getHelpCenter(String action,  int pageIndex, Listener listener, ErrorListener errorListener) {
        JsonRequest<HelpCenterModel> request = new GetRequest.Builder<HelpCenterModel>(UrlFactory.getHelpCenter()).errorListener(errorListener)
                .param("action", action)
                .param("pageIndex", Integer.toString(pageIndex))
                .param("limit", Integer.toString(Constants.LIST_MAX_LENGTH))
                .build()
                .targetClass(HelpCenterModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }
}
