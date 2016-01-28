package com.feifan.bp.salesanalysis;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feifan.bp.R;
import com.feifan.bp.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * 红包核销明细  adapter
 * Created by congjing on 2016/1/21.
 */
public class AlysisRedDetailAdapter extends RecyclerView.Adapter<AlysisRedDetailAdapter.ViewHolder>  {
    private Context mContext;
    private List<AlysisRedDetailModel.RedDetailModel> mRedDetailsData = new ArrayList<>();

    public AlysisRedDetailAdapter(Context context, List<AlysisRedDetailModel.RedDetailModel> redDetailsData){
        mContext = context;
        this.mRedDetailsData = redDetailsData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_analysis_red_detail, parent, false);
        return new ViewHolder(view);
    }

    public void notifyData(List<AlysisRedDetailModel.RedDetailModel> items) {
        int previousDataSize = this.mRedDetailsData.size();
        this.mRedDetailsData.addAll(items);
        notifyItemRangeInserted(previousDataSize, items.size());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AlysisRedDetailModel.RedDetailModel redDetailsData = mRedDetailsData.get(position);
        holder.mTvRdeCode.setText(String.format(mContext.getString(R.string.instant_colon),
                mContext.getString(R.string.anal_red_coupon_code),redDetailsData.mStrCouponId));
        holder.mTvRedStatus.setText(redDetailsData.mStrStatus);

        holder.mTvSubsidyMoneyFf.setText(String.format(mContext.getString(R.string.instant_colon),
                mContext.getString(R.string.anal_subsidy_item_money_ff),Utils.formatMoney(redDetailsData.mStrFeifan, 2)));
        if (!TextUtils.isEmpty(redDetailsData.mStride2ndRow) && redDetailsData.mStride2ndRow.equals("0")){
            holder.mTvSubsidyMoneyThird.setVisibility(View.GONE);
            holder.mTvSubsidyMoneyVendor.setVisibility(View.GONE);
        }else{
            holder.mTvSubsidyMoneyThird.setVisibility(View.VISIBLE);
            holder.mTvSubsidyMoneyVendor.setVisibility(View.VISIBLE);
            holder.mTvSubsidyMoneyThird.setText(String.format(mContext.getString(R.string.instant_colon),
                    mContext.getString(R.string.anal_subsidy_item_money_third), Utils.formatMoney(redDetailsData.mStrThird, 2)));
            holder.mTvSubsidyMoneyVendor.setText(String.format(mContext.getString(R.string.instant_colon),
                    mContext.getString(R.string.anal_subsidy_item_money_vendor), Utils.formatMoney(redDetailsData.mStrVendor, 2)));
        }

        holder.mTvGetTime.setText(String.format(mContext.getString(R.string.instant_colon),
                mContext.getString(R.string.anal_subsidy_get_time),redDetailsData.mStrGetTime));
        holder.mTvEndTime.setText(String.format(mContext.getString(R.string.instant_colon),
                mContext.getString(R.string.anal_subsidy_end_time),redDetailsData.mStrValidTime));
        holder.mTvUseTime.setText(String.format(mContext.getString(R.string.instant_colon),
                mContext.getString(R.string.anal_subsidy_use_time),redDetailsData.mStrUseTime));
    }

    @Override
    public int getItemCount() {
        return mRedDetailsData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvEndTime,mTvGetTime,mTvUseTime,mTvSubsidyMoneyFf,mTvSubsidyMoneyVendor,mTvSubsidyMoneyThird,mTvRdeCode,mTvRedStatus;
        public ViewHolder(View itemView) {
            super(itemView);
            mTvEndTime = (TextView)itemView.findViewById(R.id.tv_anal_end_time);
            mTvGetTime = (TextView)itemView.findViewById(R.id.tv_anal_get_time);
            mTvUseTime = (TextView)itemView.findViewById(R.id.tv_anal_use_time);
            mTvSubsidyMoneyFf = (TextView)itemView.findViewById(R.id.tv_anal_subsidy_money_ff);
            mTvSubsidyMoneyVendor = (TextView)itemView.findViewById(R.id.tv_anal_subsidy_money_vendor);
            mTvSubsidyMoneyThird = (TextView)itemView.findViewById(R.id.tv_anal_subsidy_money_third);
            mTvRdeCode= (TextView)itemView.findViewById(R.id.tv_anal_red_code);
            mTvRedStatus= (TextView)itemView.findViewById(R.id.tv_anal_red_status);
        }
    }

}
