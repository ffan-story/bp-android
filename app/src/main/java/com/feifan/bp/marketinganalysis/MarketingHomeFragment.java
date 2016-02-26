package com.feifan.bp.marketinganalysis;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.widget.ViewStubCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.util.TimeUtil;
import com.feifan.bp.widget.SegmentedGroup;
import com.feifan.material.datetimepicker.date.DatePickerDialog;

import java.util.Calendar;

/**
 * 营销管理二期
 * Created by kontar on 2016/1/29.
 */
public class MarketingHomeFragment extends ProgressFragment implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener,
        DatePickerDialog.OnDateSetListener,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MarketingHomeFragment";
    public static final String EXTRA_STARTDATE = "startTime";
    public static final String EXTRA_ENDTDATE = "endTime";
    public static final String TYPE = "type";
    public static final String TYPE_SUMMARY = "summary";
    public static final String TYPE_RED = "redbag";
    public static final String TYPE_SHAKE = "shake";
    public static final String TYPE_COMMON = "common";
    public static final String TYPE_COUPON = "coupon";
    private SwipeRefreshLayout mSwipe;
    private SegmentedGroup mSegmentGroup;
    private RadioButton mToday,mYesterday,mOther;
    private TextView mQueryTime, mChargeOffCount;
    private TextView mRedChargeCount,mShakeChargeCount,mCouponsCount,mGeneralCount;
    private int tabIndex;
    private String mStartDate,mEndDate;
    private RelativeLayout mTotalContiner,mNoNetView; //数据页 & 无网络页

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_marketing_home);
        View view = stub.inflate();
        initView(view);
        return view;
    }

    private void initView(View view) {

        mSwipe  = (SwipeRefreshLayout)view.findViewById(R.id.marketing_swipe);
        mSegmentGroup = (SegmentedGroup)view.findViewById(R.id.segmented_group);
        mToday = (RadioButton) view.findViewById(R.id.today);
        mYesterday = (RadioButton) view.findViewById(R.id.yesterday);
        mOther = (RadioButton) view.findViewById(R.id.other);
        mQueryTime = (TextView)view.findViewById(R.id.query_time);

        mChargeOffCount = (TextView) view.findViewById(R.id.chargeoff_total_count);
        mRedChargeCount = (TextView) view.findViewById(R.id.red_charge_count);
        mShakeChargeCount = (TextView) view.findViewById(R.id.shake_charge_count);
        mCouponsCount = (TextView) view.findViewById(R.id.coupons_charge_count);
        mGeneralCount = (TextView) view.findViewById(R.id.general_coupons_charge_count);

        mTotalContiner = (RelativeLayout) view.findViewById(R.id.total_coupons_container);
        mNoNetView = (RelativeLayout) view.findViewById(R.id.home_no_net);

        mOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });
        mSegmentGroup.setOnCheckedChangeListener(this);
        mSwipe.setColorSchemeResources(R.color.accent);
        mSwipe.setOnRefreshListener(this);
        view.findViewById(R.id.red_container).setOnClickListener(this);
        view.findViewById(R.id.shake_container).setOnClickListener(this);
        view.findViewById(R.id.coupons_container).setOnClickListener(this);
        view.findViewById(R.id.general_coupons_container).setOnClickListener(this);
        view.findViewById(R.id.reload).setOnClickListener(this);

        mToday.setChecked(true);
    }

    @Override
    protected void requestData() {
        if(Utils.isNetworkAvailable(getActivity())){
            mTotalContiner.setVisibility(View.VISIBLE);
            mNoNetView.setVisibility(View.GONE);
            mSwipe.setRefreshing(true);
            MarketingCtrl.getSummary(mStartDate, TimeUtil.getAddOneDay(mEndDate), TYPE_SUMMARY, new Response.Listener<MarketingSummaryModel>() {
                @Override
                public void onResponse(MarketingSummaryModel model) {
                    if (isAdded() && null != model) {
                        setContentEmpty(false);
                        setContentShown(true);
                        stopRefresh();
                        fillView(model);
                    }
                }
            });
        }else{
            stopRefresh();
            mTotalContiner.setVisibility(View.GONE);
            mNoNetView.setVisibility(View.VISIBLE);
        }
    }

    private void setQueryTime() {
        if (TimeUtil.getIntervalDays(mStartDate, mEndDate) >= 1){
            mQueryTime.setText(getString(R.string.query_when_time, mStartDate, mEndDate));
        }else{
            mQueryTime.setText(getString(R.string.query_time, mStartDate));
        }
    }

    private void stopRefresh() {
        if(mSwipe.isRefreshing()){
            mSwipe.setRefreshing(false);
        }
    }

    private void fillView(MarketingSummaryModel model) {
        mChargeOffCount.setText(getString(R.string.anal_charge_total_count, model.mHomeAllVerifyNum));
        mRedChargeCount.setText(getString(R.string.anal_charge_count, model.mHomeRedVerifyNum));
        mShakeChargeCount.setText(getString(R.string.anal_charge_count, model.mHomeShakeVerifyNum));
        mCouponsCount.setText(getString(R.string.anal_charge_count, model.mHomeCommonVerifyNum));
        mGeneralCount.setText(getString(R.string.anal_charge_count, model.mHomeCouponVerifyNum));

    }

    @Override
    public void onClick(View v) {
        String titleName = "";
        String type = "";
        Bundle args = new Bundle();
        switch (v.getId()){
            case R.id.red_container://红包
                titleName = getString(R.string.anal_red_pack);
                type = TYPE_RED;
                break;
            case R.id.shake_container://摇一摇
                titleName = getString(R.string.anal_remote);
                type = TYPE_SHAKE;
                break;
            case R.id.general_coupons_container://通用券
                titleName = getString(R.string.anal_general_coupons);
                type = TYPE_COMMON;
                args.putString(EXTRA_STARTDATE,mStartDate);
                args.putString(EXTRA_ENDTDATE, mEndDate);
                args.putString(TYPE,type);
                PlatformTopbarActivity.startActivity(getContext(), CommonSummaryFragment.class.getName(), titleName, args);
                return;
            case R.id.coupons_container://优惠券
                titleName = getString(R.string.anal_coupons);
                type = TYPE_COUPON;
                break;
            case R.id.reload:
                requestData();
                return;
        }
        args.putString(EXTRA_STARTDATE,mStartDate);
        args.putString(EXTRA_ENDTDATE, mEndDate);
        args.putString(TYPE,type);
        PlatformTopbarActivity.startActivity(getContext(), SummaryFragment.class.getName(), titleName, args);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.today:
                mStartDate = mEndDate = TimeUtil.getToday();
                if(tabIndex == 0){  //避免重复请求
                    tabIndex = R.id.today;
                }else{
                    tabIndex = R.id.today;
                    requestData();
                }
                break;
            case R.id.yesterday:
                tabIndex = R.id.yesterday;
                mStartDate = mEndDate = TimeUtil.getYesterday();
                requestData();
                break;
            case R.id.other:
                break;
        }
        setQueryTime();
    }

    private void setTabFocus(int tabIndex) {
        switch (tabIndex){
            case R.id.today:
                mToday.setChecked(true);
                break;
            case R.id.yesterday:
                mYesterday.setChecked(true);
                break;
            case R.id.other:
                mOther.setChecked(true);
                break;
        }
    }

    private void selectDate() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                MarketingHomeFragment.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setAccentColor(getResources().getColor(R.color.accent));
        dpd.setStartDateTitle(getString(R.string.date_start_text));
        dpd.setStopDateTitle(getString(R.string.date_end_text));
        dpd.setTabBackground(R.drawable.date_tab_selector);
        dpd.show(getFragmentManager(), "Datepickerdialog");
        dpd.setOnDateSetListener(this);
        dpd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                setTabFocus(tabIndex);
            }
        });
        dpd.setCancelable(false);
    }

    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        tabIndex = R.id.instant_other;
        setTabFocus(tabIndex);

        String FromDate = year + "-" + DataFormat(monthOfYear + 1) + "-" + DataFormat(dayOfMonth);
        String ToDate = yearEnd + "-" + DataFormat(monthOfYearEnd + 1) + "-" + DataFormat(dayOfMonthEnd);

        if (TimeUtil.compare_date(FromDate, TimeUtil.getToday())) {
            Toast.makeText(getActivity(), getString(R.string.date_error_1), Toast.LENGTH_LONG).show();
        } else if (TimeUtil.compare_date(ToDate, TimeUtil.getToday())) {
            Toast.makeText(getActivity(), getString(R.string.date_error_2), Toast.LENGTH_LONG).show();
        } else if (TimeUtil.compare_date(FromDate, ToDate)) {
            Toast.makeText(getActivity(), getString(R.string.date_error_3), Toast.LENGTH_LONG).show();
        } else {
            mStartDate = FromDate;
            mEndDate = ToDate;
            setQueryTime();
            requestData();
        }
    }

    private String DataFormat(int data) {
        if (data < 10) {
            return "0" + data;
        } else {
            return String.valueOf(data);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onRefresh() {
        requestData();
    }

}
