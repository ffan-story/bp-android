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
    public static void fetchMerchantDetail(String merchantId, Listener<MerchantModel> listener) {
        MerchantRequest request = new MerchantRequest(merchantId, listener ,null);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    /**
     * 获取门店详情信息
     * @param storeId
     * @param listener
     */
    public static void fetchStoreDetail(String storeId, Listener<StoreModel> listener) {
        StoreRequest request = new StoreRequest(storeId, listener, null);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    /**
     * 获取业务管理菜单
     * @param agId
     * @param listener
     */
    public static void fetchManageMenus(int agId, Listener<MenuListModel> listener) {
        ManageRequest request = new ManageRequest(agId, listener, null);
        PlatformState.getInstance().getRequestQueue().add(request);
    }
}
