package com.feifan.bp.base;

import android.app.Dialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.feifan.bp.R;
import com.feifan.bp.util.DialogUtil;
import com.feifan.material.MaterialDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * 基类活动
 * <pre>
 *     项目中所有活动的父类
 * </pre>
 * Created by xuchunlei on 15/11/9.
 */
public abstract class PlatformBaseActivity extends AppCompatActivity {
    private MaterialDialog mErrorDlg;

    // 空视图界面
    private EmptyViewImpl mEmptyViewImpl;

    public static final int DIALOG_ID_PROGRESS_BAR = 1;
    public static final String KEY_PROGRESS_BAR_CANCELABLE = "cancelable";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //add by tianjun 2015.11.26
        mEmptyViewImpl = new EmptyViewImpl(this,
                getEmptyViewContainerLayoutId(),
                getEmptyViewLayoutResourceId(), getEmptyViewPlaceHolderType(),
                getEmptyViewAlignment());
        mEmptyViewImpl.setupEmptyView();
    }

    protected void showProgressBar(boolean cancelable) {
        if (this.isFinishing()) {
            return;
        }
        Bundle b = new Bundle();
        b.putBoolean(KEY_PROGRESS_BAR_CANCELABLE, cancelable);
        showDialog(DIALOG_ID_PROGRESS_BAR, b);

    }

    protected void hideProgressBar() {
        if (this.isFinishing()) {
            return;
        }
        removeDialog(DIALOG_ID_PROGRESS_BAR);
    }

    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        boolean cancelable = args.getBoolean(KEY_PROGRESS_BAR_CANCELABLE);
        switch (id) {
            case DIALOG_ID_PROGRESS_BAR:
                Dialog dialog = new Dialog(this, R.style.LoadingDialog);
                dialog.setContentView(R.layout.progress_bar_layout);
                dialog.setCancelable(cancelable);
                return dialog;
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
        boolean cancelable = args.getBoolean(KEY_PROGRESS_BAR_CANCELABLE);
        dialog.setCancelable(cancelable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // 清理系统对话框
        DialogUtil.closeCookieDialog();
        DialogUtil.closeErrorDialog();

        MobclickAgent.onPause(this);
        if (isFinishing()) {
            removeDialog(DIALOG_ID_PROGRESS_BAR);
        }
    }


    //add by tianjun 2015.11.30
    protected IEmptyView.EmptyViewPlaceHolderType getEmptyViewPlaceHolderType() {
        return IEmptyView.EmptyViewPlaceHolderType.PlaceHolderTypeInsertToView;
    }

    protected IEmptyView.EmptyViewAlignment getEmptyViewAlignment() {
        return IEmptyView.EmptyViewAlignment.AlignmentCenter;
    }

    protected int getEmptyViewLayoutResourceId() {
        return R.layout.layout_empty_view;
    }

    protected int getEmptyViewContainerLayoutId() {
        return getContentContainerId();
    }

    public ViewGroup getEmptyView() {
        return mEmptyViewImpl.getEmptyView();
    }

    public void showEmptyView() {
        mEmptyViewImpl.showEmptyView();
    }

    public void hideEmptyView() {
        mEmptyViewImpl.hideEmptyView();
    }

    public int getContentContainerId(){
        return 0;
    }

    public void retryRequestNetwork(){
}
    //end.
}
