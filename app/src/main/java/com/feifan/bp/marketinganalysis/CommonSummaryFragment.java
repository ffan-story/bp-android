package com.feifan.bp.marketinganalysis;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;

import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.widget.paginate.Paginate;

/**
 * 营销分析通用券汇总页
 * Created by kontar on 2016/2/3.
 */
public class CommonSummaryFragment extends AbsSummaryFragment implements Paginate.Callbacks{

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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopRefresh();
                setContentEmpty(false);
                setContentShown(true);
                fillView();
            }
        }, 500);
    }


    private void fillView() {
        if(true){
            mNoDataView.setVisibility(View.GONE);
            mSummaryContainer.setVisibility(View.VISIBLE);
        }else{
            mSummaryContainer.setVisibility(View.GONE);
            mNoDataView.setVisibility(View.VISIBLE);
            return;
        }

        mNoDataView.setVisibility(View.GONE);
        mSummaryChargeTotal.setText(Html.fromHtml(String.format(getString(R.string.two_row_33_99),
                "666", getActivity().getString(R.string.anal_charge_off_total))));
        mSummaryFeifanSubsidy.setText(Html.fromHtml(String.format(getString(R.string.two_row_red_99),
                "666", getActivity().getString(R.string.anal_award_money))));
        mSummaryList.setAdapter(new CommonDetailAdapter(getActivity()));
    }

    protected Toolbar getToolbar() {
        Activity a = getActivity();
        if (a instanceof PlatformTopbarActivity) {
            return ((PlatformTopbarActivity) a).getToolbar();
        }
        return null;
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void hasLoadMore() {

    }

    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return false;
    }
}
