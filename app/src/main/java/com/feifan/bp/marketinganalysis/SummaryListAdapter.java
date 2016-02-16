package com.feifan.bp.marketinganalysis;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.Utils;

/**
 * Created by kontar on 2016/2/1.
 */
public class SummaryListAdapter extends BaseAdapter {
    Context mContext;
    public SummaryListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return position;
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

        holder.mTitle.setText("优惠券 " + position);
        holder.mChargeCount.setText(String.format(mContext.getString(R.string.instant_colon),
                mContext.getString(R.string.anal_charge_off_count),
                "666"));
        holder.mFeifanSubsidy.setText(String.format(mContext.getString(R.string.instant_colon),
                mContext.getString(R.string.anal_subsidy_item_money_ff),
                Utils.formatMoney("888",2)));
        if (!TextUtils.isEmpty("") && "".equals("0")){//第三方与商户补贴同时为0 隐藏
            holder.mThirdSubsidy.setVisibility(View.GONE);
            holder.mMerchantSubsidy.setVisibility(View.GONE);
        }else{
            holder.mThirdSubsidy.setVisibility(View.VISIBLE);
            holder.mMerchantSubsidy.setVisibility(View.VISIBLE);
            holder.mMerchantSubsidy.setText(String.format(mContext.getString(R.string.instant_colon),
                    mContext.getString(R.string.anal_subsidy_item_money_vendor),
                    Utils.formatMoney("555", 2)));
            holder.mThirdSubsidy.setText(String.format(mContext.getString(R.string.instant_colon),
                    mContext.getString(R.string.anal_subsidy_item_money_third),
                    Utils.formatMoney("333", 2)));
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //需要修改真是数据~~~~~~~~~
                Bundle mBundle = new Bundle();
                mBundle.putString(DetailFragment.EXTRA_CHARGE_OFF_END_NAME,"23342");
                mBundle.putString(DetailFragment.EXTRA_CHARGE_OFF_END_COUNT,"12");
                mBundle.putString(DetailFragment.EXTRA_CHARGE_OFF_ID,"0151216231722");
                mBundle.putString(DetailFragment.EXTRA_CHARGE_OFF_START_TIME,"2015-12-01");
                mBundle.putString(DetailFragment.EXTRA_CHARGE_OFF_END_TIME,"2015-12-31");
                PlatformTopbarActivity.startActivity(mContext, DetailFragment.class.getName(), mContext.getString(R.string.anal_red_charge_off_detail),mBundle);
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
