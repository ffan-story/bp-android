package com.feifan.bp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Frank on 15/10/26.
 */
public class ObservableScrollView extends ScrollView {

    public interface OnScrollChangedListener{
        void onScrollChanged(ScrollView scrollView, int l, int t, int oldl, int oldt);
    }

    private OnScrollChangedListener mOnScrollChangedListener;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(mOnScrollChangedListener!=null){
            mOnScrollChangedListener.onScrollChanged(this,l,t,oldl,oldt);
        }
    }

    public void setOnScrollChangedListener(OnScrollChangedListener listener){
        mOnScrollChangedListener = listener;
    }
}
