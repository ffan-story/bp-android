package com.feifan.bp.home.commoditymanager;

import android.os.Bundle;
import android.support.v7.internal.widget.ViewStubCompat;
import android.view.MenuItem;
import android.view.View;

import com.feifan.bp.R;
import com.feifan.bp.base.ProgressFragment;

/**
 * 商品管理 — 品牌商品
 * Created by kontar on 2015/12/22.
 */
public class BrandFragment extends ProgressFragment{
    private static final String TAG = "BrandFragment";

    public static final String EXTRA_KEY_URL = "url";
    private String mUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mUrl = getArguments().getString(EXTRA_KEY_URL);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_commodity_brand);
        View view = stub.inflate();

        return view;
    }

    @Override
    protected void requestData() {
        setContentShown(true);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
