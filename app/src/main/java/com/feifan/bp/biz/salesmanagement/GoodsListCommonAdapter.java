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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.biz.salesmanagement.GoodsListModel.GoodsDetailModel;

import java.util.List;

public class GoodsListCommonAdapter extends RecyclerView.Adapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<GoodsDetailModel> mListData;
    private int enrollStatus;
    private String promotionId;
    private boolean isCutOff;

    public GoodsListCommonAdapter(Context context, List<GoodsDetailModel> mListData, int enrollStatus,String promotionId,boolean isCutOff) {
        this.context = context;
        this.mListData = mListData;
        this.enrollStatus = enrollStatus;
        this.promotionId = promotionId;
        this.isCutOff = isCutOff;
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
            case GoodsListFragment.STATUS_NO_COMMIT:
                commonViewHolder.tvProductStatus.setText("状态:未提交");
                break;
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
        commonViewHolder.rlGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(enrollStatus == GoodsListFragment.STATUS_AUDIT_DENY && !isCutOff){
                    Bundle args = new Bundle();
                    args.putString(InstEvenSkuSettFragment.EXTRA_PARTAKE_EVENT_ID, promotionId);
                    args.putString(InstEvenSkuSettFragment.EXTRA_PARTAKE_GOODS_CODE, mListData.get(position).getGoodsCode());
                    args.putBoolean(InstEvenSkuSettFragment.EXTRA_EVENT_GOODS_ACTION, false);
                    args.putBoolean(InstEvenSkuSettFragment.EXTRA_EVENT_IS_STATUS_REFUSE, true);
                    PlatformTopbarActivity.startActivityForResult((RegisterDetailActivity) context, InstEvenSkuSettFragment.class.getName(), "设置详情", args);
                }else {
                    Bundle args = new Bundle();
                    args.putString(InstEventSignUpDetailFragment.EXTRA_PARTAKE_EVENT_ID,promotionId);
                    args.putString(InstEventSignUpDetailFragment.EXTRA_PARTAKE_GOODS_CODE, mListData.get(position).getGoodsCode());
                    if (enrollStatus == GoodsListFragment.STATUS_AUDIT_DENY){
                        args.putBoolean(InstEventSignUpDetailFragment.EXTRA_PARTAKE_GOODS_IS_REFUSE, true);
                    }else{
                        args.putBoolean(InstEventSignUpDetailFragment.EXTRA_PARTAKE_GOODS_IS_REFUSE, false);
                    }
                    PlatformTopbarActivity.startActivityForResult((RegisterDetailActivity) context, InstEventSignUpDetailFragment.class.getName(),"设置详情", args);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    class CommonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvProductName;
        private TextView tvProductStatus;
        private CheckBox cbSelect;
        private RelativeLayout rlGoods;

        public CommonViewHolder(View itemView) {
            super(itemView);
            tvProductName = (TextView) itemView.findViewById(R.id.tv_product_name);
            tvProductStatus = (TextView) itemView.findViewById(R.id.tv_product_status);
            cbSelect = (CheckBox) itemView.findViewById(R.id.check);
            rlGoods = (RelativeLayout) itemView.findViewById(R.id.rl_goods);
        }

        @Override
        public void onClick(View v) {

        }

    }
}
