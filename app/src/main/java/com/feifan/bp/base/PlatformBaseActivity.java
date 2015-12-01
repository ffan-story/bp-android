package com.feifan.bp.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.feifan.bp.R;
import com.umeng.analytics.MobclickAgent;


/**
 * 基类活动
 * <pre>
 *     项目中所有活动的父类
 * </pre>
 * Created by xuchunlei on 15/11/9.
 */
public abstract class PlatformBaseActivity extends AppCompatActivity {
    private EmptyViewImpl mEmptyViewImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //add by tianjun 2015.11.26
        mEmptyViewImpl = new EmptyViewImpl(this,
                getEmptyViewContainerLayoutId(),
                getEmptyViewLayoutResourceId(), getEmptyViewPlaceHolderType(),
                getEmptyViewAlignment());
        mEmptyViewImpl.setupEmptyView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    //add by tianjun 2015.11.30
    protected IEmptyView.EmptyViewPlaceHolderType getEmptyViewPlaceHolderType() {
        return IEmptyView.EmptyViewPlaceHolderType.PlaceHolderTypeInsertToView;
    }

    protected IEmptyView.EmptyViewAlignment getEmptyViewAlignment() {
        return IEmptyView.EmptyViewAlignment.AlignmentCenter;
    }

    protected int getEmptyViewLayoutResourceId() {
        return R.layout.layout_empty_view;
    }

    protected int getEmptyViewContainerLayoutId() {
        return getContentContainerId();
    }

    public ViewGroup getEmptyView() {
        return mEmptyViewImpl.getEmptyView();
    }

    public void showEmptyView() {
        mEmptyViewImpl.showEmptyView();
    }

    public void hideEmptyView() {
        mEmptyViewImpl.hideEmptyView();
    }

    protected abstract int getContentContainerId();

    protected abstract void retryRequestNetwork();
    //end.
}
