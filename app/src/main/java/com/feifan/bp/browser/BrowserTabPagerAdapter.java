package com.feifan.bp.browser;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.ViewGroup;

/**
 * page adapter
 */
public class BrowserTabPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] ;
    private String url;
    private String urlStatus[];
    private int position;
    private boolean isRefresh = false;
    public BrowserTabPagerAdapter(FragmentManager fm,String[] tabTitles, String url,String[] urlStatus) {
        super(fm);
        this.urlStatus= urlStatus;
        this.tabTitles = tabTitles;
        this.url = url;
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

    public void refreshViewPage() {
        // TODO: 15-11-9
        // if(getCount()>0){
        isRefresh =true;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object)   {
        if (isRefresh) {
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
