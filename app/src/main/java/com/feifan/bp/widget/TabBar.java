package com.feifan.bp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.feifan.bp.Constants;
import com.feifan.bp.R;

/**
 * TODO: document your custom view class.
 */
public class TabBar extends RadioGroup {

    private RadioButton mDefault;

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

        final int defaultIndex = a.getInt(R.styleable.TabBar_defaultIndex, Constants.NO_INTEGER);
        final int itemLayoutRes = a.getResourceId(R.styleable.TabBar_itemLayout, Constants.NO_INTEGER);
        if(itemLayoutRes == Constants.NO_INTEGER) {
            throw new IllegalArgumentException("set a resource reference for Tab item layout with xml attribute itemLayout");
        }

        if (titles != null) {
            RadioGroup.LayoutParams param = new RadioGroup.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT, 1.0f);
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int i = 0; i < titles.length; i++) {
                RadioButton child = (RadioButton) inflater.inflate(itemLayoutRes, null);
                child.setText(titles[i]);
                if (icons != null) {
                    child.setCompoundDrawablesWithIntrinsicBounds(
                            null, ContextCompat.getDrawable(getContext(), icons.getResourceId(i, Constants.NO_INTEGER)), null, null);
                }
                child.setLayoutParams(param);
                child.setId(i);
                if(i == defaultIndex) {    //设置默认项
                    mDefault = child;
                    child.setChecked(true);
                }
                addView(child, i);
            }
        }

        a.recycle();
    }

    public void reset() {
        if(mDefault != null) {
            mDefault.setChecked(true);
        }
    }
}
