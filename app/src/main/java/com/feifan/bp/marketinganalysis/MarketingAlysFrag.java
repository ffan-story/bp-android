package com.feifan.bp.marketinganalysis;

import android.support.v7.internal.widget.ViewStubCompat;
import android.view.MenuItem;
import android.view.View;

import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.home.BuildingFrag;

/**
 * 营销分析 首页
 * congjing
 */
public class MarketingAlysFrag extends ProgressFragment implements View.OnClickListener {
    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_anal_main);
        View v = stub.inflate();
        v.findViewById(R.id.anal_tv_red).setOnClickListener(this);
        v.findViewById(R.id.anal_tv_remote).setOnClickListener(this);
        return v;
    }

    @Override
    protected void requestData() {
        setContentShown(true);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.anal_tv_red:
                PlatformTopbarActivity.startActivity(getContext(),AnalRelSubTotalFrag.class.getName(), getString(R.string.anal_red_pack));
                break;
            case R.id.anal_tv_remote:
                PlatformTopbarActivity.startActivity(getContext(),BuildingFrag.class.getName(), getString(R.string.anal_remote));
                break;
        }
    }
}
