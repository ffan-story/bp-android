package com.feifan.bp.browser;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.feifan.bp.util.LogUtil;

/**
 *
 */
public class BrowserTabPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] ;
    private String url;
    private String urlStatus[];
    private int position;
    private int mChildCount = 0;
    public BrowserTabPagerAdapter(FragmentManager fm,String[] tabTitles, String url,String[] urlStatus) {
        super(fm);
        this.urlStatus= urlStatus;
        this.tabTitles = tabTitles;
        this.url = url;
        mChildCount = url.length();
    }

    @Override
    public Fragment getItem(int position) {
        this.position = position;
        if (urlStatus!=null && urlStatus.length>0 && !TextUtils.isEmpty(urlStatus[position])){
            return BrowserFragment.newInstance(url + urlStatus[position]);
        }else{
            return null;
        }
    }


    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object)   {
        if ( mChildCount > 0) {
            mChildCount --;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BrowserFragment browserFragment = (BrowserFragment)super.instantiateItem(container, position);
        browserFragment.setmUrl(url+urlStatus[position]);
        return browserFragment;
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
