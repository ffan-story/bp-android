package com.feifan.bp.transactionflow.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.widget.ViewStubCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feifan.bp.PlatformTabActivity;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.transactionflow.TransFlowCtrl;
import com.feifan.bp.transactionflow.adapter.CouponListViewAdapter;
import com.feifan.bp.transactionflow.model.CouponSummaryModel;
import com.feifan.bp.util.NumberUtil;
import com.feifan.bp.util.TimeUtil;
import com.feifan.bp.widget.LoadingMoreListView;
import com.feifan.bp.widget.MonPicker;
import com.feifan.bp.widget.OnLoadingMoreListener;
import com.feifan.bp.widget.SegmentedGroup;
import com.feifan.material.MaterialDialog;

import org.json.JSONException;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;

/**
 * 通用券
 * Created by kontar on 16/1/19.
 */
public class CouponListViewFragment extends ProgressFragment implements RadioGroup.OnCheckedChangeListener,
        SwipeRefreshLayout.OnRefreshListener, OnLoadingMoreListener, PlatformTabActivity.onPageSelectListener{

    private static final String TAG = "CouponFragment";

    private SegmentedGroup mSegmentGroup;
    private RadioButton mLast1,mLast2, mOther;
    private TextView mChargeoffTotal,mAwardAmount,mLinkRelative,mDetailTotalCount;
    private ImageView relativeArrow;
    private int tabIndex;

    private MaterialDialog mErrorDialog;
    private MaterialDialog mDialog;
    private MonPicker picker;
    private String selectData;
    private boolean isShowDlg;
    private boolean shouldRequest = false;
    private CouponListViewAdapter adapter;
    private SwipeRefreshLayout mSwipeLayout;
    private List<CouponSummaryModel.CouponDetail> couponList;

    private TextView mQueryTime;

    private static int mIntYear;
    private static int mIntMonth;
    private int mPageIndex = 1,mLimit = 10;
    /**
     * 总条数
     */
    private int mDetailCount;
    private LoadingMoreListView load;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialog();
    }

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        ((PlatformTabActivity) getActivity()).setOnPageSelectListener(this);
        stub.setLayoutResource(R.layout.fragment_check_coupon_list);
        View v = stub.inflate();

        load = (LoadingMoreListView) v.findViewById(R.id.load_coupon_list);

        View header = View.inflate(getActivity(),R.layout.fragment_check_coupons,null);
        load.addHeaderView(header);
        load.setOnLoadingMoreListener(this);

        initView(v, header);
        initData();
        return v;
    }

    private void initView(View v, View header) {
        mSwipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_coupon_list);
        mSwipeLayout.setColorSchemeResources(R.color.accent);

        mSwipeLayout.setOnRefreshListener(this);

        mSegmentGroup = (SegmentedGroup) header.findViewById(R.id.coupon_segmentedGroup);
        mLast1 = (RadioButton) header.findViewById(R.id.coupon_last1);
        mLast2 = (RadioButton) header.findViewById(R.id.coupon_last2);
        mOther = (RadioButton) header.findViewById(R.id.coupon_other);
        mOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(mIntYear, mIntMonth);
            }
        });
        mQueryTime = (TextView) header.findViewById(R.id.coupon_query_time);
        mChargeoffTotal = (TextView)header.findViewById(R.id.chargeoff_count);
        mAwardAmount = (TextView)header.findViewById(R.id.award_money_count);
        mLinkRelative = (TextView)header.findViewById(R.id.link_relative);
        mDetailTotalCount = (TextView) header.findViewById(R.id.coupon_total_count);
        relativeArrow = (ImageView) header.findViewById(R.id.coupon_relative_arrow);

        getToolbar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load.setSelection(0);
            }
        });

    }

    private void initData() {
        mLast1.setChecked(true);
        tabIndex = R.id.coupon_last1;
        selectData = TimeUtil.getLastMonth();
        mQueryTime.setText(getString(R.string.query_time, selectData));

    }

    @Override
    public void onResume() {
        super.onResume();
        mSegmentGroup.setOnCheckedChangeListener(this);
    }

    @Override
    protected void requestData() {
        if(shouldRequest){
            isShowDlg = true;
            if(Utils.isNetworkAvailable(getActivity())){
                setContentEmpty(false);
                mSwipeLayout.setRefreshing(true);
                TransFlowCtrl.getCouponSummary(selectData, mPageIndex, mLimit, new Response.Listener<CouponSummaryModel>() {
                    @Override
                    public void onResponse(CouponSummaryModel model) {
                        if (null != model && isAdded()) {
                            if (mPageIndex == 1) {
                                initCouponView(model, false);
                            } else {
                                initCouponView(model, true);
                            }
                            load.hideFooterView();
                            stopRefresh();
                            setContentShown(true);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        setContentShown(true);
                        load.hideFooterView();
                        load.setAdapter(null);
                        if (mPageIndex > 1) {
                            mPageIndex--;
                        }
                        if (isShowDlg && isAdded()) {
                            showError(volleyError);
                            stopRefresh();
                        }
                    }
                });
            }else{
                if (isShowDlg && isAdded()) {
                    mErrorDialog.setMessage(getResources().getString(R.string.error_message_text_offline))
                            .show();
                    isShowDlg = false;
                }
                setContentShown(false);
                setContentEmpty(true);
            }

        }
    }

    private void initCouponView(CouponSummaryModel model,Boolean isLoadMore) {
        mQueryTime.setText(getResources().getString(R.string.query_time,selectData ));
        if(null != model.getCouponSummary()){
            mChargeoffTotal.setText(model.getCouponSummary().totalCount + "");
            mAwardAmount.setText(NumberUtil.moneyFormat(model.getCouponSummary().awardAmount + "", 2));
            String linkNum = model.getCouponSummary().linkRelative;
            if(linkNum.contains("-")){
                mLinkRelative.setText(getString(R.string.coupon_link_relative, linkNum));
                relativeArrow.setImageResource(R.mipmap.relative_down);
            }else{
                mLinkRelative.setText(getString(R.string.coupon_link_relative, "+" + linkNum));
                relativeArrow.setImageResource(R.mipmap.relative_up);
            }

            mDetailCount = model.getCouponSummary().totalCount;
            mDetailTotalCount.setText(getString(R.string.coupon_total_acount, mDetailCount + ""));

            if(isLoadMore){
                couponList.addAll(model.getCouponSummary().couponDetailList);
            }else{
                couponList = model.getCouponSummary().couponDetailList;
                adapter = new CouponListViewAdapter(getActivity(),couponList);
                load.setAdapter(adapter);
            }
            adapter.notifyDataSetChanged();
        }

    }

    private void stopRefresh() {
        if (mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        requestData();
    }

    @Override
    public void onLoadingMore() {
        if(couponList.size() >= mDetailCount && isAdded()){
            Utils.showShortToast(getActivity(),getString(R.string.error_no_more_data));
            load.hideFooterView();
        }else{
            mPageIndex++;
            requestData();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.coupon_last1://上期
                tabIndex = R.id.coupon_last1;
                mPageIndex = 1;
                selectData = TimeUtil.getLastMonth();
                requestData();
                break;
            case R.id.coupon_last2://上上期
                tabIndex = R.id.coupon_last2;
                mPageIndex = 1;
                selectData = TimeUtil.getLast2Month();
                requestData();
                break;
            case R.id.coupon_other:
                mPageIndex = 1;
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
                        setTabFocus(tabIndex);
                        mDialog.dismiss();
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
                        mErrorDialog.dismiss();
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
        mErrorDialog.setMessage(errorInfo)
                .show();
        isShowDlg = false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    protected Toolbar getToolbar() {
        Activity a = getActivity();
        if (a instanceof PlatformTabActivity) {
            return ((PlatformTabActivity) a).getToolbar();
        }
        return null;
    }

    @Override
    public void onPageSelected() {
        shouldRequest = !shouldRequest;
        requestData();
    }
}
