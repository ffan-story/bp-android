package com.feifan.bp.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.internal.widget.ViewStubCompat;
import android.view.MenuItem;
import android.view.View;

import com.feifan.bp.R;

/**
 * Created by xuchunlei on 15/12/6.
 */
public class SimpleProgressFragment extends ProgressFragment {

    private Handler mHandler;
    private Runnable mShowContentRunnable = new Runnable() {

        @Override
        public void run() {
            setContentShown(true);
            setContentEmpty(true);
        }

    };

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_settings);
        View v = stub.inflate();
        return v;
    }

    @Override
    protected void requestData() {
        mHandler = new Handler();
        mHandler.postDelayed(mShowContentRunnable, 3000);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
