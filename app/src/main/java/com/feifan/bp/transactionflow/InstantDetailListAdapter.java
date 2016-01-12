package com.feifan.bp.transactionflow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.feifan.bp.R;


/**
 * Created by konta on 2016/1/12.
 */
public class InstantDetailListAdapter extends BaseAdapter{

    private Context mContext;

    public InstantDetailListAdapter(Context context){
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return 5;
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
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_instant_detail,parent,false);
            holder = new ViewHolder();
            holder.mTitle = (TextView) convertView.findViewById(R.id.detail_title);
            holder.mTradeMoney = (TextView) convertView.findViewById(R.id.detail_trade_money);
            holder.mTradeCount = (TextView) convertView.findViewById(R.id.detail_trade_count);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.mTitle.setText("我的老家，就住在这个屯，我是这个屯里土生土长的人");
        holder.mTradeMoney.setText("+100,000,000.00");
        holder.mTradeCount.setText("1000");

        return convertView;
    }

    public static class ViewHolder{
        public TextView mTitle;
        public TextView mTradeMoney;
        public TextView mTradeCount;
    }

}
