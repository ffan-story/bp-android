package com.feifan.bp.transactionflow;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;


/**
 * Created by Frank on 15/11/6.
 */
public class TransFlowTabPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private String tabTitles[];

    public TransFlowTabPagerAdapter(FragmentManager fm, List<Fragment> fragments,String[] tabTitles) {
        super(fm);
        this.tabTitles = tabTitles;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
