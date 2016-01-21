package com.feifan.bp.widget;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.feifan.bp.R;
import com.feifan.bp.util.LogUtil;

/**
 * Created by maning on 15/7/9.
 */
public class CustomToolbar extends Toolbar {

    private static final String TAG = CustomToolbar.class.getSimpleName();

    private CharSequence mCustomTitleText;

    public CustomToolbar(Context context) {
        super(context);
    }

    public CustomToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean hasTitleInLayout() {
        View titleView = findViewById(R.id.title);
        return (titleView != null ? true : false);
    }

    @Override
    public CharSequence getTitle() {
        if (hasTitleInLayout()) {
            return mCustomTitleText;
        }
        return super.getTitle();
    }

    @Override
    public void setTitle(int resId) {
        LogUtil.i(TAG, "setTitle() title=" + getContext().getString(resId));
        if (hasTitleInLayout()) {
            mCustomTitleText = getContext().getText(resId);
            ((TextView)findViewById(R.id.title)).setText(resId);
        } else {
            super.setTitle(resId);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        LogUtil.i(TAG, "setTitle() title=" + title);
        if (hasTitleInLayout()) {
            mCustomTitleText = title;
            ((TextView)findViewById(R.id.title)).setText(title);
        } else {
            super.setTitle(title);
        }
    }

    @Override
    public void setTitleTextAppearance(Context context, int resId) {
        if (hasTitleInLayout()) {
            ((TextView)findViewById(R.id.title)).setTextAppearance(context, resId);
        } else {
            super.setTitleTextAppearance(context, resId);
        }
    }

    @Override
    public void setTitleTextColor(int color) {
        if (hasTitleInLayout()) {
            ((TextView)findViewById(R.id.title)).setTextColor(color);
        } else {
            super.setTitleTextColor(color);
        }
    }
    
}