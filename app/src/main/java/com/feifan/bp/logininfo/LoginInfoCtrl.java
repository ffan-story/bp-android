package com.feifan.bp.logininfo;

import com.android.volley.Response;
import com.feifan.bp.PlatformState;
import com.feifan.bp.network.GetRequest;
import com.feifan.bp.network.JsonRequest;
import com.feifan.bp.network.UrlFactory;

/**
 * Created by tianjun on 2015-10-30.
 */
public class LoginInfoCtrl {
    public static void getLoginInfo(String uid,
                                    Response.Listener<LoginInfoModel> listener) {
        JsonRequest<LoginInfoModel> getRequest = new GetRequest.Builder<LoginInfoModel>(UrlFactory.getLoginInfo())
                .param("uid", uid)
                .build()
                .targetClass(LoginInfoModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(getRequest);
    }
}
