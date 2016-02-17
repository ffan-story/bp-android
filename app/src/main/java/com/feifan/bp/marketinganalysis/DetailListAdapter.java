package com.feifan.bp.marketinganalysis;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feifan.bp.Constants;
import com.feifan.bp.R;
import com.feifan.bp.Utils;

import java.util.List;

/**
 * Created by kontar on 2016/2/3.
 */
public class DetailListAdapter extends RecyclerView.Adapter<DetailListAdapter.MyViewHolder> {
    private List<MarketingDetailModel.DetailListModel> mList;
    Context mContext;
    public DetailListAdapter(Context context, List<MarketingDetailModel.DetailListModel> mDetailList) {
        mContext = context;
        mList = mDetailList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.marketing_detail_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mCouponCode.setText(String.format(mContext.getString(R.string.instant_colon),mContext.getString(R.string.anal_red_coupon_code),mList.get(position).mDetailCouponId + position));
        holder.mChargeStatus.setText(mList.get(position).mChargeOffStatus);
        holder.mSubsidyFeifan.setText(String.format(mContext.getString(R.string.instant_colon),mContext.getString(R.string.anal_subsidy_item_money_ff),Utils.formatMoney(mList.get(position).mDetailFeifan,2)));

        if(Constants.MARKETING_HIDE_OTHER_SUBSIDY.equals(mList.get(position).mHideOtherSubsidy)){//第三方与商户补贴 开关
            holder.mSubsidyThird.setVisibility(View.GONE);
            holder.mSubsidyMerchant.setVisibility(View.GONE);
        }else {
            holder.mSubsidyThird.setVisibility(View.VISIBLE);
            holder.mSubsidyMerchant.setVisibility(View.VISIBLE);
            holder.mSubsidyThird.setText(String.format(mContext.getString(R.string.instant_colon),mContext.getString(R.string.anal_subsidy_item_money_third),Utils.formatMoney(mList.get(position).mDetailThird, 2)));
            holder.mSubsidyMerchant.setText(String.format(mContext.getString(R.string.instant_colon),mContext.getString(R.string.anal_subsidy_item_money_vendor),Utils.formatMoney(mList.get(position).mDetailMerchant,2)));
        }
        holder.mGetTime.setText(String.format(mContext.getString(R.string.instant_colon),mContext.getString(R.string.anal_subsidy_get_time),mList.get(position).mDetailGetTime));
        holder.mUsefulToTime.setText(String.format(mContext.getString(R.string.instant_colon),mContext.getString(R.string.anal_subsidy_end_time),mList.get(position).mDetailValidTime));
        holder.mChargeOffTime.setText(String.format(mContext.getString(R.string.instant_colon),mContext.getString(R.string.anal_subsidy_use_time),mList.get(position).mChargeOffTime));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void notifyData(List<MarketingDetailModel.DetailListModel> items) {
        int previousDataSize = this.mList.size();
        mList.addAll(items);
        notifyItemRangeChanged(previousDataSize,items.size());
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView mCouponCode,mChargeStatus,
                mSubsidyFeifan,mSubsidyThird,mSubsidyMerchant,
                mGetTime,mUsefulToTime,mChargeOffTime;
        public MyViewHolder(View itemView) {
            super(itemView);
            mCouponCode = (TextView)itemView.findViewById(R.id.detail_coupon_code);
            mChargeStatus = (TextView)itemView.findViewById(R.id.detail_coupon_status);
            mSubsidyFeifan = (TextView)itemView.findViewById(R.id.subsidy_feifan);
            mSubsidyThird = (TextView)itemView.findViewById(R.id.subsidy_third);
            mSubsidyMerchant = (TextView)itemView.findViewById(R.id.subsidy_merchant);
            mGetTime = (TextView)itemView.findViewById(R.id.get_coupon_time);
            mUsefulToTime = (TextView)itemView.findViewById(R.id.end_useful_time);
            mChargeOffTime = (TextView)itemView.findViewById(R.id.charge_off_time);

        }
    }
}
