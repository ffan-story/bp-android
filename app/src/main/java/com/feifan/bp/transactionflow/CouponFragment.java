package com.feifan.bp.transactionflow;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.widget.ViewStubCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.widget.LoadingMoreListView;
import com.feifan.bp.widget.MonPicker;
import com.feifan.bp.widget.OnLoadingMoreListener;
import com.feifan.bp.widget.SegmentedGroup;
import com.feifan.material.MaterialDialog;

import org.json.JSONException;

import java.io.IOException;
import java.net.SocketException;

/**
 * 通用券
 * Created by Frank on 15/11/6.
 */
public class CouponFragment extends ProgressFragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, OnLoadingMoreListener {

    private static final String TAG = "CouponFragment";

    private SegmentedGroup mSegmentGroup;
    private RadioButton mLast1,mLast2, mOther;
    private TextView mChargeoffTotal,mAwardAmount,mLinkRelative;
    private SwipeRefreshLayout mSwipe;
    private LoadingMoreListView mCouponsList;
    private RecyclerView mDetailList;
    private int tabIndex;

    private MaterialDialog mDialog;
    private MonPicker picker;
    private String selectData;
    private String type;

    private boolean isShowDlg;

    private static int mIntYear;
    private static int mIntMonth;
    private MaterialDialog mErrorDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
//        stub.setLayoutResource(R.layout.fragment_swipe_load);
//        View v = stub.inflate();

        View v = View.inflate(getActivity(),R.layout.fragment_swipe_load,null);
        Log.e(TAG,"CouponFragment ----" + getActivity() + "1111");
        View header = View.inflate(getActivity(), R.layout.fragment_check_coupons, null);

        mCouponsList = (LoadingMoreListView) v.findViewById(R.id.load);
        mCouponsList.addHeaderView(header);
        mCouponsList.setOnLoadingMoreListener(this);

        mSegmentGroup = (SegmentedGroup) v.findViewById(R.id.coupon_segmentedGroup);
        mChargeoffTotal = (TextView) v.findViewById(R.id.chargeoff_count);
        mAwardAmount = (TextView) v.findViewById(R.id.award_money_count);
        mLinkRelative = (TextView) v.findViewById(R.id.link_relative);

        mSwipe = (SwipeRefreshLayout) v.findViewById(R.id.swipe);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
            }
        });

        mLast1 = (RadioButton) v.findViewById(R.id.coupon_last1);
        mLast2 = (RadioButton) v.findViewById(R.id.coupon_last2);
        mOther = (RadioButton) v.findViewById(R.id.coupon_other);
        mOther.setOnClickListener(this);

        tabIndex = R.id.last1;
        mLast1.setChecked(true);
        selectData = "2015-11";
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSegmentGroup.setOnCheckedChangeListener(this);
    }

    @Override
    protected void requestData() {
        if(Utils.isNetworkAvailable(getActivity())){
            setContentEmpty(false);
            mSwipe.setRefreshing(true);
            TransFlowCtrl.getCouponSummary(type,selectData,1,10, new Response.Listener<CouponSummaryModel>() {
                @Override
                public void onResponse(CouponSummaryModel model) {

                    if(null != model && isAdded()){
                        initCouponView(model);
                        mSwipe.setRefreshing(false);
                        setContentShown(true);
                        Log.e(TAG, "setContentShown ====" + isContentShown());
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    setContentShown(true);
                    if(isShowDlg && isAdded()){
                        showError(volleyError);
                        stopRefresh();
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

    private void initCouponView(CouponSummaryModel model) {


        mCouponsList.setAdapter(new CouponListAdapter(getActivity()));

//        if(null != model.getCouponSummary()){
//            mChargeoffTotal.setText(model.getCouponSummary().totalCount + "");
//            Log.e(TAG, "CouponFragment------" + model.getCouponSummary().totalCount + "");
//            mAwardAmount.setText(model.getCouponSummary().awardAmount + "");
//            mLinkRelative.setText(model.getCouponSummary().linkRelative);
//            mDetailList.setAdapter(new DetailListAdapter(model.getCouponSummary().couponDetailList));
//        }
    }

    private void stopRefresh() {
        if (mSwipe.isRefreshing()) {
            mSwipe.setRefreshing(false);
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.coupon_last1:
                type = "1";
                requestData();
                break;
            case R.id.coupon_last2:
                type = "2";
                requestData();
                break;
            case R.id.coupon_other:

                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.coupon_other:
                selectDate(mIntYear, mIntMonth);
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
                        type = "";
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
                mLast1.setChecked(true);
                break;
            case R.id.coupon_last2:
                mLast2.setChecked(true);
                break;
            case R.id.coupon_other:
                mOther.setChecked(true);
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

    @Override
    public void onLoadingMore() {

    }
}
