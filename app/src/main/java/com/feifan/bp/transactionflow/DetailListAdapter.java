package com.feifan.bp.transactionflow;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feifan.bp.R;

import java.util.List;

/**
 * Created by konta on 2016/1/12.
 */
public class DetailListAdapter extends RecyclerView.Adapter<DetailListAdapter.DetailViewHolder> {
    private List<CouponSummaryModel.CouponDetail> datas;
    public DetailListAdapter(List<CouponSummaryModel.CouponDetail> datas){
        this.datas = datas;
    }

    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_coupon_detail_item,parent,false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailViewHolder holder, int position) {
        holder.mCouponCode.setText(datas.get(position).CPCode);
        holder.mCouponName.setText(datas.get(position).CPName);
        holder.mAwardMoney.setText(datas.get(position).awardMoney + "");
        holder.mChargeoffTime.setText(datas.get(position).chargeoffTime + "");
    }

    @Override
    public int getItemCount() {
        Log.e("detail","datas.size" + datas.size());
        return datas.size();
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
