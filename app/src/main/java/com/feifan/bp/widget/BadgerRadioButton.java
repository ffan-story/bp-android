package com.feifan.bp.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.RadioButton;

import com.feifan.bp.util.VersionUtil;

/**
 * 可显示角标的RadioButton
 * Created by xuchunlei on 15/11/24.
 */
public class BadgerRadioButton extends RadioButton {

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
        ViewTreeObserver observer = getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                if (VersionUtil.isHigherThanICS_MR1()) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
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
            if (count>0){
                int rfBottom = 45;
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL);//充满
                paint.setColor(Color.parseColor("#FF7800"));
                if (count <10){
                    canvas.drawCircle((getWidth() + mDrawableWidth) / 2+20, 17, 18, paint);
                }else {
                    if (count <100){
                        rfBottom = 45;
                    }else if (count >= 100){
                        rfBottom = 50;
                    }
                    RectF rectF = new RectF(0,0,rfBottom,30);// 设置个新的长方形
                    rectF.offset((getWidth() + mDrawableWidth) / 2+5, 4);
                    canvas.drawRoundRect(rectF, 16, 16, paint);//第二个参数是x半径，第三个参数是y半径
                }

                paint.reset();
                paint.setColor(Color.WHITE);
                paint.setStrokeWidth(8);
                paint.setAntiAlias(true);
                //设置字体大小
                paint.setTextSize(20);
                if (count>=1000){
                    canvas.drawText("……", (getWidth() + mDrawableWidth) / 2+15, 26, paint);
                }else{
                    canvas.drawText(String.valueOf(count), (getWidth() + mDrawableWidth) / 2+15, 25, paint);
                }
            }else{//count 小于等于0 显示红点
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.parseColor("#FF7800"));
                canvas.drawCircle((getWidth() + mDrawableWidth) / 2 + 20, 10, 8, paint);
            }
        }
    }

    private int count = 0;

    /**
     * 显示角标
     * @param count 数量
     */
    public void showBadger(int count) {
        this.count = count;
        isShow = true;
        invalidate();
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