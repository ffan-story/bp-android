package com.feifan.bp.biz.salesmanagement;

/**
 * 报名活动商品列表适配器
 * <p/>
 * Created by Frank on 15/12/21.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.util.LogUtil;

import java.util.HashMap;
import java.util.List;

import com.feifan.bp.biz.salesmanagement.GoodsListModel.GoodsDetailModel;

public class GoodsListSwipeAdapter extends RecyclerView.Adapter {

    private Context context;
    private LayoutInflater layoutInflater;

    private List<GoodsDetailModel> mListData;
    private HashMap<Integer, Boolean> checkStatus;
    private String promotionId;
    private onCheckChangeListener checkChangeListener;
    private onItemDeleteListener itemDeleteListener;

    public GoodsListSwipeAdapter(Context context, List<GoodsDetailModel> mListData, HashMap<Integer, Boolean> checkStatus, String promotionId) {
        this.context = context;
        this.mListData = mListData;
        this.checkStatus = checkStatus;
        this.promotionId = promotionId;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.promotion_goods_item, parent, false);
        return new SwipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final SwipeViewHolder swipeViewHolder = (SwipeViewHolder) holder;
        GoodsDetailModel model = mListData.get(position);
        swipeViewHolder.tvProductName.setText(model.getGoodsName());
        swipeViewHolder.tvProductStatus.setText("状态:未提交");

        swipeViewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        swipeViewHolder.rlGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString(InstEvenSkuSettFragment.EXTRA_PARTAKE_EVENT_ID, promotionId);
                args.putString(InstEvenSkuSettFragment.EXTRA_PARTAKE_GOODS_CODE, mListData.get(position).getGoodsCode());
                args.putBoolean(InstEvenSkuSettFragment.EXTRA_EVENT_GOODS_ACTION, false);
                args.putBoolean(InstEvenSkuSettFragment.EXTRA_EVENT_IS_STATUS_REFUSE, false);
                PlatformTopbarActivity.startActivityForResult((RegisterDetailActivity) context, InstEvenSkuSettFragment.class.getName(), "设置详情", args);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    class SwipeViewHolder extends RecyclerView.ViewHolder {

        private TextView tvProductName;
        private TextView tvProductStatus;
        private TextView tvDelete;
        private CheckBox cbSelect;
        private RelativeLayout rlGoods;

        public SwipeViewHolder(View itemView) {
            super(itemView);
            tvProductName = (TextView) itemView.findViewById(R.id.tv_product_name);
            tvProductStatus = (TextView) itemView.findViewById(R.id.tv_product_status);
            tvDelete = (TextView) itemView.findViewById(R.id.tv_delete);
            cbSelect = (CheckBox) itemView.findViewById(R.id.check);
            rlGoods = (RelativeLayout) itemView.findViewById(R.id.rl_goods);
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
