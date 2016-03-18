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

import com.feifan.bp.R;
import com.feifan.bp.util.VersionUtil;

/**
 * 可显示角标的RadioButton
 * Created by xuchunlei on 15/11/24.
 */
public class BadgerRadioButton extends RadioButton {

    private int mDrawableWidth;

    private boolean isShow = false;

    private int count = 0;

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
            float radius = dip2px(10);
            int textSize = dip2px(12);
            int rx =(int)((getWidth()+mDrawableWidth+radius)/2+12);
            int ry = (int)(getHeight()/5+radius/2);
            int color = getResources().getColor(R.color.orange_dot);
            if (count>0){
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL);//充满
                paint.setColor(color);
                canvas.drawCircle(rx, getHeight() / 5, radius, paint);

                paint.reset();
                paint.setColor(Color.WHITE);
                paint.setStrokeWidth(dip2px(8));
                paint.setAntiAlias(true);
                //设置字体大小
                paint.setTextSize(textSize);

                int textWidth;
                if (count>=100){
                    textWidth = getTextWidth(paint, "…");
                    canvas.drawText("…", rx-textWidth/2, ry, paint);
                }else {
                    textWidth = getTextWidth(paint, String.valueOf(count));
                    canvas.drawText(String.valueOf(count), rx-textWidth/2, ry, paint);
                }
            }else{//count 小于等于0 显示红点
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(color);
                canvas.drawCircle((getWidth()+mDrawableWidth+4)/2+8, 10, dip2px(4), paint);
            }
        }
    }

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

    /**
     * 计算文字宽度
     * @param paint
     * @param str
     * @return
     */
    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px( float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}