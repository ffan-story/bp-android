package com.feifan.bp.biz.financialreconciliation.fragment;

import android.view.View;

import com.android.volley.Response;
import com.feifan.bp.base.network.response.ToastErrorListener;
import com.feifan.bp.biz.financialreconciliation.ReconciliationCtrl;
import com.feifan.bp.biz.financialreconciliation.adapter.SettleOrderAdapter;
import com.feifan.bp.biz.financialreconciliation.model.SettleOrdersModel;

import java.util.List;

/**
 * 财务对账 -- 结算单
 * Created by konta on 2016/3/22.
 */
public class SettleOrdersFragment extends AbsFinancialFragment {
    private List<SettleOrdersModel.SettleOrder> settleOrders;

    @Override
    protected void preFetchData() {

    }

    @Override
    protected void fetchData(boolean isLoadMore) {
        ReconciliationCtrl.getReconciliationSettleOrders(mReNo, "1", new Response.Listener<SettleOrdersModel>() {
            @Override
            public void onResponse(SettleOrdersModel model) {
                stopRefresh();
                setContentEmpty(false);
                setContentShown(true);
                if (isAdded() && model != null) {
                    fillView(model);
                }

            }
        }, new ToastErrorListener());

    }

    private void fillView(SettleOrdersModel model) {
        settleOrders = model.settleDetails;
        if(settleOrders != null && settleOrders.size() > 0){
            mNoDataView.setVisibility(View.GONE);
        }else {
            mList.setAdapter(null);
            mNoDataView.setVisibility(View.VISIBLE);
            return;
        }
        mList.setAdapter(new SettleOrderAdapter(getActivity(), settleOrders, mArgs));
    }

}
