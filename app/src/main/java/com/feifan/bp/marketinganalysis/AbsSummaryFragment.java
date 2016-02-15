package com.feifan.bp.marketinganalysis;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.widget.ViewStubCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.util.TimeUtil;

/**
 *
 * Created by kontar on 2016/2/14.
 */
public abstract class AbsSummaryFragment extends ProgressFragment implements SwipeRefreshLayout.OnRefreshListener {

    protected TextView mQueryTime,mSummaryChargeTotal,
            mSummaryFeifanSubsidy,mSummaryTitle;
    protected SwipeRefreshLayout mSwipe;
    protected ListView mSummaryList;
    protected String mStartDate, mEndDate;
    protected Bundle args;
    protected LinearLayout mSummaryContainer;
    protected RelativeLayout mSubsidyThirdMerchant;
    protected RelativeLayout mNoNetView,mNoDataView;//    无网络/数据 视图

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_marketing_summary);
        View view = stub.inflate();
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_marketing_summary_header,null);
        initView(view, header);
        initData();
        return view;
    }

    private void initView(View view, View header) {
        mQueryTime  = (TextView) view.findViewById(R.id.marketing_summary_query_time);
        mSwipe = (SwipeRefreshLayout) view.findViewById(R.id.marketing_summary_swipe);
        mSwipe.setColorSchemeResources(R.color.accent);
        mSwipe.setOnRefreshListener(this);
        mSummaryList = (ListView) view.findViewById(R.id.marketing_summary_list);
        mSummaryList.addHeaderView(header);
        mSummaryList.setAdapter(null);

        mSummaryChargeTotal = (TextView) header.findViewById(R.id.marketing_charge_off_total);
        mSummaryFeifanSubsidy = (TextView) header.findViewById(R.id.marketing_feifan_subsidy);
        mSummaryTitle = (TextView) header.findViewById(R.id.marketing_summary_title);

        mSummaryContainer = (LinearLayout) header.findViewById(R.id.summary_container);
        mSummaryContainer.setVisibility(View.VISIBLE);
        mSubsidyThirdMerchant = (RelativeLayout) header.findViewById(R.id.subsidy_third_merchant_container);
        mNoNetView = (RelativeLayout) header.findViewById(R.id.summary_no_net);
        mNoDataView = (RelativeLayout) header.findViewById(R.id.summary_no_data);
        mNoNetView.setVisibility(View.GONE);
        mNoDataView.setVisibility(View.GONE);

        header.findViewById(R.id.reload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData();
            }
        });

       myInitView(header);
    }

    private void initData() {
        args = getActivity().getIntent().getBundleExtra(PlatformTopbarActivity.EXTRA_ARGS);
        mStartDate = args.getString(MarketingHomeFragment.STARTDATE);
        mEndDate = args.getString(MarketingHomeFragment.ENDTDATE);
        if (TimeUtil.getIntervalDays(mStartDate, mEndDate) >= 1){
            mQueryTime.setText(getString(R.string.query_when_time, mStartDate, mEndDate));
        }else{
            mQueryTime.setText(getString(R.string.query_time, mStartDate));
        }

    }

    protected abstract void myInitView(View header);
    protected abstract void myRequestData();

    @Override
    protected void requestData() {
        if(Utils.isNetworkAvailable(getActivity())){
            mSwipe.setRefreshing(true);
            mNoNetView.setVisibility(View.GONE);
            myRequestData();
        }else{
            stopRefresh();
            setContentEmpty(false);
            setContentShown(true);
            mSummaryContainer.setVisibility(View.GONE);
            mNoDataView.setVisibility(View.GONE);
            mNoNetView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefresh() {
        requestData();
    }

    protected void stopRefresh() {
        if(mSwipe.isRefreshing()){
            mSwipe.setRefreshing(false);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
