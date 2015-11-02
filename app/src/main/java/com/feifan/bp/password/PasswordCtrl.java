package com.feifan.bp.password;

import android.content.Context;

import com.android.volley.Response.Listener;
import com.feifan.bp.PlatformState;
import com.feifan.bp.UserProfile;
import com.feifan.bp.net.BaseRequest;
import com.feifan.bp.net.BaseRequestProcessListener;
import com.feifan.bp.net.HttpEngine;
import com.feifan.bp.network.GetRequest;
import com.feifan.bp.network.JsonRequest;
import com.feifan.bp.network.UrlFactory;


public class PasswordCtrl {

    public static void resetPassword(Context context, String oldPassword, String newPassword,
                                     BaseRequestProcessListener<PasswordModel> listener) {
        ResetPasswordRequest.Params params = BaseRequest.newParams(ResetPasswordRequest.Params.class);
        params.setNewPassword(newPassword);
        params.setOldPassword(oldPassword);
        params.setuId(String.valueOf(UserProfile.getInstance()));
        HttpEngine.Builder.newInstance(context).
                setRequest(new ResetPasswordRequest(params, listener)).
                build().start();
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

    public static void sendSMSCode(Context context, String phoneNum, String content,
                                   BaseRequestProcessListener<PasswordModel> listener) {
        SendSMSCodeRequest.Params params = BaseRequest.newParams(SendSMSCodeRequest.Params.class);
        params.setPhone(phoneNum);
        params.setContent(content);
        params.setTemplateId("160");
        HttpEngine.Builder.newInstance(context).
                setRequest(new SendSMSCodeRequest(params, listener)).
                build().start();
    }

//    public static void forgetPassword(Context context, String phoneNum, String authCode,
//                                       String keyCode, BaseRequestProcessListener<PasswordModel> listener) {
//        ForgetPasswordRequest.Params params = BaseRequest.newParams(ForgetPasswordRequest.Params.class);
//        params.setPhone(phoneNum);
//        params.setAuthCode(authCode);
//        params.setKeyCode(keyCode);
//        HttpEngine.Builder.newInstance(context).setRequest(new ForgetPasswordRequest(params, listener)).
//                build().start();
//    }

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
