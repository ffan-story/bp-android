package com.feifan.bp.password;

import android.text.TextUtils;
import android.view.Gravity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feifan.bp.PlatformState;
import com.feifan.bp.R;
import com.feifan.bp.Utils;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

 
public class PasswordCtrl {

    /**
     * 重置密码
     * @param oldPassword
     * @param newPassword
     * @param listener
     */
    public static void resetPassword(String oldPassword, String newPassword, Response.Listener<PasswordModel> listener, Response.ErrorListener errorListener) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("oldPassword", oldPassword);
        params.put("newPassword", newPassword);
        params.put("uId",String.valueOf(PlatformState.getInstance().getUserProfile().getUid()));
        ResetPasswordRequest request = new ResetPasswordRequest(listener, errorListener, params);
        PlatformState.getInstance().getRequestQueue().add(request);
    }
    
    /**
     * 验证手机号
     * @param phoneNum     
     * @param listener
     */
    public static void checkPhoneNumExist(String phoneNum, Response.Listener<PasswordModel> listener) {
       
        CheckPhoneNumExistRequest request = new CheckPhoneNumExistRequest(listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                if(!Utils.isNetworkAvailable()) {     // 网络不可用
                    Utils.showShortToast(R.string.error_message_text_offline, Gravity.CENTER);
                }else {                               // 其他原因
                    String msg = volleyError.getMessage();
                    if(!TextUtils.isEmpty(msg) && msg.trim().length()>0) {
                        Utils.showShortToast(msg, Gravity.CENTER);
                    }else{
                        Utils.showShortToast(R.string.error_message_text_offline, Gravity.CENTER);
                    }
                } 
            }
        }, phoneNum);
        PlatformState.getInstance().getRequestQueue().add(request);
    }
    
//    /**
//     * 发送手机验证码
//     * @param phoneNum
//     * @param listener
//     */
//    public static void sendSMSCode(String phoneNum, String smsCode,Response.Listener<PasswordModel> listener) {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("mobile", phoneNum);
//        params.put("deviceType", "0");
//        params.put("content", "");
//        params.put("contentType", "0");
//        params.put("destAppId", "1");
//        params.put("sendTime", "");
//        Long time=(System.currentTimeMillis()/1000)+60;
//        params.put("validTime", String.valueOf(time));
//        params.put("templateId", "160");
//        params.put("deviceList", "["+phoneNum+"]");
//        params.put("argsList", "[["+smsCode+"]]");
//        SendSMSCodeRequest request = new SendSMSCodeRequest(listener, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                if(!Utils.isNetworkAvailable()) {     // 网络不可用
//                    Utils.showShortToast(R.string.error_message_text_offline, Gravity.CENTER);
//                }else {                               // 其他原因
//                    String msg = volleyError.getMessage();
//                    if(!TextUtils.isEmpty(msg)) {
//                        Utils.showShortToast(msg, Gravity.CENTER);
//                    }else{
//                        Utils.showShortToast(R.string.error_message_text_offline, Gravity.CENTER);
//                    }
//                }
//            }
//        }, params);
//        PlatformState.getInstance().getRequestQueue().add(request);
//    }


    /**
     * 发送手机验证码
     * @param phoneNum
     * @param listener
     */
    public static void sendSMSCode(String phoneNum, String content,Response.Listener<PasswordModel> listener) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phoneNum);
        params.put("content", content);
        params.put("templateId", "160");
        SendSMSCodeRequest request = new SendSMSCodeRequest(listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(!Utils.isNetworkAvailable()) {     // 网络不可用
                    Utils.showShortToast(R.string.error_message_text_offline, Gravity.CENTER);
                }else {                               // 其他原因
                    String msg = volleyError.getMessage();
                    if(!TextUtils.isEmpty(msg) && msg.trim().length()>0) {
                        Utils.showShortToast(msg, Gravity.CENTER);
                    }else{
                        Utils.showShortToast(R.string.error_message_text_offline, Gravity.CENTER);
                    }
                }
            }
        }, params);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    /**
     * 忘记密码
     * @param  phoneNum 手机号
     * @param  authCode 权限码
     * @param  keyCode  key码   
     * @param  listener
     */
    public static void forgetPassword(String phoneNum,String authCode,String keyCode, Response.Listener<PasswordModel> listener) {
    
      ForgetPasswordRequest request = new ForgetPasswordRequest(listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                if(!Utils.isNetworkAvailable()) {     // 网络不可用
                    Utils.showShortToast(R.string.error_message_text_offline, Gravity.CENTER);
                }else {                               // 其他原因
                    String msg = volleyError.getMessage();
                    if(!TextUtils.isEmpty(msg) && msg.trim().length()>0) {
                        Utils.showShortToast(msg, Gravity.CENTER);
                    }else{
                        Utils.showShortToast(R.string.error_message_text_offline, Gravity.CENTER);
                    }
                }
            }
        }, phoneNum,authCode,keyCode);
        PlatformState.getInstance().getRequestQueue().add(request);  
    }
}
