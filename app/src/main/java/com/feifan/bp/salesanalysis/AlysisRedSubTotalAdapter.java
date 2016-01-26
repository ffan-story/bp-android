package com.feifan.bp.salesanalysis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.feifan.bp.R;
import com.feifan.bp.Utils;

import java.util.List;

/**
 * 红包分类汇总
 * Created by congjing on 2016/1/21.
 */
public class AlysisRedSubTotalAdapter extends BaseAdapter {
        private final String TAG = "CouponListAdapter";
    private Context mContext;
    private List<AlysisRedSubTotalListModel.RedListModel> mListSubTotal;

    public AlysisRedSubTotalAdapter(Context context, List<AlysisRedSubTotalListModel.RedListModel> mListSubTotal){
        mContext = context;
       this.mListSubTotal = mListSubTotal;
    }

    @Override
    public int getCount() {
        return mListSubTotal.size();
    }

    @Override
    public Object getItem(int position) {
        return mListSubTotal.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_analysis_red_subtotal,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.mTvCouponName.setText(mListSubTotal.get(position).mStrRedCouponName);
        holder.mTvChargeOffCount.setText(String.format(mContext.getString(R.string.instant_colon),
                mContext.getString(R.string.anal_charge_off_count),
                mListSubTotal.get(position).mStrChargeOffCount));
        holder.mTvSubsidyMoneyFf.setText(String.format(mContext.getString(R.string.instant_colon),
                mContext.getString(R.string.anal_subsidy_item_money_ff),
                Utils.formatMoney(mListSubTotal.get(position).mStrRedFeifan,2)));
        holder.mTvSubsidyMoneyVendor.setText(String.format(mContext.getString(R.string.instant_colon),
                mContext.getString(R.string.anal_subsidy_item_money_vendor),
                Utils.formatMoney(mListSubTotal.get(position).mStrRedVendor,2)));
        holder.mTvSubsidyMoneyThird.setText(String.format(mContext.getString(R.string.instant_colon),
                mContext.getString(R.string.anal_subsidy_item_money_third),
                Utils.formatMoney(mListSubTotal.get(position).mStrRedThird,2)));
        return convertView;
    }

    public static class ViewHolder{
        public TextView mTvChargeOffCount,mTvCouponName,mTvSubsidyMoneyFf,mTvSubsidyMoneyVendor,mTvSubsidyMoneyThird;
        public ViewHolder(View itemView){
            mTvChargeOffCount= (TextView)itemView.findViewById(R.id.tv_anal_charge_off_count);
            mTvCouponName= (TextView)itemView.findViewById(R.id.tv_sub_total_coupon_name);
            mTvSubsidyMoneyFf = (TextView)itemView.findViewById(R.id.tv_anal_subsidy_money_ff);
            mTvSubsidyMoneyVendor = (TextView)itemView.findViewById(R.id.tv_anal_subsidy_money_vendor);
            mTvSubsidyMoneyThird = (TextView)itemView.findViewById(R.id.tv_anal_subsidy_money_third);
        }
    }
}
