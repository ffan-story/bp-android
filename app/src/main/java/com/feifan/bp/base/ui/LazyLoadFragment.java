package com.feifan.bp.base.ui;


import android.support.v4.app.Fragment;

/**
 * 用于viewPager+fragment取消预加载
 * <p/>
 * Created by Frank on 16/1/18.
 */
public abstract class LazyLoadFragment extends Fragment {

    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }
    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }
    /**
     * 不可见
     */
    protected void onInvisible() {


    }
    /**
     * 延迟加载*通过fragment是否可见取消viewpager预加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();
}
