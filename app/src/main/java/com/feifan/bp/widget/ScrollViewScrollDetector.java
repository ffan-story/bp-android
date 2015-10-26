package com.feifan.bp.widget;

import android.widget.ScrollView;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Frank on 15/10/26.
 */
abstract class ScrollViewScrollDetector implements ObservableScrollView.OnScrollChangedListener {

    private int mLastScrollY;
    private int mScrollThreshold;

    abstract void onScrollUp();

    abstract void onScrollDown();

    @Override
    public void onScrollChanged(ScrollView scrollView, int l, int t, int oldl, int oldt) {
        boolean isSignificantDelta = Math.abs(t - mLastScrollY) > mScrollThreshold;
        if (isSignificantDelta) {
            if (t > mLastScrollY) {
                onScrollUp();
            } else {
                onScrollDown();
            }
        }
        mLastScrollY = t;
    }

    public void setScrollThreshold(int scrollThreshold) {
        mScrollThreshold = scrollThreshold;
    }
}
