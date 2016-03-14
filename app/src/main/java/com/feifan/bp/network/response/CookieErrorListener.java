package com.feifan.bp.network.response;

import com.android.volley.Response.ErrorListener;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.network.JsonRequest;
import com.feifan.bp.network.JsonRequest.StatusError;
import com.feifan.bp.util.DialogUtil;
import com.feifan.bp.util.LogUtil;

import org.json.JSONException;

import java.io.IOException;
import java.net.SocketException;

/**
 * Created by xuchunlei on 16/3/2.
 */
public abstract class CookieErrorListener implements ErrorListener {

    private static final String TAG = "CookieErrorListener";

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        // 预处理
        preDisposeError();

        // 处理状态错误
        if (volleyError instanceof StatusError) {
            StatusError error = (StatusError) volleyError;
            switch (error.getStatus()) {
                case JsonRequest.StatusError.STATUS_COOKIE_EXPIRE:      // Cookie验证失败
                    // 系统提示框
                    DialogUtil.showCookieDialog(error.getMessage());
                    return;
                default:
                    break;
            }
        }

        // 处理其它的错误
        disposeError(getErrorMessage(volleyError));

        // 后处理
        postDisposeError();
    }

    /**
     * 错误预处理，在处理错误前调用
     */
    protected void preDisposeError() {

    }

    /**
     * 错误后处理，在处理错误结束后调用
     */
    protected void postDisposeError() {

    }

    /**
     * 处理Volley错误
     * @param error
     */
    protected abstract void disposeError(String error);

    /**
     * 显示错误
     * @param error
     */
    private String getErrorMessage(VolleyError error) {
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
        LogUtil.i(TAG, "we got an error:" + errorInfo);
        return errorInfo;
    }
}
