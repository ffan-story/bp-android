package com.feifan.bp.home.userinfo;

import com.android.volley.Response;
import com.feifan.bp.PlatformState;
import com.feifan.bp.network.GetRequest;
import com.feifan.bp.network.JsonRequest;
import com.feifan.bp.network.UrlFactory;

/**
 * Created by tianjun on 2015-10-30.
 */
public class UserInfoCtrl {
    public static void getLoginInfo(String uid,
                                    Response.Listener<UserInfoModel> listener) {
        JsonRequest<UserInfoModel> getRequest = new GetRequest.Builder<UserInfoModel>(UrlFactory.getLoginInfo())
                .param("uid", uid)
                .build()
                .targetClass(UserInfoModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(getRequest);
    }
}
