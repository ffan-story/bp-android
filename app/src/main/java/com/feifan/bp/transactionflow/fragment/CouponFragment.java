package com.feifan.bp.transactionflow.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.widget.ViewStubCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.transactionflow.adapter.CouponListAdapter;
import com.feifan.bp.transactionflow.model.CouponSummaryModel;
import com.feifan.bp.transactionflow.TransFlowCtrl;
import com.feifan.bp.util.TimeUtil;
import com.feifan.bp.widget.MonPicker;
import com.feifan.bp.widget.paginate.Paginate;
import com.feifan.material.MaterialDialog;

import org.json.JSONException;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * 通用券
 * Created by Frank on 15/11/6.
 */
public class CouponFragment extends ProgressFragment implements RadioGroup.OnCheckedChangeListener,
        SwipeRefreshLayout.OnRefreshListener,Paginate.Callbacks {

    private static final String TAG = "CouponFragment";

//    private SegmentedGroup mSegmentGroup;
//    private RadioButton mLast1,mLast2, mOther;
//    private TextView mChargeoffTotal,mAwardAmount,mLinkRelative;
    private int tabIndex;

    private MaterialDialog mDialog;
    private MonPicker picker;
    private String selectData;
    private boolean isShowDlg;
    private CouponListAdapter adapter;
    private MaterialDialog mErrorDialog;
    private SwipeRefreshLayout mSwipeLayout;
    private RecyclerView mRecyclerView;
    private List<CouponSummaryModel.CouponDetail> couponList;

//    private TextView mQueryTime;

    private static int mIntYear;
    private static int mIntMonth;
    private int mPageIndex = 1,mLimit = 10,mTotalCount;
    private boolean loading = false;
    private Paginate paginate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialog();
    }

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {

        stub.setLayoutResource(R.layout.fragment_check_coupon_summary);
        View v = stub.inflate();

        initView(v);
        initData();

        return v;
    }

    private void initView(View v) {

        mSwipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.coupon_swipe);
        mSwipeLayout.setColorSchemeResources(R.color.accent);
        mSwipeLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.coupon_recyler);
        int layoutOrientation = OrientationHelper.VERTICAL;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), layoutOrientation, false);
        ((LinearLayoutManager) layoutManager).setReverseLayout(false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new SlideInUpAnimator());

//        mSegmentGroup = (SegmentedGroup) v.findViewById(R.id.coupon_segmentedGroup);
//        mLast1 = (RadioButton) v.findViewById(R.id.coupon_last1);
//        mLast2 = (RadioButton) v.findViewById(R.id.coupon_last2);
//        mOther = (RadioButton) v.findViewById(R.id.coupon_other);
//        mOther.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectDate(mIntYear, mIntMonth);
//            }
//        });
//        mQueryTime = (TextView) v.findViewById(R.id.coupon_query_time);
//        mChargeoffTotal = (TextView)v.findViewById(R.id.chargeoff_count);
//        mAwardAmount = (TextView)v.findViewById(R.id.award_money_count);
//        mLinkRelative = (TextView)v.findViewById(R.id.link_relative);

    }

    private void initData() {
        tabIndex = R.id.last1;
//        mLast1.setChecked(true);
        selectData = TimeUtil.getLastMonth();
//        mQueryTime.setText(getString(R.string.query_time, selectData));
    }

    @Override
    public void onResume() {
        super.onResume();
//        mSegmentGroup.setOnCheckedChangeListener(this);
    }

    @Override
    protected void requestData() {
        setupPagination();
    }

    private void setupPagination() {
        if(paginate != null){
            paginate.unbind();
        }

        getCouponsDatas();
    }

    private void getCouponsDatas() {
        if(Utils.isNetworkAvailable(getActivity())){
            setContentEmpty(false);
            mSwipeLayout.setRefreshing(true);
            TransFlowCtrl.getCouponSummary(selectData, mPageIndex, mLimit, new Response.Listener<CouponSummaryModel>() {
                @Override
                public void onResponse(CouponSummaryModel model) {
                    if (null != model && isAdded()) {
                        initCouponView(model, false);
                        mSwipeLayout.setRefreshing(false);
                        setContentShown(true);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    stopRefresh();
                    setContentShown(true);
                    if (isShowDlg && isAdded()) {
                        showError(volleyError);
                        mSwipeLayout.setRefreshing(false);
                    }
                }
            });
        }else{
            Log.e(TAG, "网络不可用");
            if (isShowDlg && isAdded()) {
                mDialog.setMessage(getResources().getString(R.string.error_message_text_offline))
                        .show();
                isShowDlg = false;
            }
            setContentShown(false);
            setContentEmpty(true);
        }
    }

    private void initCouponView(CouponSummaryModel model,Boolean isLoadMore) {
//        mQueryTime.setText(getResources().getString(R.string.query_time, selectData));
        if(null != model.getCouponSummary()){
//            mChargeoffTotal.setText(model.getCouponSummary().totalCount + "");
//            mAwardAmount.setText(NumberUtil.moneyFormat(model.getCouponSummary().awardAmount+"",2));
//            mLinkRelative.setText(getString(R.string.coupon_link_relative, model.getCouponSummary().linkRelative));
            mTotalCount = model.getCouponSummary().totalCount;
            couponList = model.getCouponSummary().couponDetailList;
            if(isLoadMore){
                couponList.addAll(model.getCouponSummary().couponDetailList);
                adapter.notifyDataSetChanged();
            }else{
                adapter = new CouponListAdapter(getActivity(),couponList);
                mRecyclerView.setAdapter(adapter);
                paginate = Paginate.with(mRecyclerView,CouponFragment.this)
                        .setLoadingTriggerThreshold(0)
                        .addLoadingListItem(true)
                        .build();
            }
            paginate.setHasMoreDataToLoad(!hasLoadedAllItems());
        }

    }

    @Override
    public void onRefresh() {
        Utils.showShortToast(getActivity(),"refreshing...");
        requestData();
    }

    private void stopRefresh() {
        if (mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }

    @Override
    public void onLoadMore() {
        Log.e(TAG,"onLoadMore----------");
        loading = true;
        mPageIndex++;
        getCouponsDatas();
    }

    @Override
    public boolean isLoading() {
        Log.e(TAG, "isLoading----------");
        return loading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        Log.e(TAG, "hasLoadedAllItems----------"+ mTotalCount + "====" + couponList.size());
        return true;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.coupon_last1://上期
                tabIndex = R.id.coupon_last1;
                selectData = TimeUtil.getLastMonth();
                requestData();
                break;
            case R.id.coupon_last2://上上期
                tabIndex = R.id.coupon_last2;
                selectData = TimeUtil.getLast2Month();
                requestData();
                break;
            case R.id.coupon_other:

                break;
        }
    }

    private void selectDate(final int year , final int month) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_month_pick, null);
        picker = (MonPicker) view.findViewById(R.id.month_picker);
        if (year !=0 && month !=0){
            picker.init(year, month, 1, null);
        }
        mDialog = new MaterialDialog(getActivity()).setContentView(view)
                .setPositiveButton(R.string.date_self_define_confirm, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectData = picker.getYear() + "-" + DataFormat(picker.getMonth()+1);
                        mIntYear =  picker.getYear();
                        mIntMonth = picker.getMonth()+1;
                        Log.e(TAG,selectData);
                        requestData();
                        mDialog.dismiss();
                        tabIndex = R.id.other;
                        setTabFocus(tabIndex);
                    }
                })
                .setNegativeButton(R.string.date_self_define_cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mIntYear =  picker.getYear();
                        mIntMonth = picker.getMonth()+1;
                        mDialog.dismiss();
                        setTabFocus(tabIndex);
                    }
                });
        mDialog.show();
    }

    private void setTabFocus(int tabIndex) {
        switch (tabIndex){
            case R.id.coupon_last1:
//                mLast1.setChecked(true);
                break;
            case R.id.coupon_last2:
//                mLast2.setChecked(true);
                break;
            case R.id.coupon_other:
//                mOther.setChecked(true);
                selectDate(0,0);
                break;
        }
    }

    private String DataFormat(int data) {
        if (data < 10) {
            return "0" + data;
        } else {
            return String.valueOf(data);
        }
    }


    // FIXME: 2016/1/13 以后统一处理
    private void initDialog() {
        mErrorDialog = new MaterialDialog(getActivity())
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
