package com.feifan.bp.biz.financialreconciliation.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.biz.financialreconciliation.fragment.AbsFinancialFragment;
import com.feifan.bp.biz.financialreconciliation.fragment.SettleDetailFragment;
import com.feifan.bp.biz.financialreconciliation.model.SettleOrdersModel;

import java.util.List;

/**
 * Created by konta on 2016/3/22.
 */
public class SettleOrderAdapter extends BaseAdapter {
    private Context mContext;
    private Bundle mArgs;
    private List<SettleOrdersModel.SettleOrder> mSettleOrders;
    public SettleOrderAdapter(Context context, List<SettleOrdersModel.SettleOrder> settleOrders, Bundle args) {
        mContext = context;
        mArgs = args;
        mSettleOrders = settleOrders;
    }

    @Override
    public int getCount() {
        return mSettleOrders.size();
    }

    @Override
    public Object getItem(int position) {
        return mSettleOrders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SettleOrderViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.financial_settle_order_item,parent,false);
            holder = new SettleOrderViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (SettleOrderViewHolder) convertView.getTag();
        }
        holder.mSettleOrderNo.setText(mSettleOrders.get(position).mSettleOrderNo);
        holder.mSettleCycle.setText(mSettleOrders.get(position).mSettleCycle);
        holder.mTradeCount.setText(mSettleOrders.get(position).mSettleCount);
        holder.mPayAmount.setText(mSettleOrders.get(position).mPayAmount);
        holder.mRefundAmount.setText(mSettleOrders.get(position).mRefundAmount);
        holder.mPlatSubsidy.setText(mSettleOrders.get(position).mPlatformSub);
        holder.mThirdSubsidy.setText(mSettleOrders.get(position).mThirdSub);
        holder.mCommission.setText(mSettleOrders.get(position).mFeeAmount);
        holder.mSettleAmount.setText(mSettleOrders.get(position).mSettleMoney);
        mArgs.putString(AbsFinancialFragment.SETTLENO,mSettleOrders.get(position).mSettleOrderNo);
        holder.mSettleContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlatformTopbarActivity.startActivity(mContext,SettleDetailFragment.class.getName(),mContext.getString(R.string.financial_settle_detail),mArgs);
            }
        });
        return convertView;
    }

    public static class SettleOrderViewHolder{
        RelativeLayout mSettleContainer;
        TextView mSettleOrderNo, mSettleCycle, mTradeCount, mPayAmount,
                mRefundAmount, mPlatSubsidy, mThirdSubsidy, mCommission, mSettleAmount;
        public SettleOrderViewHolder(View itemView){
            super();
            mSettleContainer = (RelativeLayout) itemView.findViewById(R.id.financial_settle_container);
            mSettleOrderNo = (TextView) itemView.findViewById(R.id.financial_settle_orders_no);
            mSettleCycle = (TextView) itemView.findViewById(R.id.financial_settle_cycle);
            mTradeCount = (TextView) itemView.findViewById(R.id.financial_settle_count);
            mPayAmount = (TextView) itemView.findViewById(R.id.financial_settle_pay);
            mRefundAmount = (TextView) itemView.findViewById(R.id.financial_settle_refund);
            mPlatSubsidy = (TextView) itemView.findViewById(R.id.financial_settle_plat);
            mThirdSubsidy = (TextView) itemView.findViewById(R.id.financial_settle_third);
            mCommission = (TextView) itemView.findViewById(R.id.financial_settle_com);
            mSettleAmount = (TextView) itemView.findViewById(R.id.financial_settle_amount);
        }
    }
}
