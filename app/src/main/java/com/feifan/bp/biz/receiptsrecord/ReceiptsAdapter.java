package com.feifan.bp.biz.receiptsrecord;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.feifan.bp.R;

import java.util.List;

/**
 * Created by konta on 2016/3/17.
 */
public class ReceiptsAdapter extends BaseAdapter {

    private Context mContext;
    private List<ReceiptsModel.ReceiptsRecord> mList;

    public ReceiptsAdapter(Context context, List<ReceiptsModel.ReceiptsRecord> mReceiptsList){
        mContext = context;
        mList = mReceiptsList;
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
        ReceiptsViewHolder holder;
        if(null == convertView){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.receipts_item,parent,false);
            holder = new ReceiptsViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ReceiptsViewHolder) convertView.getTag();
        }
        holder.payType.setText(mList.get(position).mPayType);
        holder.orderNo.setText(mList.get(position).mRecordNo);
        holder.orderStatus.setText(mList.get(position).mStatus);
        holder.payTime.setText(mList.get(position).mReceiptsTime);
        holder.orderAmt.setText(mContext.getString(R.string.money,mList.get(position).mReceiptsAmount));
        holder.realPayAmt.setText(mContext.getString(R.string.money,mList.get(position).mRealAmount));
        return convertView;
    }

    public void notifyData(List<ReceiptsModel.ReceiptsRecord> itemList) {
        if(itemList != null && itemList.size() > 0){
            mList.addAll(itemList);
            notifyDataSetChanged();
        }
    }

    public static class ReceiptsViewHolder extends RecyclerView.ViewHolder{
        public TextView payType,orderNo,orderStatus,payTime,orderAmt,realPayAmt;
        public ReceiptsViewHolder(View itemView) {
            super(itemView);
            payType = (TextView) itemView.findViewById(R.id.receipts_pay_type);
            orderNo = (TextView) itemView.findViewById(R.id.receipts_pay_number);
            orderStatus = (TextView) itemView.findViewById(R.id.receipts_status);
            payTime = (TextView) itemView.findViewById(R.id.receipts_time);
            orderAmt = (TextView) itemView.findViewById(R.id.receipts_amount);
            realPayAmt = (TextView) itemView.findViewById(R.id.receipts_real_amount);
        }
    }
}
