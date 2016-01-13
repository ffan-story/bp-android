package com.feifan.bp.transactionflow;

import android.os.Bundle;
import android.support.v7.internal.widget.ViewStubCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.widget.LoadingMoreListView;
import com.feifan.bp.widget.OnLoadingMoreListener;
import com.feifan.material.MaterialDialog;

import org.json.JSONException;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by konta on 2016/1/14.
 */
public class InstantOrderDetailFragment extends ProgressFragment implements OnLoadingMoreListener {
    private LoadingMoreListView mLoadDetailList;
    private TextView mQueryTime,mTotalCount,orderTitle;

    private String startDate;
    private String endDate;
    private String mQueryDate;
    private int pageIndex = 1;
    private int limit = 10;
    private String googsId;

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
        orderTitle = (TextView) v.findViewById(R.id.order_title);
        mTotalCount = (TextView) v.findViewById(R.id.order_total_count);
        mLoadDetailList = (LoadingMoreListView) v.findViewById(R.id.detail_loading_more);
        mLoadDetailList.setOnLoadingMoreListener(this);
        initData();

        return v;
    }

    private void initData() {
        Bundle args = getActivity().getIntent().getBundleExtra(PlatformTopbarActivity.EXTRA_ARGS);
        String ordertitle = args.getString(InstantDetailListAdapter.GOODSNAME);
        googsId = args.getString(InstantDetailListAdapter.GOODSID);
        startDate = args.getString(InstantBuyFragment.STARTDATE);
        endDate = args.getString(InstantBuyFragment.ENDDATE);

        if(startDate.equals(endDate)){
            mQueryDate = startDate;
        }else{
            mQueryDate = startDate + "至" + endDate;
        }
        mQueryTime.setText(getResources().getString(R.string.query_time, mQueryDate));
        orderTitle.setText(ordertitle);

    }

    @Override
    protected void requestData() {
        if(Utils.isNetworkAvailable(getActivity())){
            setContentEmpty(false);
            TransFlowCtrl.getInstantOrderDetailList(startDate, endDate,
                    pageIndex, limit, googsId,
                    new Response.Listener<InstantOrderDetailModel>() {
                @Override
                public void onResponse(InstantOrderDetailModel model) {
                    if(null != model && isAdded()){
                        setContentShown(true);
                        initOrderDetailView(model);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    setContentShown(true);
                    if (isShowDlg && isAdded()) {
                        showError(volleyError);
                    }
                }
            });
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
        requestData();
    }

    private void initOrderDetailView(InstantOrderDetailModel model) {
        orders = new ArrayList<>();
        if(null != model.getOrderList()){
            orders.addAll(model.getOrderList());
            Log.e(TAG,"orders" + orders.size());
            mTotalCount.setText("总条数 : " + orders.get(1).totalCount);
            mOrderDetailAdapter = new InstantOrderDetailAdapter(getActivity(),orders);
            mLoadDetailList.setAdapter(new InstantOrderDetailAdapter(getContext(), orders));
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
        mDialog.setMessage(errorInfo)
                .show();
        isShowDlg = false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

}
