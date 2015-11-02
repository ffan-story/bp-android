package com.feifan.bp.browser;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 *
 */
public class BrowserTabPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] ;

    private String url;
    private String urlStatus[];

    public BrowserTabPagerAdapter(FragmentManager fm,String[] tabTitles, String url,String[] urlStatus) {
        super(fm);
        this.urlStatus= urlStatus;
        this.tabTitles = tabTitles;
        this.url = url;
    }

    @Override
    public Fragment getItem(int position) {
        if (urlStatus!=null && urlStatus.length>0){
            return BrowserFragment.newInstance(url + urlStatus[position]);
        }else{
            return null;
        }
    }

    @Override
    public int getCount() {
        if (tabTitles!=null){
            return tabTitles.length;
        }else{
            return 1;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (tabTitles!=null){
            return tabTitles[position];
        }else{
            return "";
        }
    }
}
