package com.feifan.bp.marketinganalysis;

import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.feifan.bp.Constants;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.util.TimeUtil;

import java.util.List;

/**
 * 营销分析汇总页
 * Created by kontar on 2016/2/1.
 */
public class SummaryFragment extends AbsSummaryFragment {

    private static final String TAG = "SummaryFragment";
    private TextView mSummaryMerchantSubsidy,mSummaryThirdSubsidy;
    private List<MarketingSummaryModel.SummaryListModel> mSummaryDataList;
    @Override
    protected void myInitView(View view) {
        mSummaryMerchantSubsidy = (TextView) view.findViewById(R.id.marketing_merchant_subsidy);
        mSummaryThirdSubsidy = (TextView) view.findViewById(R.id.marketing_third_subsidy);
    }

    @Override
    protected void myRequestData() {
        MarketingCtrl.getSummary(mStartDate, TimeUtil.getAddOneDay(mEndDate), mType, new Response.Listener<MarketingSummaryModel>() {
            @Override
            public void onResponse(MarketingSummaryModel model) {
                if(isAdded() && null != model){
                    setContentEmpty(false);
                    setContentShown(true);
                    stopRefresh();
                    fillView(model);
                }
            }
        });
    }

    private void fillView(MarketingSummaryModel model) {

        mSummaryDataList = model.summaryList;

        if(Constants.MARKETING_HIDE_OTHER_SUBSIDY.equals(model.mSummaryHideOtherSubsidy)){    //第三方补贴与商户补贴同时为0
            mSubsidyThirdMerchant.setVisibility(View.GONE);
        }else{
            mSubsidyThirdMerchant.setVisibility(View.VISIBLE);
        }
        mSummaryChargeTotal.setText(Html.fromHtml(String.format(getString(R.string.two_row_33_99),
                model.mSummaryAllTotal, getActivity().getString(R.string.anal_charge_off_total))));
        mSummaryFeifanSubsidy.setText(Html.fromHtml(String.format(getString(R.string.two_row_red_99),
                Utils.formatMoney(model.mSummaryAllFeifan, 2), getActivity().getString(R.string.anal_subsidy_money_ff))));
        mSummaryMerchantSubsidy.setText(Html.fromHtml(String.format(getString(R.string.two_row_red_99),
                Utils.formatMoney(model.mSummaryAllMerchantr, 2), getActivity().getString(R.string.anal_subsidy_money_vendor))));
        mSummaryThirdSubsidy.setText(Html.fromHtml(String.format(getString(R.string.two_row_red_99),
                Utils.formatMoney(model.mSummaryAllThird,2), getActivity().getString(R.string.anal_subsidy_money_third))));

        if(mSummaryDataList.size() <= 0){   //无数据
            mNoDataView.setVisibility(View.VISIBLE);
        }else{
            mNoDataView.setVisibility(View.GONE);
            mSummaryList.setAdapter(new SummaryListAdapter(getActivity(),mSummaryDataList,args));
        }

    }

}
