package com.feifan.bp.browser;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.feifan.bp.R;
import com.feifan.bp.base.BaseActivity;

public class BrowserTabActivity extends BaseActivity implements View.OnClickListener {
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
    public static final String EXTRA_KEY_STAFF_MANAGE = "staff";
    private boolean mIsStaffManagementPage = false;


    public static void startActivity(Context context, String url) {

        startActivity(context, url,null, null, false);
    }

    /**
     *
     * @param context
     * @param url
     * @param Status
     * @param titles
     * @param staffManage
     */
    public static void startActivity(Context context,String url,String[] Status, String[] titles, boolean staffManage) {
        Intent i = new Intent(context, BrowserTabActivity.class);
        i.putExtra(EXTRA_KEY_URL, url);
        i.putExtra(EXTRA_KEY_STATUS, Status);
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
        url = getIntent().getStringExtra(EXTRA_KEY_URL);
        arryStatus = getIntent().getStringArrayExtra(EXTRA_KEY_STATUS);
        tabTitles = getIntent().getStringArrayExtra(EXTRA_KEY_TITLES);

        mIsStaffManagementPage = getIntent().getBooleanExtra(EXTRA_KEY_STAFF_MANAGE, false);
        pagerAdapter = new BrowserTabPagerAdapter(getSupportFragmentManager(),tabTitles,url,arryStatus,mIsStaffManagementPage);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        if (tabTitles == null ){
            setTabVisibility(false);
        }else{
            tabLayout.setupWithViewPager(viewPager);
            arryTabItem = new BrowserTabItem[tabLayout.getTabCount()];
            setTabVisibility(true);
            if(tabTitles.length>4){
                tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            } else{
                tabLayout.setTabMode(TabLayout.MODE_FIXED);
            }
        }
    }

    @Override
    protected boolean isShowToolbar() {
        return true;
    }


    @Override
    public void onClick(View v) {

    }


    /**
     * 设置tab显示隐藏
     * true：隐藏
     * false：
     * @param isVisible
     */
    public void setTabVisibility(boolean isVisible){
        if(isVisible){
            tabLayout.setVisibility(View.VISIBLE);
        }else{
            tabLayout.setVisibility(View.GONE);
        }
    }
}
