package com.feifan.bp.browser;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.PopupWindow;
import com.feifan.bp.Constants;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.base.BaseActivity;
import com.feifan.bp.widget.FloatingActionButton;
import com.feifan.bp.widget.SelectPopWindow;
import com.feifan.material.MaterialDialog;

public class BrowserTabActivity extends BaseActivity implements BrowserFragment.OnBrowserListener{
    private BrowserTabPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    private String mContextTitle ="";

    private TabLayout tabLayout;
    private String tabTitles[];
    private String arryStatus[];
    //private BrowserTabItem[] arryTabItem;

    private FloatingActionButton fab;
    private SelectPopWindow mPopWindow;
    private View mShadowView;
    private int lastSelectPos = 0;

    private boolean mShowFab = false;
    private String sUrl;
    private String mUrl;
    private String mStoreId;
    private boolean mForce;

    /**
     * 参数键名称－URL
     */
    public static final String EXTRA_KEY_URL = "url";
    public static final String EXTRA_KEY_TITLES = "titles";
    public static final String EXTRA_KEY_STATUS = "status";
    public static final String EXTRA_KEY_CONTEXT_TITLE = "contextTitle";
    public static final String EXTRA_KEY_FORCE = "force";
//    public static final String EXTRA_KEY_STAFF_MANAGE = "staff";

    // dialog
    private MaterialDialog mDialog;
    private transient boolean isShowDlg = true;

    /**
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



    /**
     * @param context
     * @param url
     * @param Status
     * @param titles
     * @param contextTitle
     */
    public static void startActivity(Context context,String url,String[] Status, String[] titles,String contextTitle) {
        Intent i = new Intent(context, BrowserTabActivity.class);
        i.putExtra(EXTRA_KEY_URL, url);
        i.putExtra(EXTRA_KEY_STATUS, Status);
        i.putExtra(EXTRA_KEY_TITLES, titles);
        i.putExtra(EXTRA_KEY_CONTEXT_TITLE, contextTitle);
        context.startActivity(i);
    }

    public static void startActivity(Context context,String url,String[] Status, String[] titles,Boolean force) {
        Intent i = new Intent(context, BrowserTabActivity.class);
        i.putExtra(EXTRA_KEY_URL, url);
        i.putExtra(EXTRA_KEY_STATUS, Status);
        i.putExtra(EXTRA_KEY_TITLES, titles);
        i.putExtra(EXTRA_KEY_FORCE, force);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_browser_tab);
        initViews();
        initDialog();
        initData();

        // FIXME 增加强制刷新界面功能
        if(mForce) {
            tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    super.onTabSelected(tab);
                    pagerAdapter.refreshViewPage();
                    pagerAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void onResume() {
        pagerAdapter.refreshViewPage();
        pagerAdapter.notifyDataSetChanged();
        super.onResume();
        refreshViewPage();
        pagerAdapter.notifyDataSetChanged();
    }

    /**
     * 加载数据
     */
    private void initData(){
        mShowFab = UserProfile.getInstance().getAuthRangeType().equals("merchant");
        mUrl = getIntent().getStringExtra(EXTRA_KEY_URL);
        arryStatus = getIntent().getStringArrayExtra(EXTRA_KEY_STATUS);
        tabTitles = getIntent().getStringArrayExtra(EXTRA_KEY_TITLES);
        mContextTitle= getIntent().getStringExtra(EXTRA_KEY_CONTEXT_TITLE);
        mForce = getIntent().getBooleanExtra(EXTRA_KEY_FORCE, false);
        //arryTabItem = new BrowserTabItem[tabLayout.getTabCount()];

        if(null!=tabTitles && tabTitles.length>4){
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
        mShadowView = findViewById(R.id.shadowView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMenu();
            }
        });
    }

    public void refreshViewPage(){
        pagerAdapter.refreshViewPage();
    }


    private void initDialog() {
        mDialog = new MaterialDialog(BrowserTabActivity.this)
                .setNegativeButton(R.string.common_close_text, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                        isShowDlg = true;
                        finish();
                    }
                });
    }

    private void loadWeb(String url){
        pagerAdapter = new BrowserTabPagerAdapter(getSupportFragmentManager(),tabTitles,url,arryStatus,mContextTitle);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
//                pagerAdapter.refreshViewPage();
                pagerAdapter.notifyDataSetChanged();
            }
        });
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
    public void OnErrorReceived(String msg, final WebView web, final String url) {

        if(isShowDlg && !isFinishing() ) {
            mDialog.setMessage(msg)
                    .setPositiveButton(R.string.common_retry_text, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                            isShowDlg = true;
                            web.loadUrl(url);
                        }
                    })
                    .show();

            isShowDlg = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == Constants.REQUEST_CODE_STAFF_EDIT){
           initData();
       }
    }
}
