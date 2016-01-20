package com.feifan.bp.transactionflow.fragment;

import android.os.Bundle;
import android.support.v7.internal.widget.ViewStubCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.transactionflow.adapter.InstantDetailListAdapter;
import com.feifan.bp.transactionflow.model.InstantDetailModel;
import com.feifan.bp.transactionflow.TransFlowCtrl;
import com.feifan.bp.widget.SpacesItemDecoration;
import com.feifan.material.MaterialDialog;

import org.json.JSONException;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;

/**
 * Created by konta on 2016/1/12.
 */
public class InstantDetailListFragment extends ProgressFragment{

    private TextView mQueryTime;
    private RecyclerView mDetailList;

    private Bundle args;

    private String startDate;
    private String endDate;
    private String mQueryDate;
    private int pageIndex = 1;
    private int limit = 20;

    private MaterialDialog mDialog;
    private transient boolean isShowDlg = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialog();
    }

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_check_instant_detail);
        View v = stub.inflate();

        mQueryTime = (TextView) v.findViewById(R.id.detail_query_time);
        mDetailList = (RecyclerView) v.findViewById(R.id.detail_list);
        mDetailList.setLayoutManager(new LinearLayoutManager(getActivity()));

        SpacesItemDecoration space = new SpacesItemDecoration(8);
        mDetailList.addItemDecoration(space);
        initData();

        return v;
    }

    private void initData() {
        args = getActivity().getIntent().getBundleExtra(PlatformTopbarActivity.EXTRA_ARGS);
        startDate = args.getString("startDate");
        endDate = args.getString("endDate");
        if(startDate.equals(endDate)){
            mQueryDate = startDate;
        }else{
            mQueryDate = getString(R.string.query_interval_time,startDate,endDate);
        }
        mQueryTime.setText(getResources().getString(R.string.query_time, mQueryDate));

    }

    @Override
    protected void requestData() {
        if(Utils.isNetworkAvailable(getActivity())){
            setContentEmpty(false);
            TransFlowCtrl.getInstantDetailList(startDate, endDate, pageIndex, limit, new Response.Listener<InstantDetailModel>() {
                @Override
                public void onResponse(InstantDetailModel modle) {
                    if (modle != null && isAdded()) {
                        initInstantDetailListView(modle);
                        setContentShown(true);
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
            setContentShown(false);
            setContentEmpty(true);
        }
    }

    private void initInstantDetailListView(InstantDetailModel model) {
        List<InstantDetailModel.InstantDetail> details = model.getInstantDetailList();
        if(null != details){
            mDetailList.setAdapter(new InstantDetailListAdapter(getActivity(),details,args));
        }
    }

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

    // FIXME: 2016/1/13 以后统一处理
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
