package com.feifan.bp.biz.transactionflowing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feifan.bp.R;
import com.feifan.bp.biz.transactionflowing.model.CouponSummaryModel;
import com.feifan.bp.util.NumberUtil;

import java.util.List;

/**
 * Created by konta on 2016/1/15.
 */
public class CouponListAdapter extends RecyclerView.Adapter<CouponListAdapter.CouponAdapter> {

    List<CouponSummaryModel.CouponDetail> mCouponList;
    Context mContext;

    public CouponListAdapter(Context context, List<CouponSummaryModel.CouponDetail> couponList) {
        mContext = context;
        mCouponList = couponList;
    }

    @Override
    public CouponAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_coupon_detail_item, parent, false);
        return new CouponAdapter(view, this);
    }

    @Override
    public void onBindViewHolder(CouponAdapter holder, int position) {
        holder.mCouponCode.setText(mCouponList.get(position).CPCode);
        holder.mCouponName.setText(mCouponList.get(position).CPName);
        holder.mAwardMoney.setText(mContext.getString(R.string.money,NumberUtil.moneyFormat(mCouponList.get(position).awardMoney,2)));
        holder.mChargeOffTime.setText(mCouponList.get(position).chargeoffTime);
    }

    @Override
    public int getItemCount() {
        return mCouponList.size();
    }

    public static class CouponAdapter extends RecyclerView.ViewHolder{
        public TextView mCouponCode,mCouponName,mAwardMoney,mChargeOffTime;
        public CouponAdapter(View itemView, CouponListAdapter couponListAdapter) {
            super(itemView);
            mCouponCode = (TextView)itemView.findViewById(R.id.coupon_code);
            mCouponName = (TextView)itemView.findViewById(R.id.coupon_name);
            mAwardMoney = (TextView)itemView.findViewById(R.id.coupon_award_money);
            mChargeOffTime = (TextView)itemView.findViewById(R.id.chargeoff_time);
        }
    }

}
