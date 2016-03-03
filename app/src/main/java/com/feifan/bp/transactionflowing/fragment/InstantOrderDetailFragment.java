package com.feifan.bp.transactionflowing.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.internal.widget.ViewStubCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.network.response.DialogErrorListener;
import com.feifan.bp.transactionflowing.adapter.InstantDetailListAdapter;
import com.feifan.bp.transactionflowing.adapter.InstantOrderDetailAdapter;
import com.feifan.bp.transactionflowing.model.InstantOrderDetailModel;
import com.feifan.bp.transactionflowing.TransFlowCtrl;
import com.feifan.bp.widget.LoadingMoreListView;
import com.feifan.bp.widget.OnLoadingMoreListener;
import com.feifan.material.MaterialDialog;

import org.json.JSONException;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * 交易明细
 * Created by konta on 2016/1/14.
 */
public class InstantOrderDetailFragment extends ProgressFragment implements OnLoadingMoreListener {
    private LoadingMoreListView mLoadDetailList;
    private TextView mQueryTime,mTotalCount,orderTitle;

    private String mStartDate;
    private String mEndDate;
    private String mQueryDate;

    private String isOnlyRefund;

    private int mPageIndex = 1;
    private int mLimit = 10;
    private int totalCount;

    private String mGoogsId;

    private MaterialDialog mDialog;
    private transient boolean isShowDlg = true;

    private List<InstantOrderDetailModel.OrderDetail> orders;
    private InstantOrderDetailAdapter mOrderDetailAdapter;
    private static final String TAG = "OrderDetailFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialog();
    }

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_check_instant_order_detail);
        View v = stub.inflate();

        mQueryTime = (TextView) v.findViewById(R.id.detail_query_time);
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_check_instant_order_header,null);
        orderTitle = (TextView) header.findViewById(R.id.order_title);
        mTotalCount = (TextView) header.findViewById(R.id.order_total_count);
        mLoadDetailList = (LoadingMoreListView) v.findViewById(R.id.detail_loading_more);
        mLoadDetailList.addHeaderView(header);
        mLoadDetailList.setOnLoadingMoreListener(this);
        initData();

        return v;
    }

    private void initData() {
        Bundle args = getActivity().getIntent().getBundleExtra(PlatformTopbarActivity.EXTRA_ARGS);
        String ordertitle = args.getString(InstantDetailListAdapter.GOODSNAME);

        isOnlyRefund = args.getString("onlyRefund");

        mGoogsId = args.getString(InstantDetailListAdapter.GOODSID);
        mStartDate = args.getString(InstantBuyFragment.STARTDATE);
        mEndDate = args.getString(InstantBuyFragment.ENDDATE);

        if(mStartDate.equals(mEndDate)){
            mQueryDate = mStartDate;
        }else{
            mQueryDate = getResources().getString(R.string.query_interval_time,mStartDate,mEndDate);
        }
        mQueryTime.setText(getResources().getString(R.string.query_time, mQueryDate));
        orderTitle.setText(ordertitle);

        orders = new ArrayList<>();

        getToolbar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadDetailList.setSelection(0);
            }
        });
    }

    @Override
    protected void requestData() {
        if(Utils.isNetworkAvailable(getActivity())){
            setContentEmpty(false);
            TransFlowCtrl.getInstantOrderDetailList(isOnlyRefund, mStartDate, mEndDate,
                    mPageIndex, mLimit, mGoogsId,
                    new Response.Listener<InstantOrderDetailModel>() {
                        @Override
                        public void onResponse(InstantOrderDetailModel model) {
                            if (null != model && isAdded()) {
                                setContentShown(true);
                                mLoadDetailList.hideFooterView();
                                if (mPageIndex == 1) {
                                    initOrderDetailView(model, false);
                                } else {
                                    initOrderDetailView(model, true);
                                }

                            }
                        }
                    }, new DialogErrorListener(){
                        @Override
                        protected void preDisposeError() {
                            super.preDisposeError();
                            setContentShown(true);
                            mLoadDetailList.hideFooterView();
                            if (mPageIndex > 1) {
                                mPageIndex--;
                            }
                        }
                    }
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError volleyError) {
//                            setContentShown(true);
//                            mLoadDetailList.hideFooterView();
//                            if (mPageIndex > 1) {
//                                mPageIndex--;
//                            }
//                            if (isShowDlg && isAdded()) {
//                                showError(volleyError);
//                            }
//                        }
//                    }
            );
        }else{
            if (isShowDlg && isAdded()) {
                mDialog.setMessage(getResources().getString(R.string.error_message_text_offline))
                        .show();
                isShowDlg = false;
            }
            setContentShown(true);
            setContentEmpty(false);
        }
    }

    @Override
    public void onLoadingMore() {
        if(orders.size() >= totalCount && isAdded()){
            Utils.showShortToast(getActivity(), getString(R.string.error_no_more_data));
            mLoadDetailList.hideFooterView();
        }else{
            mPageIndex++;
            requestData();
        }
    }

    private void initOrderDetailView(InstantOrderDetailModel model, boolean isLoadMore) {
        if(null != model.getOrderList() && model.getOrderList().size() > 0){
            totalCount = Integer.parseInt(model.getOrderList().get(0).totalCount);
            mTotalCount.setText(getString(R.string.total_acount,totalCount));
            if(isLoadMore){
                orders.addAll(model.getOrderList());
                mLoadDetailList.setSelection(orders.size());
            }else{
                orders = model.getOrderList();
                mOrderDetailAdapter = new InstantOrderDetailAdapter(getActivity(),orders,isOnlyRefund);
                mLoadDetailList.setAdapter(mOrderDetailAdapter);
            }
            mOrderDetailAdapter.notifyDataSetChanged();
        }
    }

    // FIXME: 2016/1/13 以后统一处理
    private void initDialog() {
        mDialog = new MaterialDialog(getActivity())
                .setNegativeButton(R.string.common_confirm, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                        isShowDlg = true;
                        getActivity().finish();
                    }
                });
    }

    private void showError(VolleyError error) {
        String errorInfo = error.getMessage();
        Throwable t = error.getCause();
        if (t != null) {
            if (t instanceof JSONException) {
                errorInfo = Utils.getString(R.string.error_message_unknown);
            } else if (t instanceof IOException
                    || t instanceof SocketException) {
                errorInfo = Utils.getString(R.string.error_message_network);
            }
        }
        if(errorInfo == null) {
            if(error instanceof TimeoutError) {
                errorInfo = Utils.getString(R.string.error_message_timeout);
            }else {
                errorInfo = Utils.getString(R.string.error_message_network_link);
            }
        }
        mDialog.setMessage(errorInfo)
                .show();
        isShowDlg = false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    protected Toolbar getToolbar() {
        Activity a = getActivity();
        if (a instanceof PlatformTopbarActivity) {
            return ((PlatformTopbarActivity) a).getToolbar();
        }
        return null;
    }

}
