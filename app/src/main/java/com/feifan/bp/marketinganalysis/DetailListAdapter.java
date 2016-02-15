package com.feifan.bp.marketinganalysis;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feifan.bp.R;
import com.feifan.bp.Utils;

/**
 * Created by konta on 2016/2/3.
 */
public class DetailListAdapter extends RecyclerView.Adapter<DetailListAdapter.MyViewHolder> {

    Context mContext;
    public DetailListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.marketing_detail_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mCouponCode.setText(String.format(mContext.getString(R.string.instant_colon),mContext.getString(R.string.anal_red_coupon_code),"0000000" + position));
        holder.mChargeStatus.setText("已核销");
        holder.mSubsidyFeifan.setText("飞凡补贴:" + Utils.formatMoney("88",2));

        if(true){//第三方与商户补贴 开关
            holder.mSubsidyThird.setVisibility(View.GONE);
            holder.mSubsidyMerchant.setVisibility(View.GONE);
        }else {
            holder.mSubsidyThird.setVisibility(View.VISIBLE);
            holder.mSubsidyMerchant.setVisibility(View.VISIBLE);
            holder.mSubsidyThird.setText("第三方补贴:" + Utils.formatMoney("66",2));
            holder.mSubsidyMerchant.setText("商户补贴:" + Utils.formatMoney("55",2));
        }
        holder.mGetTime.setText("2016-02-01 10:00:00");
        holder.mUsefulToTime.setText("2016-02-01 10:00:00");
        holder.mChargeOffTime.setText("2016-02-01 10:00:00");
    }

    @Override
    public int getItemCount() {
        return 10;
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