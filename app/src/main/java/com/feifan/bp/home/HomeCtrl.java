package com.feifan.bp.home;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.feifan.bp.PlatformState;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.net.BaseRequestProcessListener;
import com.feifan.bp.net.DefaultHttpProcessor;
import com.feifan.bp.net.HttpEngine;

/**
 * 主界面控制类
 * <p/>
 * Created by xuchunlei on 15/6/19.
 */
public class HomeCtrl {

    /**
     * 获取商户详情信息
     *
     * @param listener
     */
    public static void checkVersion(Context context,
                                    BaseRequestProcessListener<CheckVersionModel> listener) {
        HttpEngine.Builder builder = HttpEngine.Builder.newInstance(context);
        builder.setRequest(new CheckVersionRequest(listener)).build().start();
    }

}
