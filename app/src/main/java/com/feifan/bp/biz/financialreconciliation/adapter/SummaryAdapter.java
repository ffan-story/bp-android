package com.feifan.bp.biz.financialreconciliation.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feifan.bp.PlatformTabActivity;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.biz.financialreconciliation.fragment.AbsFinancialFragment;
import com.feifan.bp.biz.financialreconciliation.fragment.AdjustOrdersFragment;
import com.feifan.bp.biz.financialreconciliation.fragment.SettleOrdersFragment;
import com.feifan.bp.biz.financialreconciliation.model.FinancialSummaryModel;

import java.util.List;

/**
 * Created by konta on 2016/3/21.
 */
public class SummaryAdapter extends BaseAdapter {

    private Context mContext;
    private Bundle mArgs;
    private List<FinancialSummaryModel.FinancialSummary> summaryList;

    public SummaryAdapter(Context context,List<FinancialSummaryModel.FinancialSummary> list,Bundle args) {
        mContext = context;
        mArgs = args;
        summaryList = list;
    }

    @Override
    public int getCount() {
        return summaryList.size();
    }

    @Override
    public Object getItem(int position) {
        return summaryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        myViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.financial_summary_item,parent,false);
            holder = new myViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (myViewHolder) convertView.getTag();
        }
        holder.mSettleNo.setText(mContext.getString(R.string.financial_order_no,summaryList.get(position).mReNo));
        holder.mSettleSubject.setText(mContext.getString(R.string.financial_settle_sub,summaryList.get(position).mSettleSub));
        holder.mSettleCount.setText(summaryList.get(position).mSettleCount);
        holder.mSettleAmount.setText(summaryList.get(position).mSettleAmount);
        holder.mAdjustAmount.setText(summaryList.get(position).mAdjustAmount);
        holder.relativeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle fragmentArgs = new PlatformTabActivity.ArgsBuilder()
                        .addFragment(SettleOrdersFragment.class.getName(), mContext.getString(R.string.financial_settle_title))
                        .addFragment(AdjustOrdersFragment.class.getName(), mContext.getString(R.string.financial_adjust_title))
                        .build();
                mArgs.putString(AbsFinancialFragment.RENO,summaryList.get(position).mReNo);
                Intent intent = PlatformTabActivity.buildIntent(mContext, mContext.getString(R.string.financial_reconciliation_detail), fragmentArgs);
                intent.putExtra(PlatformTopbarActivity.EXTRA_ARGS,mArgs);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    public void notifyData(List itemList) {
        summaryList.addAll(itemList);
        notifyDataSetChanged();
    }

    public static class myViewHolder{
        RelativeLayout relativeContainer;
        TextView mSettleNo, mSettleSubject, mSettleCount, mSettleAmount, mAdjustAmount;
        public myViewHolder(View itemView){
            super();
            relativeContainer = (RelativeLayout) itemView.findViewById(R.id.receipts_summary_container);
            mSettleNo = (TextView) itemView.findViewById(R.id.receipts_settle_no);
            mSettleSubject = (TextView) itemView.findViewById(R.id.receipts_settle_subject);
            mSettleCount = (TextView) itemView.findViewById(R.id.receipts_summary_count);
            mSettleAmount = (TextView) itemView.findViewById(R.id.receipts_summary_settle_amount);
            mAdjustAmount = (TextView) itemView.findViewById(R.id.receipts_summary_adjust_amount);
        }
    }
}
