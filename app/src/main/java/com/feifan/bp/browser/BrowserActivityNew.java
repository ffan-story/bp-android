package com.feifan.bp.browser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.R;
import com.feifan.bp.base.BaseActivity;

public class BrowserActivityNew extends BaseActivity implements OnFragmentInteractionListener {
    /**
     * 参数键名称－URL
     */
    public static final String EXTRA_KEY_URL = "url";
    public static final String EXTRA_KEY_STAFF_MANAGE = "staff";
    private boolean mIsStaffManagementPage = false;

//    public static void startActivity(Context context, String url) {
//        startActivity(context, url, false);
//    }
//
    public static void startActivity(Context context, String url) {
        Intent i = new Intent(context, BrowserActivityNew.class);
        i.putExtra(EXTRA_KEY_URL, url);
//        i.putExtra(EXTRA_KEY_STAFF_MANAGE, staffManage);
        context.startActivity(i);
    }

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, BrowserActivityNew.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_fragment);
        // 载入网页
        String url = getIntent().getStringExtra(EXTRA_KEY_URL);
        mIsStaffManagementPage = getIntent().getBooleanExtra(EXTRA_KEY_STAFF_MANAGE, false);
        //初始化数据
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.browser_fragment, BrowserFragment.newInstance(url, mIsStaffManagementPage));
        transaction.commitAllowingStateLoss();
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
}
