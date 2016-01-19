package com.feifan.bp.transactionflow;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.util.NumberUtil;

import java.util.List;


/**
 * Created by konta on 2016/1/12.
 */
public class InstantDetailListAdapter extends RecyclerView.Adapter<InstantDetailListAdapter.MyViewHolder>{

    public static final String GOODSID = "goodsId";
    public static final String GOODSNAME = "goodsName";

    private Context context;
    private Bundle args;
    private List<InstantDetailModel.InstantDetail> details;
    /**
     * 是否只包含退款
     */
    private String onlyRefund;

    public InstantDetailListAdapter(Context context,
                                    List<InstantDetailModel.InstantDetail> details,
                                    Bundle args){
        this.context = context;
        this.details = details;
        this.args = args;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        onlyRefund = args.getString("onlyRefund");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_instant_detail_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.mTitle.setText(details.get(position).goodsName);
        if(onlyRefund.equals("1")){//退款汇总
            holder.tvDetailMoney.setText(context.getString(R.string.order_refund_amount));
            holder.tvDetailCount.setText(context.getString(R.string.order_refund_count));
            holder.mTradeMoney.setText(NumberUtil.moneyFormat(details.get(position).refundAmount,2));
            holder.mTradeCount.setText(details.get(position).refundCount);
        }else{//全部汇总
            holder.tvDetailMoney.setText(context.getString(R.string.order_trade_amount));
            holder.tvDetailCount.setText(context.getString(R.string.order_trade_count));
            holder.mTradeMoney.setText("+" + NumberUtil.moneyFormat(details.get(position).payAmount,2));
            holder.mTradeCount.setText(details.get(position).payCount);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString(GOODSID,details.get(position).googsId);
                args.putString(GOODSNAME,details.get(position).goodsName + position);
                PlatformTopbarActivity.startActivity(context, InstantOrderDetailFragment.class.getName(),
                        context.getString(R.string.instant_order_detail), args);
            }
        });
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView mTitle,mTradeMoney,mTradeCount;
        public TextView tvDetailMoney, tvDetailCount;
        public MyViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView)itemView.findViewById(R.id.detail_title);
            mTradeMoney = (TextView)itemView.findViewById(R.id.detail_trade_money);
            mTradeCount= (TextView)itemView.findViewById(R.id.detail_trade_count);
            tvDetailMoney = (TextView)itemView.findViewById(R.id.detail_money_tv);
            tvDetailCount = (TextView)itemView.findViewById(R.id.detail_count_tv);
        }
    }

}
