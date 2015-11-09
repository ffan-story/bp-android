package com.feifan.bp.TransactionFlow;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.feifan.bp.TransactionFlow.FlashSummaryModel.FlashSummaryDetailModel;

import java.util.ArrayList;

/**
 * Created by Frank on 15/11/7.
 */
public class FlowDetailAdapter extends FragmentPagerAdapter {

    private int pagerCount = 2;
    private ArrayList<FlashSummaryDetailModel> mTradeDataList;

    public FlowDetailAdapter(FragmentManager fm,ArrayList<FlashSummaryDetailModel> tradeDataList) {
        super(fm);
        mTradeDataList = tradeDataList;
    }


    @Override
    public Fragment getItem(int position) {
        return FlowDetailFragment.newInstance(position,mTradeDataList);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        FlowDetailFragment flowDetailFragment = (FlowDetailFragment)super.instantiateItem(container, position);
        flowDetailFragment.setmTradeDataList(mTradeDataList);
        return flowDetailFragment;
    }

    @Override
    public int getCount() {
        return pagerCount;
    }
}
