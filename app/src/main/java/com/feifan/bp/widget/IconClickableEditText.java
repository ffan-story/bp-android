package com.feifan.bp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.feifan.bp.R;

/**
 * d
 */
public class IconClickableEditText extends EditText {

    private Rect mClickRect;                    // 可以点击的区域矩形
    private OnIconClickListener mIconClickListener;

    public IconClickableEditText(Context context) {
        super(context);
        init(null, 0);
    }

    public IconClickableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public IconClickableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        initClickRect();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                int x = (int)event.getX();
                int y = (int)event.getY();
                if(mClickRect == null) {
                    initClickRect();
                }
                if(mClickRect.contains(x, y)){
                    if(mIconClickListener != null) {
                        mIconClickListener.onRightClicked(this);
                    }
                    return true;
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setOnIconClickListener(OnIconClickListener listener) {
        mIconClickListener = listener;
    }

    private void init(AttributeSet attrs, int defStyle) {

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void initClickRect() {
        if(mClickRect == null) {
            final Rect borderRect = new Rect();
            getLocalVisibleRect(borderRect);

            //获取清除标记位置信息
            final Drawable[] drawables = getCompoundDrawables();
            if(drawables != null) {
                // 取右侧图片的位置参数
                Rect bounds = drawables[2].getBounds();
                mClickRect = new Rect(borderRect.left, borderRect.top, borderRect.right, borderRect.bottom);
                mClickRect.left = mClickRect.right - 2 * getPaddingRight() - bounds.width();
            }
        }
    }

    private void invalidateTextPaintAndMeasurements() {
//        mTextPaint.setTextSize(mExampleDimension);
//        mTextPaint.setColor(mExampleColor);
//        mTextWidth = mTextPaint.measureText(mExampleString);
//
//        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
//        mTextHeight = fontMetrics.bottom;
    }

    /**
     * 图标点击监听事件
     */
    public interface OnIconClickListener {
        void onRightClicked(View v);
    }

}
