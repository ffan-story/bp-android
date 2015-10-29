package com.feifan.bp.login;

import android.content.Context;

import com.android.volley.Response.Listener;
import com.feifan.bp.Constants;
import com.feifan.bp.PlatformState;
import com.feifan.bp.UserProfile;
import com.feifan.bp.net.BaseRequest;
import com.feifan.bp.net.BaseRequestProcessListener;
import com.feifan.bp.net.HttpEngine;
import com.feifan.bp.net.UrlFactory;
import com.feifan.bp.network.GetRequest;
import com.feifan.bp.network.JsonRequest;
import com.feifan.bp.network.PostRequest;

/**
 * Created by xuchunlei on 15/6/17.
 */
public class UserCtrl {

    public static final int USER_STATUS_NONE = 1;                         // 无状态
    public static final int USER_STATUS_LOGOUT = USER_STATUS_NONE + 1;    // 登出状态

    /**
     * 获取用户状态
     *
     * @return 状态值，见UserCtrl.USER_STATUS_XXX
     */
    public static int getStatus(Context context) {
        if (UserProfile.instance(context).getUid() == Constants.NO_INTEGER) {
            return USER_STATUS_LOGOUT;
        }
        return USER_STATUS_NONE;
    }

    public static void login(Context context, String account, String password,
                             BaseRequestProcessListener<UserModel> listener) {

        LoginRequest.Params params = BaseRequest.newParams(LoginRequest.Params.class);
        params.setPassword(password);
        params.setUserName(account);
        params.setAuthRangeType("store,merchant");
        HttpEngine.Builder.newInstance(context).
                setRequest(new LoginRequest(params, listener)).
                build().start();

    }

    public static void login(String account, String password, Listener<UserModel> listener) {
        JsonRequest<UserModel> request = new PostRequest<UserModel>(UrlFactory.getLoginUrl(), null)
                .param("userName", account)
                .param("password", password)
                .param("authRangeType", "store,merchant")
                .targetClass(UserModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);

        JsonRequest<UserModel> getRequest = new GetRequest.Builder<UserModel>(UrlFactory.getLoginUrl())
                .param("userName", account)
                .param("password", password)
                .param("authRangeType", "store,merchant")
                .build()
                .targetClass(UserModel.class)
                .listener(listener);

//        GetRequest<UserModel> request = new GetRequest.Builder<UserModel>(UrlFactory.getLoginUrl())
//                .build();

    }

//    public static void login(String userName, String password, Listener<JSONObject> listener){
//        JSONObject params = new JSONObject();
//        try {
//            params.put("userName", userName);
//            params.put("password", password);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        HttpService.getInstance().requestAsync(new JsonObjectRequest(
//                Method.POST, UrlFactory.getLoginUrl(), params, listener, null));
//    }

    public static void checkPermission(Context context, String uid,
                                       BaseRequestProcessListener<PermissionModel> listener) {
        CheckPermissionRequest.Params params = BaseRequest.newParams(CheckPermissionRequest.Params.class);
        params.setUid(uid);
        params.setNodeid(Authority.NODE_ID);
        HttpEngine.Builder.newInstance(context).
                setRequest(new CheckPermissionRequest(params, listener)).
                build().start();
    }
}
