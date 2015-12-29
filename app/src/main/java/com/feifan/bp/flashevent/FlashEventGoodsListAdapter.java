package com.feifan.bp.flashevent;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.salesmanagement.RecyclerOnItemClickListener;
import com.feifan.bp.salesmanagement.RegisterDetailActivity;

import java.util.List;

/**
 * 商品列表
 * Created by congjing on 15/12/18.
 */
public class FlashEventGoodsListAdapter extends RecyclerView.Adapter<FlashEventGoodsListAdapter.GoodsDatas> implements RecyclerOnItemClickListener {

    private Context context;
    private final List<FlashEventGoodsListModel.GoodsListData> data;
    private Boolean isRegistered;//活动是否报名

    public FlashEventGoodsListAdapter(Context context, List<FlashEventGoodsListModel.GoodsListData> data, Boolean isRegistered) {
        this.context = context;
        this.data = data;
        this.isRegistered = isRegistered;
    }

    @Override
    public GoodsDatas onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flash_event_goods_list_item, parent, false);
        return new GoodsDatas(view, this);
    }

    @Override
    public void onBindViewHolder(GoodsDatas holder, final int position) {
        FlashEventGoodsListModel.GoodsListData GoodsListModel = data.get(position);
        holder.mTvGoodsName.setText(GoodsListModel.mStrGoodsName);
        if (!TextUtils.isEmpty(GoodsListModel.mStrGoodsStatus) && GoodsListModel.mStrGoodsStatus.equals("1")){//已提交
            holder.mTvGoodsStatus.setText("已提交");
            holder.mTvGoodsName.setTextColor(context.getResources().getColor(R.color.font_color_99));
            holder.mTvGoodsStatus.setTextColor(context.getResources().getColor(R.color.font_color_99));
            holder.mImgArrow.setVisibility(View.INVISIBLE);
        }else{//可提交
            holder.mTvGoodsStatus.setText("可提交");
            holder.mTvGoodsName.setTextColor(context.getResources().getColor(R.color.font_color_33));
            holder.mTvGoodsStatus.setTextColor(context.getResources().getColor(R.color.font_color_33));//可提交
            holder.mImgArrow.setVisibility(View.VISIBLE);
            holder.mTvGoodsStatus.setTag(GoodsListModel.mStrGoodsStatus);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onItemClicked(View view, int position) {
        if (!data.get(position).mStrGoodsStatus .equals("1")){
            Bundle args = new Bundle();
            args.putString(FlashEventGoodsSettingDetailFragment.EXTRA_EVENT_ID, "12323");
            args.putBoolean(FlashEventGoodsSettingDetailFragment.EXTRA_EVENT_IS_GOODS_SETTINGDETAIL, false);
            PlatformTopbarActivity.startActivity(context, FlashEventGoodsSettingDetailFragment.class.getName(), "设置详情",args);
        }

    }

    public void add(List<FlashEventGoodsListModel.GoodsListData> items) {
        int previousDataSize = this.data.size();
        this.data.addAll(items);
        notifyItemRangeInserted(previousDataSize, items.size());
    }

    public static class GoodsDatas extends RecyclerView.ViewHolder {

        TextView mTvGoodsName, mTvGoodsStatus;
        ImageView mImgArrow;

        public GoodsDatas(View itemView, final RecyclerOnItemClickListener recyclerOnItemClickListener) {
            super(itemView);
            mTvGoodsName = (TextView) itemView.findViewById(R.id.tv_flash_goods_name);
            mTvGoodsStatus = (TextView) itemView.findViewById(R.id.tv_flash_goods_status);
            mImgArrow = (ImageView) itemView.findViewById(R.id.img_flash_goods_arrow);
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
