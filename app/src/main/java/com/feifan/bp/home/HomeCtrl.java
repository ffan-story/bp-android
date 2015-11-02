package com.feifan.bp.home;


import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.feifan.bp.PlatformState;
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
        JsonRequest<VersionModel> request = new PostRequest<VersionModel>(UrlFactory.getCheckVersionUrl(), errorListener)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

}
