package com.feifan.bp.transactionflow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.feifan.bp.R;
import com.feifan.bp.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frank on 15/11/6.
 */
public class TransFlowTabActivity extends BaseActivity{

    private static final String TAG = TransFlowTabActivity.class.getSimpleName();

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TransFlowTabPagerAdapter mAdapter;

    public static void startActivity(Context context) {
        Intent i = new Intent(context, TransFlowTabActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_flow);
        initViews();
    }

    @Override
    protected void showProgressBar(boolean cancelable) {
        super.showProgressBar(cancelable);
    }

    @Override
    protected void hideProgressBar() {
        super.hideProgressBar();
    }

    private void initViews(){

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new InstantBuyFragment());
        fragments.add(new CouponsFragment());

        mAdapter = new TransFlowTabPagerAdapter(getSupportFragmentManager(),
                fragments,new String[]{getString(R.string.falsh_buy),getString(R.string.coupons)});
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(0);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected boolean isShowToolbar() {
        return true;
    }
}
