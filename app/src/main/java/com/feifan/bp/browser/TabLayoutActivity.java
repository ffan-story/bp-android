package com.feifan.bp.browser;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.feifan.bp.R;
import com.feifan.bp.base.BaseActivity;

public class TabLayoutActivity extends BaseActivity implements View.OnClickListener {
    private BrowserTabPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    private TabLayout tabLayout;
    private String tabTitles[];
    private String url[];
    TabItem[] tabs;

    /**
     * 参数键名称－URL
     */
    public static final String EXTRA_KEY_URLS = "urls";
    public static final String EXTRA_KEY_TITLES = "titles";
    public static final String EXTRA_KEY_STAFF_MANAGE = "staff";
    private boolean mIsStaffManagementPage = false;

    public static void startActivity(Context context,String [] titles, String url[]) {
        startActivity(context, titles, url, false);
    }

    public static void startActivity(Context context, String [] titles,String url[], boolean staffManage) {
        Intent i = new Intent(context, BrowserActivityNew.class);
        i.putExtra(EXTRA_KEY_URLS, url);
        i.putExtra(EXTRA_KEY_TITLES, titles);
        i.putExtra(EXTRA_KEY_STAFF_MANAGE, staffManage);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_browser_tab);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        // 载入网页
        url = getIntent().getStringArrayExtra(EXTRA_KEY_URLS);
        tabTitles = getIntent().getStringArrayExtra(EXTRA_KEY_TITLES);
        mIsStaffManagementPage = getIntent().getBooleanExtra(EXTRA_KEY_STAFF_MANAGE, false);
        pagerAdapter = new BrowserTabPagerAdapter(getSupportFragmentManager(), this,tabTitles,url,mIsStaffManagementPage);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        if(tabTitles.length>4){
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }else{
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        }
        tabLayout.setupWithViewPager(viewPager);
        tabs = new TabItem[tabLayout.getTabCount()];
    }

    @Override
    protected boolean isShowToolbar() {
        return true;
    }


    @Override
    public void onClick(View v) {

    }


}
