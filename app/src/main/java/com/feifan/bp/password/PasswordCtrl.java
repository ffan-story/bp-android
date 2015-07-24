package com.feifan.bp.password;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feifan.bp.PlatformState;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.net.BaseRequestProcessListener;
import com.feifan.bp.net.HttpEngine;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class PasswordCtrl {

    /**
     * 重置密码
     *
     * @deprecated
     * @param oldPassword
     * @param newPassword
     * @param listener
     */
    public static void resetPassword(String oldPassword, String newPassword, Response.Listener<PasswordModel> listener, Response.ErrorListener errorListener) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("oldPassword", oldPassword);
        params.put("newPassword", newPassword);
        params.put("uId", String.valueOf(PlatformState.getInstance().getUserProfile().getUid()));
        ResetPasswordRequest request = new ResetPasswordRequest(listener, errorListener, params);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    public static void resetPassword2(Context context, String oldPassword, String newPassword, BaseRequestProcessListener<PasswordModel2> listener) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("oldPassword", oldPassword);
        params.put("newPassword", newPassword);
        params.put("uId", String.valueOf(PlatformState.getInstance().getUserProfile().getUid()));
        HttpEngine.Builder.newInstance(context).
                setRequest(new ResetPasswordRequest2(params, listener)).
                build().start();
    }

    /**
     * 验证手机号
     *
     * @deprecated
     * @param phoneNum
     * @param listener
     */
    public static void checkPhoneNumExist(String phoneNum, Response.Listener<PasswordModel> listener) {

        CheckPhoneNumExistRequest request = new CheckPhoneNumExistRequest(listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                if (!Utils.isNetworkAvailable()) {     // 网络不可用
                    Utils.showShortToast(R.string.error_message_text_offline, Gravity.CENTER);
                } else {                               // 其他原因
                    String msg = volleyError.getMessage();
                    if (!TextUtils.isEmpty(msg) && msg.trim().length() > 0) {
                        Utils.showShortToast(msg, Gravity.CENTER);
                    } else {
                        Utils.showShortToast(R.string.error_message_text_offline, Gravity.CENTER);
                    }
                }
            }
        }, phoneNum);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    public static void checkPhoneNumExist2(Context context, String phoneNum,
                                           BaseRequestProcessListener<PasswordModel2> listener) {
        HttpEngine.Builder.newInstance(context).
                setRequest(new CheckPhoneNumExistRequest2(phoneNum, listener)).
                build().start();
    }

    /**
     * 发送手机验证码
     * @deprecated
     * @param phoneNum
     * @param listener
     */
    public static void sendSMSCode(String phoneNum, String content, Response.Listener<PasswordModel> listener) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phoneNum);
        params.put("content", content);
        params.put("templateId", "160");
        SendSMSCodeRequest request = new SendSMSCodeRequest(listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (!Utils.isNetworkAvailable()) {     // 网络不可用
                    Utils.showShortToast(R.string.error_message_text_offline, Gravity.CENTER);
                } else {                               // 其他原因
                    String msg = volleyError.getMessage();
                    if (!TextUtils.isEmpty(msg) && msg.trim().length() > 0) {
                        Utils.showShortToast(msg, Gravity.CENTER);
                    } else {
                        Utils.showShortToast(R.string.error_message_text_offline, Gravity.CENTER);
                    }
                }
            }
        }, params);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    public static void sendSMSCode2(Context context, String phoneNum, String content, BaseRequestProcessListener<PasswordModel2> listener) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phoneNum);
        params.put("content", content);
        params.put("templateId", "160");
        HttpEngine.Builder.newInstance(context).
                setRequest(new SendSMSCodeRequest2(params, listener)).
                build().start();
    }

    /**
     * 忘记密码
     *
     * @deprecated
     * @param phoneNum 手机号
     * @param authCode 权限码
     * @param keyCode  key码
     * @param listener
     */
    public static void forgetPassword(String phoneNum, String authCode, String keyCode, Response.Listener<PasswordModel> listener) {

        ForgetPasswordRequest request = new ForgetPasswordRequest(listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                if (!Utils.isNetworkAvailable()) {     // 网络不可用
                    Utils.showShortToast(R.string.error_message_text_offline, Gravity.CENTER);
                } else {                               // 其他原因
                    String msg = volleyError.getMessage();
                    if (!TextUtils.isEmpty(msg) && msg.trim().length() > 0) {
                        Utils.showShortToast(msg, Gravity.CENTER);
                    } else {
                        Utils.showShortToast(R.string.error_message_text_offline, Gravity.CENTER);
                    }
                }
            }
        }, phoneNum, authCode, keyCode);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    public static void forgetPassword2(Context context, String phoneNum, String authCode,
                                       String keyCode, BaseRequestProcessListener<PasswordModel2> listener) {
        HttpEngine.Builder.newInstance(context).
                setRequest(new ForgetPasswordRequest2(phoneNum, authCode, keyCode, listener)).
                build().start();
    }
}
