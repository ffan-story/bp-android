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
import android.widget.TextView;

import com.feifan.bp.R;

import java.util.List;
import com.feifan.bp.salesmanagement.GoodsListModel.GoodsDetailModel;

public class GoodsListCommonAdapter extends RecyclerView.Adapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<GoodsDetailModel> mListData;
    private int enrollStatus;

    public GoodsListCommonAdapter(Context context, List<GoodsDetailModel> mListData, int enrollStatus) {
        this.context = context;
        this.mListData = mListData;
        this.enrollStatus = enrollStatus;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = layoutInflater.inflate(R.layout.campaign_goods_item, parent, false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final CommonViewHolder commonViewHolder = (CommonViewHolder)holder;
        GoodsDetailModel model = mListData.get(position);
        commonViewHolder.tvProductName.setText(model.getGoodsName());
        commonViewHolder.cbSelect.setVisibility(View.GONE);
        switch (enrollStatus) {
            case GoodsListFragment.STATUS_AUDIT:
                commonViewHolder.tvProductStatus.setText("状态:审核中");
                break;
            case GoodsListFragment.STATUS_AUDIT_PASS:
                commonViewHolder.tvProductStatus.setText("状态:审核通过");
                break;
            case GoodsListFragment.STATUS_AUDIT_DENY:
                commonViewHolder.tvProductStatus.setText("状态:审核拒绝");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    class CommonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvProductName;
        private TextView tvProductStatus;
        private CheckBox cbSelect;

        public CommonViewHolder(View itemView) {
            super(itemView);
            tvProductName = (TextView) itemView.findViewById(R.id.tv_product_name);
            tvProductStatus = (TextView) itemView.findViewById(R.id.tv_product_status);
            cbSelect = (CheckBox) itemView.findViewById(R.id.check);
        }

        @Override
        public void onClick(View v) {

        }

    }
}
