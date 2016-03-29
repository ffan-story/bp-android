package com.feifan.bp.biz.financialreconciliation.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.widget.ViewStubCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ui.ProgressFragment;

/**
 * 财务对账 抽象类
 * Created by konta on 2016/3/23.
 */
public abstract class AbsFinancialFragment extends ProgressFragment implements SwipeRefreshLayout.OnRefreshListener {
    private TextView mQueryTime;
    protected ListView mList;
    protected SwipeRefreshLayout mSwipe;
    protected RelativeLayout mNoDataView, mNoNetView;
    protected Bundle mArgs;
    public static final String RENO = "reNo";   //对账单号
    public static final String SETTLENO = "settleNo";   //对账单号
    protected String mReNo,mSettleNo;
    protected int pageIndex, mTotalCount, mCurrentSize;

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_financial_reconciliation_detail);
        View view = stub.inflate();
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.view_no_data_net, null, false);

        mQueryTime = (TextView) view.findViewById(R.id.query_time);
        initData();
        mSwipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        mSwipe.setColorSchemeResources(R.color.accent);
        mSwipe.setOnRefreshListener(this);
        mList = (ListView) view.findViewById(R.id.list);
        mList.addHeaderView(header);
        mList.setAdapter(null);
        mNoDataView = (RelativeLayout) header.findViewById(R.id.no_data);
        mNoNetView = (RelativeLayout) header.findViewById(R.id.no_net);
        mNoNetView.setVisibility(View.GONE);
        mNoDataView.setVisibility(View.GONE);

        header.findViewById(R.id.reload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData();
            }
        });
        return view;
    }

    private void initData() {
        if(isAdded() && getActivity().getIntent() != null){
            mArgs = getActivity().getIntent().getBundleExtra(PlatformTopbarActivity.EXTRA_ARGS);
            if(mArgs != null){
                mReNo = mArgs.getString(RENO);
                if(!TextUtils.isEmpty(mArgs.getString(FinancialSummaryFragment.QUERY_TIME))){
                    mQueryTime.setText(mArgs.getString(FinancialSummaryFragment.QUERY_TIME));
                }
                mSettleNo = mArgs.getString(SETTLENO);
            }
        }

    }

    @Override
    protected void requestData() {
        if(Utils.isNetworkAvailable()){
            mSwipe.setRefreshing(true);
            mNoNetView.setVisibility(View.GONE);
            preFetchData();
            fetchData(false);
        }else {
            stopRefresh();
            setContentEmpty(false);
            setContentShown(true);
            mList.setAdapter(null);
            mNoNetView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 子类实现做请求数据前的操作
     */
    protected abstract void preFetchData();

    /**
     * 子类实现 请求数据
     * @param isLoadMore
     */
    protected abstract void fetchData(boolean isLoadMore);

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
