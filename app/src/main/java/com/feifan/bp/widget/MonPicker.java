package com.feifan.bp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

/**
 * Created by Frank on 15/11/12.
 */
public class MonPicker extends DatePicker {

    public MonPicker(Context context) {
        super(context);
        ((ViewGroup) ((ViewGroup) this.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
    }

    public MonPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        ((ViewGroup) ((ViewGroup) this.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
    }

    public MonPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ((ViewGroup) ((ViewGroup) this.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
    }
}


