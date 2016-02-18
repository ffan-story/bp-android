package com.feifan.bp.marketinganalysis;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.feifan.bp.Constants;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.Utils;

import java.util.List;

/**
 * Created by kontar on 2016/2/1.
 */
public class SummaryListAdapter extends BaseAdapter {
    private Context mContext;
    private List<MarketingSummaryModel.SummaryListModel> mList;
    private Bundle mArgs;
    public SummaryListAdapter(Context context, List<MarketingSummaryModel.SummaryListModel> mSummaryDataList, Bundle args) {
        mContext = context;
        mList = mSummaryDataList;
        mArgs = args;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        if(null == convertView){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.marketing_summary_item,parent,false);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (MyViewHolder)convertView.getTag();
        }

        holder.mTitle.setText(mList.get(position).mListCouponName);
        holder.mChargeCount.setText(String.format(mContext.getString(R.string.instant_colon),
                mContext.getString(R.string.anal_charge_off_count),
                mList.get(position).mListChargeOffCount));
        holder.mFeifanSubsidy.setText(String.format(mContext.getString(R.string.instant_colon),
                mContext.getString(R.string.anal_subsidy_item_money_ff),
                Utils.formatMoney(mList.get(position).mListFeifan,2)));

        if (Constants.MARKETING_HIDE_OTHER_SUBSIDY.equals(mList.get(position).mListHideOtherSubsidy)){//第三方与商户补贴同时为0 隐藏
            holder.mThirdSubsidy.setVisibility(View.GONE);
            holder.mMerchantSubsidy.setVisibility(View.GONE);
        }else{
            holder.mThirdSubsidy.setVisibility(View.VISIBLE);
            holder.mMerchantSubsidy.setVisibility(View.VISIBLE);
            holder.mMerchantSubsidy.setText(String.format(mContext.getString(R.string.instant_colon),
                    mContext.getString(R.string.anal_subsidy_item_money_vendor),
                    Utils.formatMoney(mList.get(position).mListMerchant, 2)));
            holder.mThirdSubsidy.setText(String.format(mContext.getString(R.string.instant_colon),
                    mContext.getString(R.string.anal_subsidy_item_money_third),
                    Utils.formatMoney(mList.get(position).mListThird, 2)));
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mArgs.putString(DetailFragment.EXTRA_COUPON_ID,mList.get(position).mListCouponId);
                mArgs.putString(DetailFragment.EXTRA_COUPON_NAME,mList.get(position).mListCouponName);
                mArgs.putString(DetailFragment.EXTRA_CHARGEOFF_NUM,mList.get(position).mListChargeOffCount);
                PlatformTopbarActivity.startActivity(mContext, DetailFragment.class.getName(), mList.get(position).mListCouponName,mArgs);
            }
        });

        return convertView;
    }

    public static class MyViewHolder{
        public TextView mTitle,mChargeCount,mFeifanSubsidy,mMerchantSubsidy,mThirdSubsidy;
        public MyViewHolder(View itemView){
            mTitle = (TextView)itemView.findViewById(R.id.marketing_item_title);
            mChargeCount = (TextView)itemView.findViewById(R.id.summary_charge_count);
            mFeifanSubsidy = (TextView)itemView.findViewById(R.id.summary_feifan_subsidy);
            mMerchantSubsidy = (TextView)itemView.findViewById(R.id.summary_merchant_subsidy);
            mThirdSubsidy = (TextView)itemView.findViewById(R.id.summary_third_subsidy);
        }
    }
}
