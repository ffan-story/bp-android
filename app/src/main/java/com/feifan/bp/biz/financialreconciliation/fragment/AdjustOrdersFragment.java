package com.feifan.bp.biz.financialreconciliation.fragment;

import android.view.View;

import com.android.volley.Response;
import com.feifan.bp.base.network.response.ToastErrorListener;
import com.feifan.bp.biz.financialreconciliation.ReconciliationCtrl;
import com.feifan.bp.biz.financialreconciliation.adapter.AdjustOrderAdapter;
import com.feifan.bp.biz.financialreconciliation.model.AdjustOrderModel;

import java.util.List;

/**
 * 财务对账 -- 调账信息
 * Created by konta on 2016/3/24.
 */
public class AdjustOrdersFragment extends AbsFinancialFragment {
    private List<AdjustOrderModel.AdjustOrder> adjustOrders;

    @Override
    protected void preFetchData() {

    }

    @Override
    protected void fetchData(boolean isLoadMore) {
        ReconciliationCtrl.getReconciliationAdjustOrders(mReNo, "2", new Response.Listener<AdjustOrderModel>() {
            @Override
            public void onResponse(AdjustOrderModel model) {
                stopRefresh();
                setContentEmpty(false);
                setContentShown(true);
                if (isAdded() && model != null) {
                    fillView(model);
                }
            }
        }, new ToastErrorListener());

    }

    private void fillView(AdjustOrderModel model) {
        adjustOrders = model.adjustOrders;
        if(adjustOrders != null && adjustOrders.size() > 0){
            mNoDataView.setVisibility(View.GONE);
        }else {
            mList.setAdapter(null);
            mNoDataView.setVisibility(View.VISIBLE);
            return;
        }
        mList.setAdapter(new AdjustOrderAdapter(getActivity(),adjustOrders));
    }
}
