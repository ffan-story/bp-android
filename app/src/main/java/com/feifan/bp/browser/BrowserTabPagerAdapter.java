package com.feifan.bp.browser;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.feifan.bp.R;
import com.feifan.bp.util.LogUtil;

/**
 * Created by huangzx on 2015/8/27.
 */
public class BrowserTabPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] ;
    private Context context;
    private int[] imageResId = {R.drawable.abc_ic_menu_moreoverflow_mtrl_alpha,
            R.drawable.abc_ic_menu_cut_mtrl_alpha,
            R.drawable.abc_ic_menu_share_mtrl_alpha};

    private boolean mIsStaffManagementPage = false;
    private String url[];

    public BrowserTabPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.context = context;
    }

    public BrowserTabPagerAdapter(FragmentManager fm,Context context ,String[] tabTitles, String[] url,boolean mIsStaffManagementPage) {
        super(fm);
        this.url= url;
        this.mIsStaffManagementPage = mIsStaffManagementPage;
        this.context = context;
        this.tabTitles  = tabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return BrowserFragment.newInstance(url[position],mIsStaffManagementPage);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
