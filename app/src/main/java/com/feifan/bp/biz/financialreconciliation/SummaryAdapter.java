package com.feifan.bp.biz.financialreconciliation;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.feifan.bp.R;

import java.util.List;

/**
 * Created by konta on 2016/3/21.
 */
public class SummaryAdapter extends BaseAdapter {

    private Context mContext;

    public SummaryAdapter(Context context) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        myViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.financial_summary_item,parent,false);
            holder = new myViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (myViewHolder) convertView.getTag();
        }

        holder.mSettleAmount.setText(Html.fromHtml(String.format(mContext.getString(R.string.two_row_33_99),"123","计算金额")));

        return convertView;
    }

    public void notifyData(List list) {

    }

    public static class myViewHolder{
        TextView mSettleNo, mSettleSubject, mSettleCount, mSettleAmount, mAdjustAmount;
        public myViewHolder(View itemView){
            super();
            mSettleNo = (TextView) itemView.findViewById(R.id.receipts_settle_no);
            mSettleSubject = (TextView) itemView.findViewById(R.id.receipts_settle_subject);
            mSettleCount = (TextView) itemView.findViewById(R.id.receipts_summary_count);
            mSettleAmount = (TextView) itemView.findViewById(R.id.receipts_summary_settle_amount);
            mAdjustAmount = (TextView) itemView.findViewById(R.id.receipts_summary_adjust_amount);
        }
    }
}
