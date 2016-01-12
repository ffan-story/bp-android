package com.feifan.bp.transactionflow;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feifan.bp.R;

/**
 * Created by konta on 2016/1/12.
 */
public class DetailListAdapter extends RecyclerView.Adapter<DetailListAdapter.DetailViewHolder> {

    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coupon_detail,parent,false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailViewHolder holder, int position) {
        holder.mCouponCode.setText("000001");
        holder.mCouponName.setText("一条大河~向东流一条大河~向东流一条大河~向东流一条大河~向东流一条大河~向东流");
        holder.mAwardMoney.setText("￥88.88");
        holder.mChargeoffTime.setText("2016-1-1 18:00:00");
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public static class DetailViewHolder extends RecyclerView.ViewHolder{
        TextView mCouponCode,mCouponName,mAwardMoney,mChargeoffTime;
        public DetailViewHolder(View itemView){
            super(itemView);
            mCouponCode = (TextView)itemView.findViewById(R.id.coupon_code);
            mCouponName = (TextView)itemView.findViewById(R.id.coupon_name);
            mAwardMoney = (TextView)itemView.findViewById(R.id.coupon_award_money);
            mChargeoffTime = (TextView)itemView.findViewById(R.id.chargeoff_time);
        }
    }
}
