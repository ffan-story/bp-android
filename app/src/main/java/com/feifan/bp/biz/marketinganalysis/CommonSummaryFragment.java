package com.feifan.bp.biz.marketinganalysis;

import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;

import com.android.volley.Response;
import com.feifan.bp.Constants;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.base.network.response.ToastErrorListener;
import com.feifan.bp.util.TimeUtil;
import com.feifan.bp.util.ToastUtil;
import com.feifan.bp.widget.paginate.Paginate;

/**
 * 营销分析通用券汇总页
 * Created by kontar on 2016/2/3.
 */
public class CommonSummaryFragment extends AbsSummaryFragment implements Paginate.Callbacks{
    public static final String TAG = "CommonSummaryFragment";
    private int mPageIndex;
    private boolean isLoading = false;
    private Paginate mPaginate;
    private CommonDetailAdapter mCommonDetailAdapter;
    private int mRequestDataSize;
    @Override
    protected void myInitView(View view) {
        mSubsidyThirdMerchant.setVisibility(View.GONE);
        getToolbar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSummaryList.setSelection(0);
            }
        });
    }

    @Override
    protected void myRequestData() {
        mSummaryContainer.setVisibility(View.VISIBLE);
        mPageIndex = 0;
        fetchData(false);
    }

    private void fetchData(final boolean isLoadMore) {
        if(!Utils.isNetworkAvailable()){
            mSummaryList.setAdapter(null);
            mNoNetView.setVisibility(View.VISIBLE);
            return;
        }
        MarketingCtrl.getCommonSummaryAndDetail(mStartDate, TimeUtil.getAddOneDay(mEndDate), mType, mPageIndex+"", new Response.Listener<CommonModel>() {
            @Override
            public void onResponse(CommonModel model) {
                if (isAdded() && null != model) {
                    setContentEmpty(false);
                    setContentShown(true);
                    stopRefresh();
                    isLoading = false;
                    fillView(model, isLoadMore);
                }
            }
        }, new ToastErrorListener());
    }

    private void fillView(CommonModel model,Boolean isLoadMore) {
        mRequestDataSize = model.mCommonDetails.size();
        mSummaryChargeTotal.setText(Html.fromHtml(String.format(getString(R.string.two_row_33_99),
                model.mVerifyNum, getActivity().getString(R.string.anal_charge_off_total))));
        mSummaryFeifanSubsidy.setText(Html.fromHtml(String.format(getString(R.string.two_row_red_99),
                Utils.formatMoney(model.mAwardAmount,2), getActivity().getString(R.string.anal_award_money))));

        if(null != model.mCommonDetails && model.mCommonDetails.size() > 0){//有数据
            mNoDataView.setVisibility(View.GONE);
        }else if(!isLoadMore){
            mSummaryList.setAdapter(null);
            mNoDataView.setVisibility(View.VISIBLE);
            return;
        }

        if(isLoadMore){
            if(null != mCommonDetailAdapter){
                mCommonDetailAdapter.notifyData(model.mCommonDetails);
            }
        }else{
            mCommonDetailAdapter = new CommonDetailAdapter(getActivity(),model.mCommonDetails);
            mSummaryList.setAdapter(mCommonDetailAdapter);
            mPaginate = Paginate.with(mSummaryList,CommonSummaryFragment.this)
                    .setLoadingTriggerThreshold(0)
                    .addLoadingListItem(true)
                    .build();
        }
        mPaginate.setHasMoreDataToLoad(!hasLoadedAllItems());

    }

    @Override
    public void onLoadMore() {
        isLoading = true;
        mPageIndex++;
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
        return mRequestDataSize != Integer.parseInt(Constants.LIST_LIMIT);
    }

    protected Toolbar getToolbar() {
        Activity a = getActivity();
        if (a instanceof PlatformTopbarActivity) {
            return ((PlatformTopbarActivity) a).getToolbar();
        }
        return null;
    }

}
