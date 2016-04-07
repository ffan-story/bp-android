package com.feifan.bp.biz.financialreconciliation.fragment;

import android.view.View;

import com.android.volley.Response;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.base.network.response.ToastErrorListener;
import com.feifan.bp.biz.financialreconciliation.ReconciliationCtrl;
import com.feifan.bp.biz.financialreconciliation.adapter.SettleDetailAdapter;
import com.feifan.bp.biz.financialreconciliation.model.SettleDetailModel;
import com.feifan.bp.util.ToastUtil;
import com.feifan.bp.widget.paginate.Paginate;

import java.util.List;

/**
 * 财务对账 -- 结算详情
 * Created by konta on 2016/3/23.
 */
public class SettleDetailFragment extends AbsFinancialFragment implements Paginate.Callbacks{

    private Paginate mPaginate;
    private boolean isLoading;
    private SettleDetailAdapter mAdapter;

    private List<SettleDetailModel.SettleDetail> settleDetails;

    @Override
    protected void preFetchData() {
        pageIndex = 0;
        mCurrentSize = 0;
        mTotalCount = 0;
    }

    @Override
    protected void fetchData(final boolean isLoadMore) {
        if (Utils.isNetworkAvailable()){
            mNoNetView.setVisibility(View.GONE);
            mSwipe.setRefreshing(true);
            ReconciliationCtrl.getSettleDetail(pageIndex + "", mSettleNo, new Response.Listener<SettleDetailModel>() {
                @Override
                public void onResponse(SettleDetailModel model) {
                    stopRefresh();
                    isLoading = false;
                    setContentEmpty(false);
                    setContentShown(true);
                    if(isAdded() && model != null){
                        fillView(model,isLoadMore);
                    }
                }
            }, new ToastErrorListener());
        }else {
            stopRefresh();
            mList.setAdapter(null);
            mNoNetView.setVisibility(View.VISIBLE);
        }
    }

    private void fillView(SettleDetailModel model, boolean isLoadMore) {
        settleDetails = model.settleDetails;
        if(settleDetails != null && settleDetails.size() > 0){  //有数据
            mNoDataView.setVisibility(View.GONE);
            mCurrentSize += settleDetails.size();
            mTotalCount = Integer.parseInt(model.mTotalCount);
        }else if(!isLoadMore){  //无数据
            mList.setAdapter(null);
            mNoDataView.setVisibility(View.VISIBLE);
            return;
        }

        if (isLoadMore) {
            if (null != mAdapter) {
                mAdapter.notifyData(settleDetails);
            }
        } else {
            mAdapter = new SettleDetailAdapter(getActivity(),settleDetails);
            mList.setAdapter(mAdapter);
            mPaginate = Paginate.with(mList, SettleDetailFragment.this)
                    .setLoadingTriggerThreshold(0)
                    .addLoadingListItem(true)
                    .build();
        }
        mPaginate.setHasMoreDataToLoad(!hasLoadedAllItems());
    }

    @Override
    public void onLoadMore() {
        isLoading = true;
        pageIndex++;
        fetchData(true);
    }

    @Override
    public void hasLoadMore() {
        ToastUtil.showToast(getActivity(), getString(R.string.anal_no_more_data));
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return mCurrentSize == mTotalCount;
    }
}
