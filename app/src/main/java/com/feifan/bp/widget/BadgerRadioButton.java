package com.feifan.bp.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.RadioButton;

import com.feifan.bp.util.LogUtil;
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

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px( float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isShow) {
            int tabWidth = getWidth();
            int tabHeight = getHeight();
            float radius =  dip2px(10);
            if (count>0){
                int ry = (int)(tabHeight/5+radius/2);
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL);//充满
                paint.setColor(Color.parseColor("#FF7800"));
                canvas.drawCircle(tabWidth/2+mDrawableWidth, tabHeight/5, radius, paint);

                paint.reset();
                paint.setColor(Color.WHITE);
                paint.setStrokeWidth(dip2px(8));
                paint.setAntiAlias(true);
                //设置字体大小
                paint.setTextSize(dip2px(12));

                if (count>=100){
                    canvas.drawText("…", tabWidth/2+mDrawableWidth-(radius/2), ry, paint);
                }else if (count>=10){
                    canvas.drawText(String.valueOf(count), tabWidth/2+mDrawableWidth-((2*radius)/3), ry, paint);
                }else{
                    canvas.drawText(String.valueOf(count), tabWidth/2+mDrawableWidth-(radius/2-3), ry, paint);
                }
            }else{//count 小于等于0 显示红点
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.parseColor("#FF7800"));
                canvas.drawCircle(tabWidth/2+mDrawableWidth, tabHeight/5, dip2px(6), paint);
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