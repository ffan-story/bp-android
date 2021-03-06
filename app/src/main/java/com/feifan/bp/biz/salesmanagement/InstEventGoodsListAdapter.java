package com.feifan.bp.biz.salesmanagement;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;

import java.util.List;

/**
 * 商品列表
 * Created by congjing on 15/12/18.
 */
public class InstEventGoodsListAdapter extends RecyclerView.Adapter<InstEventGoodsListAdapter.GoodsDatas> implements RecyclerOnItemClickListener {

    private Activity context;
    private final List<com.feifan.bp.biz.salesmanagement.InstEventGoodsListModel.GoodsListData> data;
    private String mEventId;

    public InstEventGoodsListAdapter(Activity context, List<com.feifan.bp.biz.salesmanagement.InstEventGoodsListModel.GoodsListData> data,String mEventId) {
        this.context = context;
        this.mEventId = mEventId;
        this.data = data;
    }

    @Override
    public GoodsDatas onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.instant_event_goods_list_item, parent, false);
        return new GoodsDatas(view, this);
    }

    @Override
    public void onBindViewHolder(GoodsDatas holder, final int position) {
        com.feifan.bp.biz.salesmanagement.InstEventGoodsListModel.GoodsListData GoodsListModel = data.get(position);
        holder.mTvGoodsName.setText(GoodsListModel.mStrGoodsName);
        holder.mTvGoodsStatus.setText(GoodsListModel.mStrGoodsStatusTxt);
        if (!TextUtils.isEmpty(GoodsListModel.mStrGoodsStatus) && GoodsListModel.mStrGoodsStatus.equals("1")){//已提交
            holder.mTvGoodsName.setTextColor(context.getResources().getColor(R.color.font_color_99));
            holder.mTvGoodsStatus.setTextColor(context.getResources().getColor(R.color.font_color_99));
            holder.mImgArrow.setVisibility(View.VISIBLE);
        }else{//可提交
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
        if (!data.get(position).mStrGoodsStatus.equals("1")) {
//            context.finish();
            Bundle args = new Bundle();
            args.putString(com.feifan.bp.biz.salesmanagement.InstEvenSkuSettFragment.EXTRA_PARTAKE_EVENT_ID, mEventId);
            args.putString(com.feifan.bp.biz.salesmanagement.InstEvenSkuSettFragment.EXTRA_PARTAKE_GOODS_CODE, data.get(position).mStrGoodsId);
            args.putBoolean(com.feifan.bp.biz.salesmanagement.InstEvenSkuSettFragment.EXTRA_EVENT_GOODS_ACTION, true);
            PlatformTopbarActivity.startActivityForResult(context, com.feifan.bp.biz.salesmanagement.InstEvenSkuSettFragment.class.getName(), context.getString(R.string.instant_setting_detail), args);
        }
    }

    public void notifyData(List<com.feifan.bp.biz.salesmanagement.InstEventGoodsListModel.GoodsListData> items) {
        int previousDataSize = this.data.size();
        this.data.addAll(items);
        notifyItemRangeInserted(previousDataSize, items.size());
    }

    public static class GoodsDatas extends RecyclerView.ViewHolder {

        TextView mTvGoodsName, mTvGoodsStatus;
        ImageView mImgArrow;

        public GoodsDatas(View itemView, final RecyclerOnItemClickListener recyclerOnItemClickListener) {
            super(itemView);
            mTvGoodsName = (TextView) itemView.findViewById(R.id.tv_instant_goods_name);
            mTvGoodsStatus = (TextView) itemView.findViewById(R.id.tv_instant_goods_status);
            mImgArrow = (ImageView) itemView.findViewById(R.id.img_instant_goods_arrow);
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
