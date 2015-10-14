package com.feifan.bp.login;

import android.content.Context;

import com.feifan.bp.Constants;
import com.feifan.bp.UserProfile;
import com.feifan.bp.net.Authority;
import com.feifan.bp.net.BaseRequest;
import com.feifan.bp.net.BaseRequestProcessListener;
import com.feifan.bp.net.HttpEngine;

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
        params.setAuthRangeType("store");
        HttpEngine.Builder.newInstance(context).
                setRequest(new LoginRequest(params, listener)).
                build().start();
    }

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
