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
}
