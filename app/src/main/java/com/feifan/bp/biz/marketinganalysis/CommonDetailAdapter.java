package com.feifan.bp.biz.marketinganalysis;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.feifan.bp.R;
import com.feifan.bp.Utils;

import java.util.List;

/**
 * Created by konta on 2016/2/3.
 */
public class CommonDetailAdapter extends BaseAdapter {

    private static final String TAG = "CommonDetailAdapter";
    private Context mContext;
    private List<CommonModel.CommonDetail> mDetails;

    public CommonDetailAdapter(Context context, List<CommonModel.CommonDetail> mCommonDetails) {
        mContext = context;
        mDetails = mCommonDetails;
    }

    @Override
    public int getCount() {
        return mDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return mDetails.get(position);
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
                mContext.getString(R.string.anal_red_coupon_code),mDetails.get(position).mCouponId));
        holder.mAwardMoney.setText(Html.fromHtml(String.format(mContext.getString(R.string.instant_discount_content),
                mContext.getString(R.string.coupon_award_amount), Utils.formatMoney(mDetails.get(position).mAwardMoney,2))));
        holder.mCouponName.setText(String.format(mContext.getString(R.string.instant_colon),
                mContext.getString(R.string.coupon_name),mDetails.get(position).mCouponName));
        holder.mChargeTime.setText(String.format(mContext.getString(R.string.instant_colon),
                mContext.getString(R.string.chargeoff_time),mDetails.get(position).mVerifyTime));

        return convertView;
    }

    public void notifyData(List<CommonModel.CommonDetail> items){
        mDetails.addAll(items);
        notifyDataSetChanged();
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
