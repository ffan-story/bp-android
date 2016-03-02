package com.feifan.bp.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Response.ErrorListener;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.feifan.bp.LaunchActivity;
import com.feifan.bp.PlatformState;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.Utils;
import com.feifan.bp.network.JsonRequest.StatusError;
import com.feifan.bp.util.DialogUtil;
import com.feifan.material.MaterialDialog;

import org.json.JSONException;

import java.io.IOException;
import java.net.SocketException;

/**
 * VolleyFragment
 * <pre>
 *     支持使用Volley框架进行网络请求
 * </pre>
 * Created by xuchunlei on 16/2/29.
 */
public abstract class VolleyFragment extends ProgressFragment {

    /**
     * Volley请求错误回调
     */
    public static class DefaultErrorListener implements ErrorListener {

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            // 处理状态错误
            if (volleyError instanceof StatusError) {
                StatusError error = (StatusError) volleyError;
                switch (error.getStatus()) {
                    case StatusError.STATUS_COOKIE_EXPIRE:      // Cookie验证失败
                        // 系统提示框
                        DialogUtil.showCookieDialog(error.getMessage());
                        return;
                    default:
                        break;
                }
            }

            //接收到错误并且未显示
            DialogUtil.showErrorDialog(getError(volleyError));
        }
    }

    /**
     * 显示错误
     * @param error
     */
    private static String getError(VolleyError error) {
        String errorInfo = error.getMessage();
        Throwable t = error.getCause();
        if (t != null) {
            if (t instanceof JSONException) {
                errorInfo = Utils.getString(R.string.error_message_unknown);
            } else if (t instanceof IOException
                    || t instanceof SocketException) {
                errorInfo = Utils.getString(R.string.error_message_network);
            }
        }
        if (errorInfo == null) {
            if (error instanceof TimeoutError) {
                errorInfo = Utils.getString(R.string.error_message_timeout);
            } else {
                errorInfo = Utils.getString(R.string.error_message_network_link);
            }
        }
        return errorInfo;
    }
}
