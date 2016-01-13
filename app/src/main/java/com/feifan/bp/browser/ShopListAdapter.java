package com.feifan.bp.browser;

/**
 * Created by Frank on 15/10/28.
 */
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feifan.bp.R;

public class ShopListAdapter extends Adapter<ShopListAdapter.ViewHolder> {

    private Context  mContext;
    private String[] mDataset;
    private int selectPos;

    private MyItemClickListener mItemClickListener;

    public ShopListAdapter(Context mContext,String[] dataset,int selectPos) {
        this.mContext = mContext;
        this.mDataset = dataset;
        this.selectPos = selectPos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_shop_select,parent,false);
        ViewHolder holder = new ViewHolder(view,mItemClickListener);
        return holder;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mItemName.setText(mDataset[position]);
        if(position == selectPos){
            holder.mItemBg.setBackgroundColor(mContext.getResources().getColor(R.color.accent));
            holder.mItemName.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.mItemStatus.setBackground(mContext.getResources().getDrawable(R.mipmap.icon_selected));
        }else{
            holder.mItemBg.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            holder.mItemName.setTextColor(mContext.getResources().getColor(R.color.text_normal));
            holder.mItemStatus.setBackground(mContext.getResources().getDrawable(R.mipmap.icon_select));
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        private MyItemClickListener mListener;
        public RelativeLayout mItemBg;
        public TextView mItemName;
        public ImageView mItemStatus;
        public ViewHolder(View itemView,MyItemClickListener listener) {
            super(itemView);
            mItemBg = (RelativeLayout) itemView.findViewById(R.id.item_bg);
            mItemName = (TextView) itemView.findViewById(R.id.item_name);
            mItemStatus = (ImageView) itemView.findViewById(R.id.item_status);
            this.mListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                mListener.onItemClick(v,getAdapterPosition());
            }
        }
    }

    public interface MyItemClickListener {
        public void onItemClick(View view, int postion);
    }

}
