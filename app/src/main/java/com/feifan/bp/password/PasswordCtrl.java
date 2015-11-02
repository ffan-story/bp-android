package com.feifan.bp.password;

import com.android.volley.Response.Listener;
import com.feifan.bp.PlatformState;
import com.feifan.bp.UserProfile;
import com.feifan.bp.network.BaseModel;
import com.feifan.bp.network.DefaultErrorListener;
import com.feifan.bp.network.GetRequest;
import com.feifan.bp.network.JsonRequest;
import com.feifan.bp.network.PostRequest;
import com.feifan.bp.network.UrlFactory;


public class PasswordCtrl {

    public static void resetPassword(String oldPassword, String newPassword, Listener<PasswordModel> listener) {
        JsonRequest<PasswordModel> request = new PostRequest<PasswordModel>(UrlFactory.getResetPasswordUrl(), new DefaultErrorListener())
                .param("newPassword", newPassword)
                .param("oldPassword", oldPassword)
                .param("uId", String.valueOf(UserProfile.getInstance().getUid()))
                .targetClass(PasswordModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    public static void checkPhoneNumExist(String phoneNum,
                                          Listener<PasswordModel> listener) {
        JsonRequest<PasswordModel> request = new GetRequest.Builder<PasswordModel>(UrlFactory.getCheckPhoneNumExistUrl())
                .param("phone", phoneNum)
                .build()
                .targetClass(PasswordModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    public static void sendSMSCode(String phoneNum, String content,Listener<BaseModel> listener) {
        JsonRequest<BaseModel> request = new PostRequest<>(UrlFactory.getSendSMSUrl(), new DefaultErrorListener())
                .param("phone", phoneNum)
                .param("content", content)
                .param("templateId", "160")
                .targetClass(BaseModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    public static void forgetPassword(String phoneNum, String authCode, String keyCode, Listener listener) {
        JsonRequest<PasswordModel> request = new GetRequest.Builder<PasswordModel>(UrlFactory.getForgetPasswordUrl())
                .param("phone", phoneNum)
                .param("authCode", authCode)
                .param("keyCode", keyCode)
                .build()
                .targetClass(PasswordModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }
}
