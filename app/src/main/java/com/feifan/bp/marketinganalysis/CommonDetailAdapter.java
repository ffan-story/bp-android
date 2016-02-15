package com.feifan.bp.marketinganalysis;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.feifan.bp.R;

/**
 * Created by konta on 2016/2/3.
 */
public class CommonDetailAdapter extends BaseAdapter {

    Context mContext;
    private static final String TAG = "CommonDetailAdapter";

    public CommonDetailAdapter(Context context) {
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
        MyViewHolder holder;
        if(null == convertView){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.marketing_common_item,null);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (MyViewHolder)convertView.getTag();
        }

        holder.mCouponCode.setText(String.format(mContext.getString(R.string.instant_colon),
                mContext.getString(R.string.anal_red_coupon_code),"0000000" + position));
        holder.mAwardMoney.setText(Html.fromHtml(String.format(mContext.getString(R.string.instant_discount_content),
                mContext.getString(R.string.coupon_award_amount),position)));
        holder.mCouponName.setText(String.format(mContext.getString(R.string.instant_colon),
                mContext.getString(R.string.coupon_name),"测试券名称" + position));
        holder.mChargeTime.setText(String.format(mContext.getString(R.string.instant_colon),
                mContext.getString(R.string.chargeoff_time),"2016-02-02 10:00:0" + position));

        return convertView;
    }

    public static class MyViewHolder{
        public TextView mCouponCode,mAwardMoney,mCouponName,mChargeTime;
        public MyViewHolder(View itemView){
            mCouponCode = (TextView) itemView.findViewById(R.id.common_code);
            mAwardMoney = (TextView) itemView.findViewById(R.id.common_award_money);
            mCouponName = (TextView) itemView.findViewById(R.id.common_coupon_name);
            mChargeTime = (TextView) itemView.findViewById(R.id.common_charge_off_time);
        }
    }
}
