package com.feifan.bp.biz.transactionflowing.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.biz.transactionflowing.fragment.InstantOrderDetailFragment;
import com.feifan.bp.biz.transactionflowing.model.InstantDetailModel;
import com.feifan.bp.util.NumberUtil;

import java.util.Iterator;
import java.util.List;


/**
 * Created by konta on 2016/1/12.
 */
public class InstantDetailListAdapter extends RecyclerView.Adapter<InstantDetailListAdapter.MyViewHolder>{

    public static final String GOODSID = "goodsId";
    public static final String GOODSNAME = "goodsName";

    private Context mContext;
    private Bundle mArgs;
    private List<InstantDetailModel.InstantDetail> mDetails;
    /**
     * 是否只包含退款
     */
    private String onlyRefund;
    private static final String TAG = "DetailListAdapter";

    public InstantDetailListAdapter(Context context,
                                    List<InstantDetailModel.InstantDetail> details,
                                    Bundle args){
        mContext = context;
        initData(details, args);
    }

    private void initData(List<InstantDetailModel.InstantDetail> details, Bundle args) {
        mDetails = details;
        mArgs = args;
        onlyRefund = mArgs.getString("onlyRefund");
        //筛选
        Iterator iterator = mDetails.iterator();
        while(iterator.hasNext()){
            InstantDetailModel.InstantDetail detail = (InstantDetailModel.InstantDetail) iterator.next();
            if("0".equals(onlyRefund)){
                if("0".equals(detail.payCount)){
                    iterator.remove();
                }
            }else{
                if("0".equals(detail.refundCount)){
                    iterator.remove();
                }
            }

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_instant_detail_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.mTitle.setText(mDetails.get(position).goodsName);
        if("1".equals(onlyRefund)){//退款汇总
            holder.tvDetailMoney.setText(mContext.getString(R.string.order_refund_amount));
            holder.tvDetailCount.setText(mContext.getString(R.string.order_refund_count));
            holder.mTradeMoney.setText(NumberUtil.moneyFormat(mDetails.get(position).refundAmount,2));
            holder.mTradeCount.setText(mDetails.get(position).refundCount);
        }else{//全部汇总
            holder.tvDetailMoney.setText(mContext.getString(R.string.order_trade_amount));
            holder.tvDetailCount.setText(mContext.getString(R.string.order_trade_count));
            holder.mTradeMoney.setText("+" + NumberUtil.moneyFormat(mDetails.get(position).payAmount,2));
            holder.mTradeCount.setText(mDetails.get(position).payCount);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mArgs.putString(GOODSID, mDetails.get(position).googsId);
                mArgs.putString(GOODSNAME, mDetails.get(position).goodsName);
                PlatformTopbarActivity.startActivity(mContext, InstantOrderDetailFragment.class.getName(),
                        mContext.getString(R.string.instant_order_detail), mArgs);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDetails.size();
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
