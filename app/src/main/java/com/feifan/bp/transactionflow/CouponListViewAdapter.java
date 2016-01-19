package com.feifan.bp.transactionflow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.feifan.bp.R;
import com.feifan.bp.util.NumberUtil;

import java.util.List;

/**
 * Created by konta on 2016/1/19.
 */
public class CouponListViewAdapter extends BaseAdapter {
        private final String TAG = "CouponListAdapter";
    private Context mContext;
    private List<CouponSummaryModel.CouponDetail> couponDetails;

    public CouponListViewAdapter(Context context, List<CouponSummaryModel.CouponDetail> couponList){
        mContext = context;
        couponDetails = couponList;
    }

    @Override
    public int getCount() {
        return couponDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return couponDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.check_coupon_detail_item,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.mCouponCode.setText(couponDetails.get(position).CPCode);
        holder.mCouponName.setText(couponDetails.get(position).CPName);
        holder.mAwardMoney.setText(mContext.getString(R.string.money, NumberUtil.moneyFormat(couponDetails.get(position).awardMoney, 2)));
        holder.mChargeOffTime.setText(couponDetails.get(position).chargeoffTime);

        return convertView;
    }

    public static class ViewHolder{
        public TextView mCouponCode,mCouponName,mAwardMoney,mChargeOffTime;
        public ViewHolder(View itemView){
            mCouponCode = (TextView)itemView.findViewById(R.id.coupon_code);
            mCouponName = (TextView)itemView.findViewById(R.id.coupon_name);
            mAwardMoney = (TextView)itemView.findViewById(R.id.coupon_award_money);
            mChargeOffTime = (TextView)itemView.findViewById(R.id.chargeoff_time);
        }
    }
}
