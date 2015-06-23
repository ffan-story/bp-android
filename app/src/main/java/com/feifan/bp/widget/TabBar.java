package com.feifan.bp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.feifan.bp.Constants;
import com.feifan.bp.R;

/**
 * TODO: document your custom view class.
 */
public class TabBar extends RadioGroup {

//
//    private TextPaint mTextPaint;
//    private float mTextWidth;
//    private float mTextHeight;

    public TabBar(Context context) {
        super(context);
        init(null, 0);
    }

    public TabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TabBar, defStyle, 0);

        String[] titles = null;
        if(a.hasValue(R.styleable.TabBar_titles)) {
            final int titlesId = a.getResourceId(R.styleable.TabBar_titles, Constants.NO_INTEGER);
            if(titlesId != Constants.NO_INTEGER) {
                titles = getContext().getResources().getStringArray(titlesId);
            }
        }

        TypedArray icons = null;
        if(a.hasValue(R.styleable.TabBar_icons)) {
            final int iconsId = a.getResourceId(R.styleable.TabBar_icons, Constants.NO_INTEGER);
            if(iconsId != Constants.NO_INTEGER) {
                icons = getResources().obtainTypedArray(iconsId);
            }
        }

        if(titles != null) {
            RadioGroup.LayoutParams param = new RadioGroup.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT, 1.0f);
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for(int i = 0;i < titles.length;i++) {
               RadioButton child = (RadioButton)inflater.inflate(R.layout.home_tab_item, null);
               child.setText(titles[i]);
               if(icons != null) {
                   child.setCompoundDrawablesWithIntrinsicBounds(
                           null, ContextCompat.getDrawable(getContext(), icons.getResourceId(i, Constants.NO_INTEGER)), null, null);
               }
               child.setLayoutParams(param);
                child.setId(i);
               addView(child, i);
           }
        }

//        mExampleString = a.getString(
//                R.styleable.TabBar_exampleString);
//        mExampleColor = a.getColor(
//                R.styleable.TabBar_exampleColor,
//                mExampleColor);
//        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
//        // values that should fall on pixel boundaries.
//        mExampleDimension = a.getDimension(
//                R.styleable.TabBar_exampleDimension,
//                mExampleDimension);
//
//        if (a.hasValue(R.styleable.TabBar_exampleDrawable)) {
//            mExampleDrawable = a.getDrawable(
//                    R.styleable.TabBar_exampleDrawable);
//            mExampleDrawable.setCallback(this);
//        }

        a.recycle();

        // Set up a default TextPaint object
//        mTextPaint = new TextPaint();
//        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
//        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
//        mTextPaint.setTextSize(mExampleDimension);
//        mTextPaint.setColor(mExampleColor);
//        mTextWidth = mTextPaint.measureText(mExampleString);
//
//        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
//        mTextHeight = fontMetrics.bottom;
    }

}
