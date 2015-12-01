package com.feifan.bp.transactionflow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.feifan.bp.transactionflow.FlashListModel.FlashDetailModel;
import com.feifan.bp.R;

import java.util.ArrayList;

/**
 * Created by Frank on 15/11/9.
 */
public class FlowListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<FlashDetailModel> mFlashDetailList;

    public FlowListAdapter(Context context,ArrayList<FlashDetailModel> flashDetailList) {
        mFlashDetailList = flashDetailList;
        mContext = context;
    }

    public void setData(ArrayList<FlashDetailModel> flashDetailList){
        mFlashDetailList = flashDetailList;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mFlashDetailList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFlashDetailList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_flashbuy_detail, parent, false);
            holder = new ViewHolder();
            holder.textView1 = (TextView) convertView.findViewById(R.id.textView1);
            holder.textView2 = (TextView) convertView.findViewById(R.id.textView2);
            holder.textView3 = (TextView) convertView.findViewById(R.id.textView3);
            holder.textView4 = (TextView) convertView.findViewById(R.id.textView4);
            holder.textView5 = (TextView) convertView.findViewById(R.id.textView5);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        if(mFlashDetailList!=null){
            holder.textView1.setText(mFlashDetailList.get(position).getBillNo());
            holder.textView2.setText(mFlashDetailList.get(position).getOrderNo());
            holder.textView3.setText(mFlashDetailList.get(position).getMobile());
            holder.textView4.setText(mFlashDetailList.get(position).getOrderTradeSuccessTime());
            holder.textView5.setText(mFlashDetailList.get(position).getOrderRefundAuditTime());
        }
        return convertView;
    }

    public static class ViewHolder{
        public TextView textView1, textView2, textView3, textView4, textView5;
    }
}
