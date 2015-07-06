package com.feifan.bp.password;

import android.view.Gravity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feifan.bp.PlatformState;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
 

import java.util.HashMap;
import java.util.Map;

 
public class PasswordCtrl {

    /**
     * 重置密码
     * @param oldPassword
     * @param newPassword
     * @param listener
     */
    public static void resetPassword(String oldPassword, String newPassword, Response.Listener<PasswordModel> listener) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("oldPassword", oldPassword);
        params.put("newPassword", newPassword);
        params.put("uId",String.valueOf(PlatformState.getInstance().getUserProfile().getUid()));
        ResetPasswordRequest request = new ResetPasswordRequest(listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                if(!Utils.isNetworkAvailable()) {     // 网络不可用
                    Utils.showShortToast(R.string.error_message_text_offline, Gravity.CENTER);
                }else {                               // 其他原因
                    Utils.showShortToast(volleyError.getMessage());
                }

            }
        }, params);
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
                    Utils.showShortToast(volleyError.getMessage());
                } 
            }
        }, phoneNum);
        PlatformState.getInstance().getRequestQueue().add(request);
    }
    
    /**
     * 发送手机验证码
     * @param phoneNum     
     * @param listener
     */
    public static void sendSMSCode(String phoneNum, Response.Listener<PasswordModel> listener) {
        Map<String, String> params = new HashMap<String, String>(); 
        params.put("mobile", phoneNum);
        params.put("deviceType", "0");
        params.put("content", "");
        params.put("contentType", "0");
        params.put("destAppId", "1");
        params.put("sendTime", "");
        params.put("validTime", String.valueOf(System.currentTimeMillis()/1000));
        params.put("templateId", "160");
        //
        params.put("deviceList", "");
        params.put("argsList", "");
        SendSMSCodeRequest request = new SendSMSCodeRequest(listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) { 
                if(!Utils.isNetworkAvailable()) {     // 网络不可用
                    Utils.showShortToast(R.string.error_message_text_offline, Gravity.CENTER);
                }else {                               // 其他原因
                    Utils.showShortToast(volleyError.getMessage());
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
       Map<String, String> params = new HashMap<String, String>(); 
      ForgetPasswordRequest request = new ForgetPasswordRequest(listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                if(!Utils.isNetworkAvailable()) {     // 网络不可用
                    Utils.showShortToast(R.string.error_message_text_offline, Gravity.CENTER);
                }else {                               // 其他原因
                    Utils.showShortToast(volleyError.getMessage());
                }

            }
        }, phoneNum,authCode,keyCode);
        PlatformState.getInstance().getRequestQueue().add(request);  
    }
}
