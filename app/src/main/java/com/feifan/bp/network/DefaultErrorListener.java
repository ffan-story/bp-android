package com.feifan.bp.network;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.feifan.bp.LaunchActivity;
import com.feifan.bp.PlatformState;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.Utils;
import com.feifan.bp.util.DialogUtil;
import com.feifan.bp.util.LogUtil;
import com.feifan.bp.network.JsonRequest.StatusError;

/**
 * 网络请求错误Listener默认实现
 * <pre>
 *     适用于Volley网络请求框架，没有独立界面显示错误的情况
 * </pre>
 * <p>
 * Created by xuchunlei on 15/10/31.
 */
public class DefaultErrorListener implements ErrorListener {

    private static final String TAG = DefaultErrorListener.class.getSimpleName();

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        final Context context = PlatformState.getApplicationContext();
        if (!Utils.isNetworkAvailable(context)) {     // 网络不可用
            Utils.showShortToast(context, R.string.error_message_text_offline, Gravity.CENTER);
        } else {                               // 其他原因
            if (volleyError instanceof StatusError) {
                final StatusError error = (StatusError) volleyError;
                switch (error.getStatus()) {
                    case StatusError.STATUS_COOKIE_EXPIRE:
                        DialogUtil.showCookieDialog(error.getMessage());
                        return;
                    default:
                        break;
                }
            }

            String msg = volleyError.getMessage();
            if (!TextUtils.isEmpty(msg) && msg.trim().length() > 0) {
                LogUtil.i(TAG, "error msg=" + msg);
                if (!Utils.isChineseChar(msg)) {
                    msg = context.getString(R.string.error_message_text_network_data_fail);
                }
                Utils.showShortToastSafely(msg);
            } else {
                Utils.showShortToastSafely(R.string.error_message_text_offline);
            }
        }
    }
}
