package com.feifan.bp.login;

import com.android.volley.Response;
import com.feifan.bp.Constants;
import com.feifan.bp.PlatformApplication;
import com.feifan.bp.PlatformState;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuchunlei on 15/6/17.
 */
public class UserCtrl {

    public static final int USER_STATUS_NONE = 1;                      // 无状态
    public static final int USER_STATUS_LOGOUT = USER_STATUS_NONE + 1; // 登出状态

    /**
     * 获取用户状态
     * @return 状态值，见UserCtrl.USER_STATUS_XXX
     */
    public static int getStatus() {
        UserProfile profile = PlatformState.getInstance().getUserProfile();
        if(profile.getUid() == Constants.NO_INTEGER) {
            return USER_STATUS_LOGOUT;
        }
        return USER_STATUS_NONE;
    }

    /**
     * 登录
     * @param accout
     * @param password
     * @param listener
     */
    public static void login(String accout, String password, Response.Listener<UserModel> listener) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userName", accout);
        params.put("password", password);
        LoginRequest request = new LoginRequest(listener, null, params);
        PlatformState.getInstance().getRequestQueue().add(request);
    }
}
