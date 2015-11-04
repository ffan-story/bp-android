package com.feifan.bp.browser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;

import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.PlatformApplication;
import com.feifan.bp.PlatformState;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.base.BaseActivity;
import com.feifan.bp.widget.FloatingActionButton;
import com.feifan.bp.widget.ObservableScrollView;
import com.feifan.bp.widget.SelectPopWindow;

import java.util.List;

public class BrowserActivity extends BaseActivity implements OnFragmentInteractionListener,BrowserFragment.OnTitleReceiveListener {
    /**
     * 参数键名称－URL
     */
    public static final String EXTRA_KEY_URL = "url";
    public static final String EXTRA_KEY_STAFF_MANAGE = "staff";
    private boolean mIsStaffManagementPage = false;

    private ObservableScrollView mScrollView;
    private FloatingActionButton fab;
    private SelectPopWindow mPopWindow;
    private View mShadowView;
    private int lastSelectPos = 0;

    private boolean mShowFab = false;
    private String sUrl;
    private String mUrl;
    private String mStoreId;

    public static void startActivity(Context context, String url) {
        Intent i = new Intent(context, BrowserActivity.class);
        i.putExtra(EXTRA_KEY_URL, url);
        context.startActivity(i);
    }

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, BrowserActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_fragment);
        initViews();
        mShowFab = UserProfile.getInstance().getAuthRangeType().equals("merchant");
        mUrl = getIntent().getStringExtra(EXTRA_KEY_URL);
        mIsStaffManagementPage = getIntent().getBooleanExtra(EXTRA_KEY_STAFF_MANAGE, false);
        if(mShowFab){
            mStoreId = UserProfile.getInstance().getStoreId(lastSelectPos);
            sUrl = mUrl + "&storeId=" + mStoreId;
        }else{
            sUrl = mUrl;
        }
        loadWeb(sUrl);
    }

    private void initViews() {
        mScrollView = (ObservableScrollView) findViewById(R.id.scrollView);
        mShadowView = findViewById(R.id.shadowView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToScrollView(mScrollView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMenu();
            }
        });
    }


    /**
     * 载入网页
     */
    private void loadWeb(String url) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.browser_fragment, BrowserFragment.newInstance(url));
        transaction.commitAllowingStateLoss();
    }


    /**
     * 门店选择
     */
    private void selectMenu() {

        mPopWindow = new SelectPopWindow(BrowserActivity.this, lastSelectPos);
        mShadowView.setVisibility(View.VISIBLE);
        mShadowView.startAnimation(AnimationUtils.loadAnimation(BrowserActivity.this, R.anim.pop_bg_show));
        mPopWindow.showAtLocation(BrowserActivity.this.findViewById(R.id.fab),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mShadowView.startAnimation(AnimationUtils.loadAnimation(BrowserActivity.this, R.anim.pop_bg_hide));
                mShadowView.setVisibility(View.INVISIBLE);
                if (lastSelectPos != mPopWindow.getSelectPos()) {
                    lastSelectPos = mPopWindow.getSelectPos();
                    mStoreId = UserProfile.getInstance().getStoreId(lastSelectPos);
                    sUrl = mUrl + "&storeId=" + mStoreId;
                    loadWeb(sUrl);
                }
                mPopWindow.dismiss();
            }
        });
    }

    @Override
    protected boolean isShowToolbar() {
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    @Override
    public void onFragmentInteraction(Bundle args) {

    }

    @Override
    public void OnTitleReceiveListener(String title) {
        if(title.equals(getString(R.string.index_report_text))){
            fab.setVisibility(View.VISIBLE);
        }else{
            fab.setVisibility(View.GONE);
        }
    }
}
