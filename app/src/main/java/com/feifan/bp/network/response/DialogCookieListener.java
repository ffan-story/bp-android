package com.feifan.bp.network.response;

import com.feifan.bp.util.DialogUtil;

/**
 * 提供错误对话框的错误监听者
 *
 * Created by xuchunlei on 16/3/2.
 */
public class DialogCookieListener extends CookieErrorListener {

    @Override
    protected void disposeError(String error) {
        //接收到错误并且未显示
        DialogUtil.showErrorDialog(error);
    }
}
