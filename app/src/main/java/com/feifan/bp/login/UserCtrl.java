package com.feifan.bp.login;

import com.feifan.bp.Constants;
import com.feifan.bp.PlatformApplication;
import com.feifan.bp.PlatformState;

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
        if(profile.getUser().equals(Constants.NO_STRING)) {
            return USER_STATUS_LOGOUT;
        }
        return USER_STATUS_NONE;
    }
}
