package com.feifan.bp.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.feifan.bp.R;
import com.feifan.bp.util.LogUtil;
import com.feifan.bp.widget.SystemAlterDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by maning on 15/7/9.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    private Toolbar mToolbar;
    public static final int DIALOG_ID_PROGRESS_BAR = 1;
    public static final String KEY_PROGRESS_BAR_CANCELABLE = "cancelable";

    private EmptyViewImpl mEmptyViewImpl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);

        //add by tianjun 2015.11.26
        mEmptyViewImpl = new EmptyViewImpl(this,
                getEmptyViewContainerLayoutId(),
                getEmptyViewLayoutResourceId(), getEmptyViewPlaceHolderType(),
                getEmptyViewAlignment());
        mEmptyViewImpl.setupEmptyView();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (isShowToolbar()) {
            initToolbar();
        }
    }

    @Override
    public final void setContentView(int layoutResID) {
        if (isShowToolbar()) {
            super.setContentView(R.layout.activity_base);
            RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.activity_base_layout);
            View view = LayoutInflater.from(this).inflate(layoutResID, rootLayout, false);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
            params.addRule(RelativeLayout.BELOW, R.id.toolbar);
            view.setLayoutParams(params);
            rootLayout.addView(view);
        } else {
            super.setContentView(layoutResID);
        }
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setupToolbar(mToolbar);
    }

    /**
     * Return the toolbar of this activity,
     * you can change toolbar's views or actions after activity is start-up.
     *
     * @return
     */
    protected Toolbar getToolbar() {
//        if(mToolbar == null) {
//            initToolbar();
//        }
        return mToolbar;
    }

    /**
     * Override this method in subclass to custom toolbar.
     * This method is called after activity is start-up.
     *
     * @param toolbar
     */
    protected void setupToolbar(Toolbar toolbar) {

    }

    /**
     * Implement by subclass and return true if you want a toolbar is on top of your activity.
     *
     * @return
     */
    protected abstract boolean isShowToolbar();

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
        LogUtil.i(TAG, "onCreateDialog() id=" + id + " cancelable=" + cancelable);
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
        LogUtil.i(TAG, "onPrepareDialog() id=" + id + " cancelable=" + cancelable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SystemAlterDialog.dismissSystemDialog();
        MobclickAgent.onPause(this);

        if (isFinishing()) {
            removeDialog(DIALOG_ID_PROGRESS_BAR);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //First, Hide soft input panel if it is showing.
        if (hideSoftInputIfShow(ev)) {
            return true;
        }
        //Second, Let fragments have chances to handle motion events.
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment f : fragments) {
                if (f instanceof OnDispatchTouchEventListener) {
                    View v = f.getView();
                    if (v == null) {
                        continue;
                    }
                    Rect r = new Rect();
                    v.getHitRect(r);
                    if (r.contains((int) ev.getX(), (int) ev.getY())) {
                        OnDispatchTouchEventListener l = ((OnDispatchTouchEventListener) f);
                        if (l.dispatchTouchEvent(ev)) {
                            return true;
                        }
                    }
                }
            }
        }

        //And finally, dispatch the event to activity system.
        return super.dispatchTouchEvent(ev);
    }

    private boolean hideSoftInputIfShow(MotionEvent ev) {
        boolean result = false;
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                result = imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return result;
    }

    //add by tianjun 2015.11.26
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
