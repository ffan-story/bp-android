package com.feifan.bp.marketinganalysis;

import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.feifan.bp.R;

/**
 * 营销分析汇总页
 * Created by kontar on 2016/2/1.
 */
public class SummaryFragment extends AbsSummaryFragment {

    private TextView mSummaryMerchantSubsidy,mSummaryThirdSubsidy;

    @Override
    protected void myInitView(View view) {
        mSummaryMerchantSubsidy = (TextView) view.findViewById(R.id.marketing_merchant_subsidy);
        mSummaryThirdSubsidy = (TextView) view.findViewById(R.id.marketing_third_subsidy);
    }

    @Override
    protected void myRequestData() {
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
        mSummaryTitle.setText(getString(R.string.anal_coupons_summary_title));

        if(true){   //无数据
            mNoDataView.setVisibility(View.GONE);
            mSummaryContainer.setVisibility(View.VISIBLE);
        }else{
            mSummaryContainer.setVisibility(View.GONE);
            mNoDataView.setVisibility(View.VISIBLE);
            return;
        }

        if(true){    //第三方补贴与商户补贴同时为0
            mSubsidyThirdMerchant.setVisibility(View.GONE);
        }else{
            mSubsidyThirdMerchant.setVisibility(View.VISIBLE);
        }

        mSummaryChargeTotal.setText(Html.fromHtml(String.format(getString(R.string.two_row_33_99),
                "888", getActivity().getString(R.string.anal_charge_off_total))));
        mSummaryFeifanSubsidy.setText(Html.fromHtml(String.format(getString(R.string.two_row_red_99),
                "999", getActivity().getString(R.string.anal_subsidy_money_ff))));
        mSummaryMerchantSubsidy.setText(Html.fromHtml(String.format(getString(R.string.two_row_red_99),
                "666", getActivity().getString(R.string.anal_subsidy_money_vendor))));
        mSummaryThirdSubsidy.setText(Html.fromHtml(String.format(getString(R.string.two_row_red_99),
                "555", getActivity().getString(R.string.anal_subsidy_money_third))));
        mSummaryList.setAdapter(new SummaryListAdapter(getActivity()));

    }

}
