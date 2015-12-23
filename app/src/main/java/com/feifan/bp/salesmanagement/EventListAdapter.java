package com.feifan.bp.salesmanagement;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.feifan.bp.R;

import java.util.List;

/**
 * 活动列表适配器
 * Created by Frank on 15/12/18.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ActivityVH> implements RecyclerOnItemClickListener {

    private Context context;
    private final List<ActivityModel> data;
    private Boolean isRegistered;//活动是否报名

    public EventListAdapter(Context context, List<ActivityModel> data, Boolean isRegistered) {
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
        ActivityModel activityModel = data.get(position);
        if (isRegistered) {
            holder.tv_status_label.setText("报名状态: ");
            holder.tv_activity_status.setText("已报名");
            holder.tv_activity_status.setTextColor(context.getResources().getColor(R.color.accent));
        } else {
            holder.tv_status_label.setText("已报名商家: ");
            holder.tv_activity_status.setText("50");
        }
        holder.tv_activity_name.setText(activityModel.getActivityName());
        holder.tv_activity_type.setText(activityModel.getActivityType());
        holder.tv_activity_sdate.setText(activityModel.getActivityStartDate());
        holder.tv_activity_edate.setText(activityModel.getActivityEndDate());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onItemClicked(View view, int position) {
        RegisterDetailActivity.startActivity(context,String.valueOf(position));
        Toast.makeText(view.getContext(), "Clicked position: " + position, Toast.LENGTH_SHORT).show();
    }

    public void add(List<ActivityModel> items) {
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
