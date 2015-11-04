package com.feifan.bp.browser;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;

import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.base.BaseActivity;
import com.feifan.bp.widget.FloatingActionButton;
import com.feifan.bp.widget.ObservableScrollView;
import com.feifan.bp.widget.SelectPopWindow;
import com.feifan.materialwidget.MaterialDialog;

public class BrowserTabActivity extends BaseActivity implements BrowserFragment.OnBrowserListener{
    private BrowserTabPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    private TabLayout tabLayout;
    private String tabTitles[];
    private String arryStatus[];
    private BrowserTabItem[] arryTabItem;

    private ObservableScrollView mScrollView;
    private FloatingActionButton fab;
    private SelectPopWindow mPopWindow;
    private View mShadowView;
    private int lastSelectPos = 0;

    private boolean mShowFab = false;
    private String sUrl;
    private String mUrl;
    private String mStoreId;

    /**
     * 参数键名称－URL
     */
    public static final String EXTRA_KEY_URL = "url";
    public static final String EXTRA_KEY_TITLES = "titles";
    public static final String EXTRA_KEY_STATUS = "status";
//    public static final String EXTRA_KEY_STAFF_MANAGE = "staff";

    // dialog
    private MaterialDialog mDialog;
    private transient boolean isShowDlg;

    public static void startActivity(Context context, String url) {
        startActivity(context, url, null, null);
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
        initViews();
        mShowFab = UserProfile.getInstance().getAuthRangeType().equals("merchant");
        mUrl = getIntent().getStringExtra(EXTRA_KEY_URL);
        arryStatus = getIntent().getStringArrayExtra(EXTRA_KEY_STATUS);
        tabTitles = getIntent().getStringArrayExtra(EXTRA_KEY_TITLES);
        arryTabItem = new BrowserTabItem[tabLayout.getTabCount()];
        if(tabTitles.length>4){
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else{
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        }

        if(mShowFab){
            mStoreId = UserProfile.getInstance().getStoreId(lastSelectPos);
            sUrl = addStoreId(mUrl,mStoreId);
        }else{
            sUrl = mUrl;
        }
        loadWeb(sUrl);
    }

    private void initViews() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
//        mScrollView = (ObservableScrollView) findViewById(R.id.scrollView);
        mShadowView = findViewById(R.id.shadowView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.attachToScrollView(mScrollView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMenu();
            }
        });
    }

    private void loadWeb(String url){
        pagerAdapter = new BrowserTabPagerAdapter(getSupportFragmentManager(),tabTitles,url,arryStatus);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(0);
        tabLayout.setupWithViewPager(viewPager);
        pagerAdapter.notifyDataSetChanged();
    }

    private void selectMenu() {

        mPopWindow = new SelectPopWindow(BrowserTabActivity.this, lastSelectPos);
        mShadowView.setVisibility(View.VISIBLE);
        mShadowView.startAnimation(AnimationUtils.loadAnimation(BrowserTabActivity.this, R.anim.pop_bg_show));
        mPopWindow.showAtLocation(BrowserTabActivity.this.findViewById(R.id.fab),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mShadowView.startAnimation(AnimationUtils.loadAnimation(BrowserTabActivity.this, R.anim.pop_bg_hide));
                mShadowView.setVisibility(View.INVISIBLE);
                if (lastSelectPos != mPopWindow.getSelectPos()) {
                    lastSelectPos = mPopWindow.getSelectPos();
                    mStoreId = UserProfile.getInstance().getStoreId(lastSelectPos);
                    sUrl = addStoreId(mUrl, mStoreId);
                    loadWeb(sUrl);
                }
                mPopWindow.dismiss();
            }
        });
    }

    private String addStoreId(String url,String storeId){
        String[] urls = url.split("\\?");
        return urls[0]+"?storeId="+storeId+"&"+urls[1];
    }

    @Override
    protected boolean isShowToolbar() {
        return true;
    }

    @Override
    public void OnTitleReceived(String title) {
        if((title.equals(getString(R.string.index_history_text))||
                title.equals(getString(R.string.index_order_text))||
                title.equals(getString(R.string.browser_staff_list)))&&mShowFab){
            fab.setVisibility(View.VISIBLE);
        }else{
            fab.setVisibility(View.GONE);
        }
    }

    @Override
    public void OnErrorReceived(String msg) {
        if(isShowDlg) {
            mDialog.show();
        }
    }
}
