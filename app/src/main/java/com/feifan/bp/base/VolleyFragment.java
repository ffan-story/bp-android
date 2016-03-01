package com.feifan.bp.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Response;
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

    // 错误提示框
    private MaterialDialog mErrDialog;
    // 系统提示框
    private AlertDialog mSysDialog;

    // 是否可以显示错误提示框
    private transient boolean canShowErr = true;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mErrDialog = new MaterialDialog(getActivity())
                .setNegativeButton(R.string.common_confirm, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mErrDialog.dismiss();
                        canShowErr = true;
                        getActivity().finish();
                    }
                });
        if (isAdded()) {
            mSysDialog = DialogUtil.getDialog();
        }
    }

    /**
     * 是否显示错误对话框
     * <pre>
     *     默认开启错误对话框，如果需要自己实现错误提示界面，则设置为false
     * </pre>
     * @param enable
     */
    protected void EnableErrorDialog(boolean enable) {
        canShowErr = enable;
    }

    public class DefaultErrorListener implements ErrorListener {

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            final Context context = PlatformState.getApplicationContext();
            // 处理状态错误
            if (volleyError instanceof StatusError) {
                StatusError error = (StatusError) volleyError;
                switch (error.getStatus()) {
                    case StatusError.STATUS_COOKIE_EXPIRE:      // Cookie验证失败
                        if (!mSysDialog.isShowing()) {
                            //显示对话框
                            mSysDialog.show();
                            DialogUtil.setDialogStyle(error.getMessage(), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mSysDialog.dismiss();
                                    // 清理登录数据
                                    UserProfile.getInstance().clear();
                                    PlatformState.getInstance().reset();
                                    Intent intent = LaunchActivity.buildIntent(context);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                    context.startActivity(intent);
                                }
                            });
                        }
                        return;
                }
            }

            //接收到错误并且未显示
            if (canShowErr && isAdded()) {
                showError(volleyError);
            }
        }
    }

    private void showError(VolleyError error) {
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
        mErrDialog.setMessage(errorInfo)
                .show();
        canShowErr = false;
    }
}
