package com.feifan.bp.login;

import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;
import com.feifan.bp.Constants;
import com.feifan.bp.PlatformState;
import com.feifan.bp.UserProfile;
import com.feifan.bp.envir.EnvironmentManager;
import com.feifan.bp.network.BaseModel;
import com.feifan.bp.network.CookieRequest;
import com.feifan.bp.network.DefaultErrorListener;
import com.feifan.bp.network.PostRequest;
import com.feifan.bp.network.UrlFactory;
import com.feifan.bp.network.GetRequest;
import com.feifan.bp.network.JsonRequest;

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
    public static int getStatus() {
        if (UserProfile.getInstance().getUid() == Constants.NO_INTEGER) {
            return USER_STATUS_LOGOUT;
        }
        return USER_STATUS_NONE;
    }

    /**
     * 登录请求
     * @param account     账号名
     * @param password    密码
     * @param listener    响应回调
     */
    public static void login(String account, String password, Listener<UserModel> listener, ErrorListener errorListener) {
        JsonRequest<UserModel> request = new CookieRequest<UserModel>(UrlFactory.getLoginUrl(),errorListener)
                .param("userName", account)
                .param("password", password)
                .param("authRangeType", "store,merchant")
                .targetClass(UserModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    /**
     * xadmin 登录验证
     * @param token    登录token
     * @param listener 响应回调
     */
    public static void loginConfirm(String token,Listener<BaseModel> listener){
        JsonRequest<BaseModel> request = new PostRequest<>(UrlFactory.getLoginConfirmUrl(),new DefaultErrorListener())
                .param("token",token)
                .targetClass(BaseModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    /**
     * 检查权限
     * @param uId
     * @param listener
     */
    public static void checkPermissions(int uId, Listener<AuthListModel> listener){

        JsonRequest<AuthListModel> request = new GetRequest
                .Builder<AuthListModel>(UrlFactory.getAuthorizeUrl())
                .param("uid", String.valueOf(uId))
                .param("nodeid", EnvironmentManager.getAuthFactory().getNodeId())
                .param("appType", "bpMobile")
                .build()
                .targetClass(AuthListModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

}
