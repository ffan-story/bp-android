package com.feifan.bp.transactionflow;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.feifan.bp.R;
import com.feifan.bp.util.NumberUtil;

import java.util.List;

/**
 * Created by konta on 2016/1/15.
 */
public class InstantOrderDetailAdapter extends BaseAdapter {

    private Context mContext;
    private List<InstantOrderDetailModel.OrderDetail> mOrderList;
    private String mIsOnlyRefund;

    public InstantOrderDetailAdapter(Context context, List<InstantOrderDetailModel.OrderDetail> orders, String isOnlyRefund) {
        mContext = context;
        mOrderList = orders;
        mIsOnlyRefund = isOnlyRefund;
    }

    @Override
    public int getCount() {
        return mOrderList.size();
    }

    @Override
    public Object getItem(int position) {
        return mOrderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(null == convertView){
            if(mIsOnlyRefund.equals("1")){//退款单列表
                convertView = View.inflate(mContext, R.layout.check_instant_order_refund_item,null);
            }else{//全部订单列表
                convertView = View.inflate(mContext, R.layout.check_instant_order_detail_item,null);
            }
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        if(mIsOnlyRefund.equals("1")){//退款单明细
            holder.mOrderCode.setText(mOrderList.get(position).orderNumber);
            holder.mTradeMoney.setText(mContext.getString(R.string.money,NumberUtil.moneyFormat(mOrderList.get(position).tradeMoney,2)));
            holder.mTradeSuccDate.setText(mOrderList.get(position).tradeTime);
            holder.mRefundDate.setText(mOrderList.get(position).refundTime);
            holder.mRefundMoney.setText("-" + NumberUtil.moneyFormat(mOrderList.get(position).tradeMoney,2));
        }else{//全部订单明细
            holder.mOrderCode.setText(mOrderList.get(position).orderNumber);
            holder.mTradeMoney.setText(mContext.getString(R.string.money,NumberUtil.moneyFormat(mOrderList.get(position).tradeMoney,2)));
            holder.mTradeDate.setText(mOrderList.get(position).tradeTime);
        }

        return convertView;
    }

    public static class ViewHolder{
        TextView mOrderCode,mTradeMoney,mTradeDate,mTradeSuccDate,mRefundDate,mRefundMoney;
        public ViewHolder(View itemView){
            mOrderCode = (TextView) itemView.findViewById(R.id.order_code);
            mTradeMoney = (TextView) itemView.findViewById(R.id.order_trade_money);
            mTradeDate = (TextView) itemView.findViewById(R.id.order_trade_date);
            mTradeSuccDate = (TextView) itemView.findViewById(R.id.order_trade_succ_date);
            mRefundDate = (TextView) itemView.findViewById(R.id.order_refund_date);
            mRefundMoney = (TextView) itemView.findViewById(R.id.order_refund_money);
        }
    }
}
