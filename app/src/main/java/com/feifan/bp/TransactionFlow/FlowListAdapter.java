package com.feifan.bp.TransactionFlow;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feifan.bp.TransactionFlow.FlashListModel.FlashDetailModel;
import com.feifan.bp.R;

import java.util.ArrayList;

/**
 * Created by Frank on 15/11/9.
 */
public class FlowListAdapter extends RecyclerView.Adapter<FlowListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<FlashDetailModel> mFlashDetailList;

    public FlowListAdapter(Context context, ArrayList<FlashDetailModel> flashDetailList) {
        mContext = context;
        mFlashDetailList = flashDetailList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_flashbuy_detail, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mFlashDetailList != null) {
            holder.textView1.setText(mFlashDetailList.get(position).getBillNo());
            holder.textView2.setText(mFlashDetailList.get(position).getOrderNo());
            holder.textView3.setText(mFlashDetailList.get(position).getMobile());
            holder.textView4.setText(mFlashDetailList.get(position).getOrderTradeSuccessTime());
            holder.textView5.setText(mFlashDetailList.get(position).getOrderRefundAuditTime());
        } else {
            holder.textView1.setText("");
            holder.textView2.setText("");
            holder.textView3.setText("");
            holder.textView4.setText("");
            holder.textView5.setText("");
        }

    }

    @Override
    public int getItemCount() {
        return mFlashDetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView1, textView2, textView3, textView4, textView5;

        public ViewHolder(View itemView) {
            super(itemView);
            textView1 = (TextView) itemView.findViewById(R.id.textView1);
            textView2 = (TextView) itemView.findViewById(R.id.textView2);
            textView3 = (TextView) itemView.findViewById(R.id.textView3);
            textView4 = (TextView) itemView.findViewById(R.id.textView4);
            textView5 = (TextView) itemView.findViewById(R.id.textView5);
        }
    }
}
