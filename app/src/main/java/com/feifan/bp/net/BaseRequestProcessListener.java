package com.feifan.bp.net;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;

import com.android.volley.VolleyError;
import com.feifan.bp.LogUtil;
import com.feifan.bp.R;
import com.feifan.bp.Utils;

/**
 * Created by maning on 15/7/28.
 */
public abstract class BaseRequestProcessListener<T> implements BaseRequest.OnRequestProcessListener<T> {

    private static final String TAG = "BaseRequestProcessListener";
    private Context mAppContext;
    private Dialog mProgressDialog;
    private boolean mIsShowProgress;
    private boolean mIsProgressCancelable;

    public BaseRequestProcessListener(Context context) {
        this(context, true, false);
    }

    public BaseRequestProcessListener(Context context, boolean progressCancelable) {
        this(context, true, progressCancelable);
    }

    public BaseRequestProcessListener(Context context, boolean showProgress, boolean progressCancelable) {
        mAppContext = context;
        mIsShowProgress = showProgress;
        mIsProgressCancelable = progressCancelable;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (!Utils.isNetworkAvailable()) {     // 网络不可用
            Utils.showShortToast(R.string.error_message_text_offline, Gravity.CENTER);
        } else {                               // 其他原因
            String msg = error.getMessage();
            if (!TextUtils.isEmpty(msg) && msg.trim().length() > 0) {
                Utils.showShortToast(msg, Gravity.CENTER);
            } else {
                Utils.showShortToast(R.string.error_message_text_offline, Gravity.CENTER);
            }
        }
    }

    @Override
    public void onStart() {
        LogUtil.i(TAG, "onStart()");
        if (mIsShowProgress) {
            showProgressBar();
        }
    }

    @Override
    public void onFinish() {
        LogUtil.i(TAG, "onFinish()");
        if (mIsShowProgress) {
            hideProgressBar();
        }
    }

    private void showProgressBar() {
        if (mProgressDialog != null) {
            return;
        }
        mProgressDialog = new Dialog(mAppContext, R.style.LoadingDialog);
        mProgressDialog.setContentView(R.layout.progress_bar_layout);
        mProgressDialog.setCancelable(mIsProgressCancelable);
        mProgressDialog.show();
    }

    private void hideProgressBar() {
        if (mProgressDialog == null) {
            return;
        }
        mProgressDialog.dismiss();
        mProgressDialog = null;
    }
}
