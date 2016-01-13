package com.feifan.bp.salesmanagement;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feifan.bp.PlatformTopbarActivity;

import com.feifan.bp.R;
import com.feifan.bp.salesmanagement.PromotionListModel.PromotionDetailModel;

import java.util.List;

/**
 * 活动列表适配器
 * Created by Frank on 15/12/18.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ActivityVH> implements RecyclerOnItemClickListener {

    private Context context;
    private final List<PromotionDetailModel> data;
    private Boolean isRegistered;//活动是否报名

    public EventListAdapter(Context context, List<PromotionDetailModel> data, Boolean isRegistered) {
        this.context = context;
        this.data = data;
        this.isRegistered = isRegistered;
    }

    @Override
    public ActivityVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity_list, parent, false);
        return new ActivityVH(view, this);
    }

    @Override
    public void onBindViewHolder(ActivityVH holder, final int position) {
        PromotionDetailModel model = data.get(position);
        if (isRegistered) {
            holder.tv_status_label.setText("报名状态: ");
            holder.tv_activity_status.setText(model.getPromotionEnrollStatusName());
            if (model.getPromotionEnrollStatus()==2) {
                holder.tv_activity_status.setTextColor(context.getResources().getColor(R.color.red));
            } else {
                holder.tv_activity_status.setTextColor(context.getResources().getColor(R.color.accent));
            }
        } else {
            holder.tv_status_label.setText("已报名: ");
            holder.tv_activity_status.setText(model.getEnrollCount());
        }
        holder.tv_activity_name.setText(model.getPromotionName());
        holder.tv_activity_type.setText(model.getPromotionTypeName());
        if (model.getPromotionType().equals("0")) {
            holder.tv_activity_type.setBackgroundResource(R.drawable.pinkred_corner_rect);
        } else if (model.getPromotionType().equals("1")) {
            holder.tv_activity_type.setBackgroundResource(R.drawable.orange_corner_rect);
        }

        holder.tv_activity_sdate.setText(formatDate(model.getStartTime()));
        holder.tv_activity_edate.setText(formatDate(model.getEndTime()));
    }

    private SpannableString formatDate(String date){
        SpannableString formatDate = new SpannableString(date);
        formatDate.setSpan(new StyleSpan(Typeface.BOLD),0,10, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        formatDate.setSpan(new RelativeSizeSpan(1.1f),0,10, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        formatDate.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.font_color_33)),0,10, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return formatDate;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onItemClicked(View view, int position) {
        if (isRegistered) {//已报名活动
            if(data.get(position).getPromotionEnrollStatus()== 2){//报名已截止
                RegisterDetailActivity.startActivity(context, data.get(position).getPromotionCode(), data.get(position).getPromotionName(),true);
            }else{//报名未截止
                RegisterDetailActivity.startActivity(context, data.get(position).getPromotionCode(), data.get(position).getPromotionName(),false);
            }
        } else {//可报名活动
            Bundle args = new Bundle();
            args.putString(EventDetailFragment.EXTRA_KEY_ID, data.get(position).getPromotionCode());
            args.putString(EventDetailFragment.EXTRA_KEY_NAME, data.get(position).getPromotionName());
            args.putBoolean(EventDetailFragment.EXTRA_KEY_FLAG, true);
            PlatformTopbarActivity.startActivity(context, EventDetailFragment.class.getName(), context.getString(R.string.promotionDetail), args);
        }
    }

    public void add(List<PromotionDetailModel> items) {
        int previousDataSize = this.data.size();
        this.data.addAll(items);
        notifyItemRangeInserted(previousDataSize, items.size());
    }

    public static class ActivityVH extends RecyclerView.ViewHolder {

        TextView tv_activity_name, tv_activity_type, tv_activity_status, tv_activity_sdate, tv_activity_edate, tv_status_label;

        public ActivityVH(View itemView, final RecyclerOnItemClickListener recyclerOnItemClickListener) {
            super(itemView);
            tv_activity_name = (TextView) itemView.findViewById(R.id.tv_activity_name);
            tv_activity_type = (TextView) itemView.findViewById(R.id.tv_activity_type);
            tv_activity_status = (TextView) itemView.findViewById(R.id.tv_activity_status);
            tv_activity_sdate = (TextView) itemView.findViewById(R.id.tv_activity_sdate);
            tv_activity_edate = (TextView) itemView.findViewById(R.id.tv_activity_edate);
            tv_status_label = (TextView) itemView.findViewById(R.id.tv_status_label);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerOnItemClickListener != null) {
                        recyclerOnItemClickListener.onItemClicked(v, getAdapterPosition());
                    }
                }
            });
        }
    }
}
