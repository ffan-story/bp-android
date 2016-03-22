package com.feifan.bp.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by maning on 15/7/9.
 */
public class BaseFragment extends Fragment implements OnDispatchTouchEventListener {

    protected Toolbar getToolbar() {
        Activity a = getActivity();
        if (a instanceof BaseActivity) {
            return ((BaseActivity) a).getToolbar();
        }
        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = getToolbar();
        if (toolbar != null && shouldSetupToolbar()) {
            setupToolbar(toolbar);
        }
    }

    /**
     * Call super.setupToolbar() first if you want to override this method.
     *
     * @param toolbar
     */
    protected void setupToolbar(Toolbar toolbar) {
        //clean toolbar here.
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle(null);
        toolbar.setNavigationIcon(null);
        toolbar.setNavigationOnClickListener(null);
    }

    protected boolean shouldSetupToolbar() {
        return true;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return false;
    }

    protected void showProgressBar(boolean cancelable) {
        if(isAdded()) {
            Activity a = getActivity();
            if (a instanceof BaseActivity) {
                ((BaseActivity) a).showProgressBar(cancelable);
            }else if(a instanceof PlatformBaseActivity){
                ((PlatformBaseActivity) a).showProgressBar(cancelable);
            }
        }
    }

    protected void hideProgressBar() {
        if(isAdded()) {
            Activity a = getActivity();
            if (a instanceof BaseActivity) {
                ((BaseActivity) a).hideProgressBar();
            }else if(a instanceof PlatformBaseActivity){
                ((PlatformBaseActivity) a).hideProgressBar();
            }
        }
    }

    //add by tianjun 2015.11.26
    public void showEmptyView() {

        if(isAdded()) {
            Activity a = getActivity();
            if (a instanceof BaseActivity) {
                ((BaseActivity) a).showEmptyView();
            } else if (a instanceof PlatformBaseActivity) {
                ((PlatformBaseActivity) a).showEmptyView();
            }
        }
    }

    public void hideEmptyView() {
        if(isAdded()) {
            Activity a = getActivity();
            if (a instanceof BaseActivity) {
                ((BaseActivity) a).hideEmptyView();
            } else if (a instanceof PlatformBaseActivity) {
                ((PlatformBaseActivity) a).hideEmptyView();
            }
        }

    }
}
