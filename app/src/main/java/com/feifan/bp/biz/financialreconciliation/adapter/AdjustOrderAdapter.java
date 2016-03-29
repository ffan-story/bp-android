package com.feifan.bp.biz.financialreconciliation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.feifan.bp.R;
import com.feifan.bp.biz.financialreconciliation.model.AdjustOrderModel;

import java.util.List;

/**
 * Created by konta on 2016/3/23.
 */
public class AdjustOrderAdapter extends BaseAdapter {

    private Context mContext;
    private List<AdjustOrderModel.AdjustOrder> mAdjustOrders;

    public AdjustOrderAdapter(Context context, List<AdjustOrderModel.AdjustOrder> adjustOrders){
        mContext = context;
        mAdjustOrders = adjustOrders;
    }
    @Override
    public int getCount() {
        return mAdjustOrders.size();
    }

    @Override
    public Object getItem(int position) {
        return mAdjustOrders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AdjustViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.financial_settle_adjust,parent,false);
            holder = new AdjustViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (AdjustViewHolder) convertView.getTag();
        }

        holder.mAdjustNo.setText(mAdjustOrders.get(position).mAdjustNo);
        holder.mSettleNo.setText(mAdjustOrders.get(position).mSettleNo);
        holder.mAdjustAmount.setText(mAdjustOrders.get(position).mAdjustAmount);
        holder.mAdjustExplain.setText(mAdjustOrders.get(position).mAdjustExplain);

        return convertView;
    }

    public static class AdjustViewHolder{
        public TextView mAdjustNo, mSettleNo, mAdjustAmount, mAdjustExplain;
        public AdjustViewHolder(View itemView) {
            mAdjustNo = (TextView) itemView.findViewById(R.id.financial_adjust_no);
            mSettleNo = (TextView) itemView.findViewById(R.id.financial_adjust_settle_no);
            mAdjustAmount = (TextView) itemView.findViewById(R.id.financial_adjust_amount);
            mAdjustExplain = (TextView) itemView.findViewById(R.id.financial_adjust_explain);
        }
    }

}
