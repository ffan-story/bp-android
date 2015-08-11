package com.feifan.bp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.feifan.bp.R;

/**
 * Created by maning on 15/8/10.
 */
public class FunctionItemView extends RelativeLayout {
    private ImageView mRedDotView;

    public FunctionItemView(Context context) {
        super(context);
    }

    public FunctionItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mRedDotView = (ImageView) findViewById(R.id.iv_red_dot);
        mRedDotView.setVisibility(View.GONE);
    }

    public void hasUnreadData(boolean hasData) {
        if (mRedDotView == null) {
            return;
        }
        if (hasData) {
            mRedDotView.setVisibility(View.VISIBLE);
        } else {
            mRedDotView.setVisibility(View.GONE);
        }
    }
}
