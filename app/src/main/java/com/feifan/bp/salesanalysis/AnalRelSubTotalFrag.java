package com.feifan.bp.salesanalysis;

import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.android.volley.Response;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.util.LogUtil;
import com.feifan.bp.util.TimeUtil;

import java.util.List;

/**
 * 红包核销汇总
 * Created by apple on 16-1-21.
 */
public class AnalRelSubTotalFrag extends AbstractAnalSubTotalPack {

    private AlysisRedSubTotalAdapter mySubTotalAdapter;
    public List<AlysisRedSubTotalListModel.RedListModel> redList;
    public String mStrStarDate,mStrEndDate;

    @Override
    public void myRequestData(String sDate, String eDate) {
        mStrStarDate = sDate;
        mStrEndDate = eDate;
        if (TimeUtil.getIntervalDays(mStrStarDate,mStrEndDate)>1){
            mRedTvQueryTime.setText(getString(R.string.query_when_time, mStrStarDate,TimeUtil.getSUBOneDay(mStrEndDate)));
        }else{
            mRedTvQueryTime.setText(getString(R.string.query_time, mStrStarDate));
        }

        AlysisCtrl.getRedTypeList(mStrStarDate, mStrEndDate, new Response.Listener<AlysisRedSubTotalListModel>() {
            @Override
            public void onResponse(AlysisRedSubTotalListModel mSubTotalModel) {
                redList = mSubTotalModel.redList;
                if (redList.size()<=0){
                    mSRL.setVisibility(View.GONE);
                    mTvNoData.setVisibility(View.VISIBLE);
                }else{
                    mSRL.setVisibility(View.VISIBLE);
                    mTvNoData.setVisibility(View.GONE);
                    mySubTotalAdapter =  new AlysisRedSubTotalAdapter(getActivity(),redList);
                    load.setAdapter(mySubTotalAdapter);
                }

                if (mTvChargeOffTotal !=null){
                    mTvChargeOffTotal.setText(Html.fromHtml(String.format(getString(R.string.two_row_33_99),
                            mSubTotalModel.mStrRedAllTotal, getActivity().getString(R.string.anal_charge_off_total))));
                    mTvSubsidyMoneyFf.setText(Html.fromHtml(String.format(getString(R.string.two_row_red_99),
                            Utils.formatMoney(mSubTotalModel.mStrRedAllFeifan,2),
                            getActivity().getString(R.string.anal_subsidy_money_ff))));

                    if (mSubTotalModel.mStride2ndRow.equals("0")){
                        mRel2Row.setVisibility(View.GONE);
                    }else{
                        mRel2Row.setVisibility(View.VISIBLE);
                        mTvSubsidyMoneyThird.setText(Html.fromHtml(String.format(getString(R.string.two_row_red_99),
                                Utils.formatMoney(mSubTotalModel.mStrRedAllThird,2),
                                getActivity().getString(R.string.anal_subsidy_money_third))));
                        mTvSubsidyMoneyVendor.setText(Html.fromHtml(String.format(getString(R.string.two_row_red_99),
                                Utils.formatMoney(mSubTotalModel.mStrRedAllVendor, 2),
                                getActivity().getString(R.string.anal_subsidy_money_vendor))));
                    }
                }
            }
        });
    }

    @Override
    public void mItemClick(int position) {
        Bundle mBundle = new Bundle();
        mBundle.putString(AnalRedDetailFrag.EXTRA_CHARGE_OFF_ID,redList.get(position).mStrCouponId);
        mBundle.putString(AnalRedDetailFrag.EXTRA_CHARGE_OFF_START_TIME,mStrStarDate);
        mBundle.putString(AnalRedDetailFrag.EXTRA_CHARGE_OFF_END_TIME,mStrEndDate);
        PlatformTopbarActivity.startActivity(getActivity(), AnalRedDetailFrag.class.getName(), getString(R.string.anal_red_charge_off_detail),mBundle);
    }
}
