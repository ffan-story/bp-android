package com.feifan.bp.home;

import com.android.volley.Response.Listener;
import com.feifan.bp.PlatformState;
import com.feifan.bp.home.Model.MenuListModel;
import com.feifan.bp.home.Model.MenuModel;
import com.feifan.bp.home.Model.MerchantModel;
import com.feifan.bp.home.Model.StoreModel;

import java.util.ArrayList;

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
