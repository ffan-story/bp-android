package com.feifan.bp.biz.financialreconciliation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.feifan.bp.R;
import com.feifan.bp.biz.financialreconciliation.model.SettleDetailModel;

import java.util.List;

/**
 * Created by konta on 2016/3/23.
 */
public class SettleDetailAdapter extends BaseAdapter {
    private Context mContext;
    private List<SettleDetailModel.SettleDetail> mSettleDetails;
    public SettleDetailAdapter(Context context, List<SettleDetailModel.SettleDetail> settleDetails) {
        mContext = context;
        mSettleDetails = settleDetails;
    }

    @Override
    public int getCount() {
        return mSettleDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return mSettleDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SettleDetailViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.financial_settle_detail_item,parent,false);
            holder = new SettleDetailViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (SettleDetailViewHolder) convertView.getTag();
        }
        String type = mSettleDetails.get(position).orderType;
        if("2".equals(type)){   //逆向订单（退款单）
            holder.mRefundTitle.setVisibility(View.VISIBLE);
        }else {     //正向订单
            holder.mRefundTitle.setVisibility(View.GONE);
        }
        holder.mOrderNo.setText(mContext.getString(R.string.financial_settle_no,mSettleDetails.get(position).mOrderNo));
        holder.mPayAmount.setText(mSettleDetails.get(position).mPayAmount);
        holder.mRefundAmount.setText(mSettleDetails.get(position).mRefundAmount);
        holder.mPlatSubsidy.setText(mSettleDetails.get(position).mPlatformSub);
        holder.mThirdSubsidy.setText(mSettleDetails.get(position).mThirdSub);
        holder.mCommission.setText(mSettleDetails.get(position).mFeeAmount);
        holder.mSettleAmount.setText(mSettleDetails.get(position).mSettleMoney);
        return convertView;
    }

    public void notifyData(List<SettleDetailModel.SettleDetail> settleDetails) {
        if(settleDetails != null && settleDetails.size() > 0){
            mSettleDetails.addAll(settleDetails);
            notifyDataSetChanged();
        }
    }

    public static class SettleDetailViewHolder{
        TextView mOrderNo, mRefundTitle, mPayAmount, mRefundAmount, mPlatSubsidy,
                mThirdSubsidy, mCommission, mSettleAmount;
        public SettleDetailViewHolder(View itemView){
            super();
            mOrderNo = (TextView) itemView.findViewById(R.id.financial_settle_detail_no);
            mRefundTitle = (TextView) itemView.findViewById(R.id.financial_settle_detail_refund_title);
            mPayAmount = (TextView) itemView.findViewById(R.id.financial_settle_detail_pay);
            mRefundAmount = (TextView) itemView.findViewById(R.id.financial_settle_detail_refund);
            mPlatSubsidy = (TextView) itemView.findViewById(R.id.financial_settle_detail_platform);
            mThirdSubsidy = (TextView) itemView.findViewById(R.id.financial_settle_detail_third);
            mCommission = (TextView) itemView.findViewById(R.id.financial_settle_detail_commission);
            mSettleAmount = (TextView) itemView.findViewById(R.id.financial_settle_detail_amount);
        }
    }
}
