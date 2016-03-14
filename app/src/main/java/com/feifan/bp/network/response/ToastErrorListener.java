package com.feifan.bp.network.response;

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
        final Context context = PlatformState.getApplicationContext();
        if (!Utils.isNetworkAvailable(context)) {     // 网络不可用
            Utils.showShortToast(context, R.string.error_message_text_offline, Gravity.CENTER);
        } else {                               // 其他原因

            if (!TextUtils.isEmpty(error) && error.trim().length() > 0) {
                if (!Utils.isChineseChar(error)) {
                    error = context.getString(R.string.error_message_text_network_data_fail);
                }
                Utils.showShortToast(context, error, Gravity.CENTER);
            } else {
                Utils.showShortToast(context, R.string.error_message_text_offline, Gravity.CENTER);
            }
        }
    }
}
