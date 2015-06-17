package com.feifan.bp.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feifan.bp.R;

/**
 * Created by maning on 15/6/16.
 */
public class TabPagerIndicator extends LinearLayout implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;

    public TabPagerIndicator(Context context) {
        super(context);
    }

    public TabPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setViewPager(ViewPager pager) {
        if (pager == null) {
            throw new IllegalArgumentException("ViewPager is null!");
        }
        if (pager.getAdapter() == null) {
            throw new IllegalArgumentException("ViewPager is not set an adapter!");
        }
        mViewPager = pager;
        PagerAdapter adapter = pager.getAdapter();

        this.removeAllViews();
        int count = pager.getAdapter().getCount();
        for (int i=0; i<count; ++i) {
            TextView  text = new TextView(getContext());
            Drawable drawable = getResources().getDrawable(R.drawable.ic_management_selector);
            text.setCompoundDrawables(null, drawable, null, null);
            text.setText(pager.getAdapter().getPageTitle(i));
            text.setTextColor(getResources().getColorStateList(R.color.main_tab_text_color_selector));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            params.weight = 1.0f;
            text.setId(i);
            text.setLayoutParams(params);
            text.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    mViewPager.setCurrentItem(id);

                }
            });
            this.addView(text);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int count = getChildCount();
        if (position >= count) {
            throw new RuntimeException("Position is larger than indicator's children number!");
        }
        for (int i=0; i<count; ++i) {
            View child = getChildAt(i);
            child.setSelected(false);
        }
        View selectedChild = getChildAt(position);
        selectedChild.setSelected(true);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
