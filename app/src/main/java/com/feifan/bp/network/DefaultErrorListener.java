package com.feifan.bp.network;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;

import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.feifan.bp.PlatformState;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.util.LogUtil;

/**
 * 网络请求错误Listener默认实现
 * <pre>
 *     适用于Volley网络请求框架
 * </pre>
 *
 * Created by xuchunlei on 15/10/31.
 */
public class DefaultErrorListener implements ErrorListener {

    private static final String TAG = DefaultErrorListener.class.getSimpleName();

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Context context = PlatformState.getApplicationContext();
        if (!Utils.isNetworkAvailable(context)) {     // 网络不可用
            Utils.showShortToast(context, R.string.error_message_text_offline, Gravity.CENTER);
        } else {                               // 其他原因
            String msg = volleyError.getMessage();
            if (!TextUtils.isEmpty(msg) && msg.trim().length() > 0) {
                LogUtil.i(TAG, "error msg=" + msg);
                if (!Utils.isChineseChar(msg)) {
                    msg = context.getString(R.string.error_message_text_network_data_fail);
                }
                Utils.showShortToast(context, msg, Gravity.CENTER);
            } else {
                Utils.showShortToast(context, R.string.error_message_text_offline, Gravity.CENTER);
            }
        }
    }
}
