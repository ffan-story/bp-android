package com.feifan.bp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.feifan.bp.R;

/**
 * Created by xuchunlei on 15/12/2.
 */
public class BadgerTextView extends TextView {

    private Drawable mBadgerView;

    private boolean isShow = false;

    // 用于计算绘制Badger位移的尺寸
    private int mTextWidth;
    private int mTextHeight;
    private int mGap;

    public BadgerTextView(Context context) {
        this(context, null);
    }

    public BadgerTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BadgerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBadgerView = ContextCompat.getDrawable(context, R.drawable.bg_red_dot);
        mGap = context.getResources().getDimensionPixelSize(R.dimen.view_spacing_5);

        ViewTreeObserver observer = getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect bounds = new Rect();
                Paint textPaint = getPaint();
                textPaint.getTextBounds(getText().toString(), 0, getText().length(), bounds);
                mTextWidth = bounds.width();
                mTextHeight = bounds.height();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isShow) {
            mBadgerView.setBounds(0, 0, mBadgerView.getIntrinsicWidth(), mBadgerView.getIntrinsicHeight());
            canvas.save();
            canvas.translate((getWidth() + mTextWidth + mGap) / 2, getHeight() - getPaddingBottom() - mTextHeight);
            mBadgerView.draw(canvas);
            canvas.restore();
        }
    }

    /**
     * 显示角标
     */
    public void showBadger() {
        isShow = true;
        invalidate();
    }

    /**
     * 隐藏角标
     */
    public void hideBadger() {
        isShow = false;
        invalidate();
    }
}
