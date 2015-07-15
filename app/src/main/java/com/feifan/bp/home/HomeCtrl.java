package com.feifan.bp.home;

import android.view.Gravity;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.feifan.bp.PlatformState;
import com.feifan.bp.R;
import com.feifan.bp.Utils;

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
        VersionRequest request = new VersionRequest(listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(!Utils.isNetworkAvailable()) {     // 网络不可用
                    Utils.showShortToast(R.string.error_message_text_offline);
                }else {                               // 其他原因
                    String msg = volleyError.getMessage();
                    if(msg != null) {
                        Utils.showShortToast(msg);
                    }else{
                        Utils.showShortToast(R.string.error_message_text_offline);
                    }


                }
            }
        });
        PlatformState.getInstance().getRequestQueue().add(request);
    }

}
