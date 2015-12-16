package com.feifan.bp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.RadioButton;

import com.feifan.bp.BuildConfig;
import com.feifan.bp.R;
import com.feifan.bp.util.VersionUtil;

/**
 * 可显示角标的RadioButton
 * Created by xuchunlei on 15/11/24.
 */
public class BadgerRadioButton extends RadioButton {

    private Drawable mBadgerView;

    private int mDrawableWidth;

    private boolean isShow = false;

    public BadgerRadioButton(Context context) {
        this(context, null);
    }

    public BadgerRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BadgerRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBadgerView = ContextCompat.getDrawable(context, R.drawable.bg_red_dot);

        ViewTreeObserver observer = getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (VersionUtil.isHigherThanICS_MR1()){
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }else{
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                Drawable[] drawables = getCompoundDrawables();
                if (drawables[1] != null) {
                    mDrawableWidth = drawables[1].getIntrinsicWidth();
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isShow) {
            mBadgerView.setBounds(0, 0, mBadgerView.getIntrinsicWidth(), mBadgerView.getIntrinsicHeight());
            canvas.save();
            canvas.translate((getWidth() + mDrawableWidth) / 2, 0);
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
