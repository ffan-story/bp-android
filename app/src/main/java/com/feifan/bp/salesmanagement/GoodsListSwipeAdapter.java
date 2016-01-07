package com.feifan.bp.salesmanagement;

/**
 * 报名活动商品列表适配器
 * <p/>
 * Created by Frank on 15/12/21.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.feifan.bp.R;
import com.feifan.bp.util.LogUtil;
import com.feifan.bp.widget.paginate.SwipeMenuViewHolder;

import java.util.HashMap;
import java.util.List;

public class GoodsListSwipeAdapter extends RecyclerView.Adapter {

    private Context context;
    private LayoutInflater layoutInflater;

    private List<String> mListData;
    private HashMap<Integer, Boolean> checkStatus;
    private onCheckChangeListener checkChangeListener;
    private onItemDeleteListener itemDeleteListener;
    private int enrollStatus;

    public GoodsListSwipeAdapter(Context context, List<String> mListData, int enrollStatus, HashMap<Integer, Boolean> checkStatus) {
        this.context = context;
        this.mListData = mListData;
        this.checkStatus = checkStatus;
        this.enrollStatus = enrollStatus;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View captureView = layoutInflater.inflate(R.layout.campaign_goods_item, parent, false);
        View swipeMenuView = layoutInflater.inflate(R.layout.swipe_menu_view, parent, false);
        swipeMenuView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return new SwipeViewHolder(context, swipeMenuView, captureView, SwipeMenuViewHolder.EDGE_RIGHT).getDragViewHolder();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final SwipeViewHolder swipeViewHolder = (SwipeViewHolder) SwipeMenuViewHolder.getHolder(holder);
        String data = mListData.get(position);
        swipeViewHolder.tvProductName.setText(data);
        swipeViewHolder.tvProductStatus.setText("状态:未提交");

        swipeViewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListData.remove(position);
                LogUtil.i("fangke", "onItemDelete调用的位置" + position);
                itemDeleteListener.onDelete(position);
            }
        });
        //页面滚动时设置不调用onCheckedChanged导致Checkbox自动选中
        swipeViewHolder.cbSelect.setOnCheckedChangeListener(null);
        swipeViewHolder.cbSelect.setChecked(checkStatus.get(position) == null ? false : true);
        swipeViewHolder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LogUtil.i("fangke", "onCheckChanged调用的位置" + position);
                checkChangeListener.getCheckData(position, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    class SwipeViewHolder extends SwipeMenuViewHolder implements View.OnClickListener {

        private TextView tvProductName;
        private TextView tvProductStatus;
        private TextView tvDelete;
        private CheckBox cbSelect;

        public SwipeViewHolder(Context context, View swipeMenuView, View captureView, int mTrackingEdges) {
            super(context, swipeMenuView, captureView, mTrackingEdges);
        }


        @Override
        public void initView(View itemView) {
            tvProductName = (TextView) itemView.findViewById(R.id.tv_product_name);
            tvProductStatus = (TextView) itemView.findViewById(R.id.tv_product_status);
            tvDelete = (TextView) itemView.findViewById(R.id.tv_delete);
            cbSelect = (CheckBox) itemView.findViewById(R.id.check);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public interface onCheckChangeListener {
        void getCheckData(int position, boolean isChecked);
    }

    public void setCheckChangeListener(onCheckChangeListener checkChangeListener) {
        this.checkChangeListener = checkChangeListener;
    }

    public interface onItemDeleteListener {
        void onDelete(int position);
    }

    public void setItemDeleteListener(onItemDeleteListener itemDeleteListener) {
        this.itemDeleteListener = itemDeleteListener;
    }
}
