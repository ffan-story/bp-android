package com.feifan.bp.home;

import com.android.volley.Response.Listener;
import com.feifan.bp.PlatformState;

/**
 * 主界面控制类
 *
 * Created by xuchunlei on 15/6/19.
 */
public class HomeCtrl {

    /**
     * 获取商户详情信息
     * @param listener
     */
    public static void checkUpgrade(Listener<VersionModel> listener) {
        VersionRequest request = new VersionRequest(listener, null);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

}
