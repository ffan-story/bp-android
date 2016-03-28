package com.feifan.bp.base.network.response;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;

import com.feifan.bp.PlatformState;
import com.feifan.bp.R;
import com.feifan.bp.Utils;

/**
 * 提供吐司形式的错误监听者
 * Created by xuchunlei on 16/3/2.
 */
public class ToastErrorListener extends CookieErrorListener {

    @Override
    protected void disposeError(String error) {
        if (!Utils.isNetworkAvailable()) {     // 网络不可用
            Utils.showShortToastSafely(R.string.error_message_text_offline);
        } else {                               // 其他原因

            if (!TextUtils.isEmpty(error) && error.trim().length() > 0) {
                if (!Utils.isChineseChar(error)) {
                    error = Utils.getString(R.string.error_message_text_network_data_fail);
                }
                Utils.showShortToastSafely(error);
            } else {
                Utils.showShortToastSafely(R.string.error_message_text_offline);
            }
        }
    }
}
