package com.feifan.bp.home;

import android.os.Bundle;
import android.support.v7.internal.widget.ViewStubCompat;
import android.view.MenuItem;
import android.view.View;

import com.feifan.bp.R;
import com.feifan.bp.base.ui.ProgressFragment;

/**
 * 建设中。。。。。
 * Created by congjing on 2016/1/22.
 */
public class BuildingFrag extends ProgressFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_building);
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
