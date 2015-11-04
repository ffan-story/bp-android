package com.feifan.bp.browser;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.feifan.bp.R;
import com.feifan.bp.base.BaseActivity;

public class BrowserTabActivity extends BaseActivity {
    private BrowserTabPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    private TabLayout tabLayout;
    private String tabTitles[];
    private String url;
    private String arryStatus[];
    private BrowserTabItem[] arryTabItem;

    /**
     * 参数键名称－URL
     */
    public static final String EXTRA_KEY_URL = "url";
    public static final String EXTRA_KEY_TITLES = "titles";
    public static final String EXTRA_KEY_STATUS = "status";
//    public static final String EXTRA_KEY_STAFF_MANAGE = "staff";


    public static void startActivity(Context context, String url) {
        startActivity(context, url,null, null);
    }

    /**
     *
     * @param context
     * @param url
     * @param Status
     * @param titles
     */
    public static void startActivity(Context context,String url,String[] Status, String[] titles) {
        Intent i = new Intent(context, BrowserTabActivity.class);
        i.putExtra(EXTRA_KEY_URL, url);
        i.putExtra(EXTRA_KEY_STATUS, Status);
        i.putExtra(EXTRA_KEY_TITLES, titles);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_browser_tab);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        // 载入网页
        url = getIntent().getStringExtra(EXTRA_KEY_URL);
        arryStatus = getIntent().getStringArrayExtra(EXTRA_KEY_STATUS);
        tabTitles = getIntent().getStringArrayExtra(EXTRA_KEY_TITLES);

        pagerAdapter = new BrowserTabPagerAdapter(getSupportFragmentManager(),tabTitles,url,arryStatus);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(0);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        arryTabItem = new BrowserTabItem[tabLayout.getTabCount()];
        if(tabTitles.length>4){
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else{
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        }

    }

    @Override
    protected boolean isShowToolbar() {
        return true;
    }

}
