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

public class GoodsListCommonAdapter extends RecyclerView.Adapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> mListData;
    private int enrollStatus;

    public GoodsListCommonAdapter(Context context, List<String> mListData, int enrollStatus) {
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
        String data = mListData.get(position);
        commonViewHolder.tvProductName.setText(data);
        commonViewHolder.cbSelect.setVisibility(View.GONE);
        switch (enrollStatus) {
            case ProductListFragment.STATUS_AUDIT:
                commonViewHolder.tvProductStatus.setText("状态:审核中");
                break;
            case ProductListFragment.STATUS_AUDIT_PASS:
                commonViewHolder.tvProductStatus.setText("状态:审核通过");
                break;
            case ProductListFragment.STATUS_AUDIT_DENY:
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
