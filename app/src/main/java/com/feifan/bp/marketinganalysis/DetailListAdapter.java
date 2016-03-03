package com.feifan.bp.marketinganalysis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.feifan.bp.Constants;
import com.feifan.bp.R;
import com.feifan.bp.Utils;

import java.util.List;

/**
 * Created by kontar on 2016/2/3.
 */
public class DetailListAdapter extends BaseAdapter {
    private List<MarketingDetailModel.DetailListModel> mList;
    Context mContext;
    public DetailListAdapter(Context context, List<MarketingDetailModel.DetailListModel> mDetailList) {
        mContext = context;
        mList = mDetailList;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        if(null == convertView){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.marketing_detail_item,null);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (MyViewHolder) convertView.getTag();
        }

        holder.mCouponCode.setText(String.format(mContext.getString(R.string.instant_colon),mContext.getString(R.string.anal_red_coupon_code),mList.get(position).mDetailCouponId + position));
        holder.mChargeStatus.setText(mList.get(position).mChargeOffStatus);
        holder.mSubsidyFeifan.setText(Utils.formatMoney(mList.get(position).mDetailFeifan,2));

        if(Constants.MARKETING_HIDE_OTHER_SUBSIDY.equals(mList.get(position).mHideOtherSubsidy)){//第三方与商户补贴 开关
            holder.mSubsidyThird.setVisibility(View.GONE);
            holder.mSubsidyMerchant.setVisibility(View.GONE);
        }else {
            holder.mSubsidyThird.setVisibility(View.VISIBLE);
            holder.mSubsidyMerchant.setVisibility(View.VISIBLE);
            holder.mSubsidyThird.setText(Utils.formatMoney(mList.get(position).mDetailThird, 2));
            holder.mSubsidyMerchant.setText(Utils.formatMoney(mList.get(position).mDetailMerchant,2));
        }
        holder.mGetTime.setText(String.format(mContext.getString(R.string.instant_colon),mContext.getString(R.string.anal_subsidy_get_time),mList.get(position).mDetailGetTime));
        holder.mUsefulToTime.setText(String.format(mContext.getString(R.string.instant_colon),mContext.getString(R.string.anal_subsidy_end_time),mList.get(position).mDetailValidTime));
        holder.mChargeOffTime.setText(String.format(mContext.getString(R.string.instant_colon),mContext.getString(R.string.anal_subsidy_use_time),mList.get(position).mChargeOffTime));
        return convertView;
    }

    public void notifyData(List<MarketingDetailModel.DetailListModel> items) {
        mList.addAll(items);
        notifyDataSetChanged();
    }

    public static class MyViewHolder {
        TextView mCouponCode,mChargeStatus,
                mSubsidyFeifan,mSubsidyThird,mSubsidyMerchant,
                mGetTime,mUsefulToTime,mChargeOffTime;
        public MyViewHolder(View itemView) {
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
