package com.feifan.bp.transactionflowing.fragment;

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
import com.feifan.bp.Statistics;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ui.ProgressFragment;
import com.feifan.bp.base.network.response.DialogErrorListener;
import com.feifan.bp.transactionflowing.TransFlowCtrl;
import com.feifan.bp.transactionflowing.model.InstantSummaryModel;
import com.feifan.bp.util.LogUtil;
import com.feifan.bp.util.NumberUtil;
import com.feifan.bp.util.TimeUtil;
import com.feifan.bp.widget.SegmentedGroup;
import com.feifan.material.MaterialDialog;
import com.feifan.material.datetimepicker.date.DatePickerDialog;
import com.feifan.statlib.FmsAgent;

import java.util.Calendar;

/**
 * Created by konta on 2016/1/7.
 */
public class InstantBuyFragment extends ProgressFragment implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener,
        SegmentedGroup.OnCheckedChangeListener {

    private static final String TAG = "InstantBuyFragment";
    public static final String STARTDATE = "startDate";
    public static final String ENDDATE = "endDate";
    private RadioButton mToday, mYesterday, mOther;
    private TextView mTradeCount, mTradeMoney, mRefundCount, mRefundMoney, mQueryTime;
    private RelativeLayout mRefundContiner;
    private SegmentedGroup mSegment;
    private SwipeRefreshLayout mSwipe;

    private String startDate;
    private String endDate;

    private String tradeCount;

    private MaterialDialog mDialog;
    private transient boolean isShowDlg = true;
    private int tabIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialog();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //统计埋点 对账管理 闪购
            FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_FINA_FLASHBUY);
        }
    }

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_check_instant_home);
        View v = stub.inflate();

        mSegment = (SegmentedGroup) v.findViewById(R.id.instant_segmentedGroup);
        mSwipe = (SwipeRefreshLayout) v.findViewById(R.id.instant_swipe);

        mToday = (RadioButton) v.findViewById(R.id.instant_today);
        mYesterday = (RadioButton) v.findViewById(R.id.instant_yesterday);
        mOther = (RadioButton) v.findViewById(R.id.instant_other);

        mOther.setOnClickListener(this);

        mQueryTime = (TextView) v.findViewById(R.id.instant_query_time);

        mTradeCount = (TextView) v.findViewById(R.id.trade_count);
        mTradeMoney = (TextView) v.findViewById(R.id.trade_money);
        mRefundContiner = (RelativeLayout) v.findViewById(R.id.refund_continer);
        mRefundCount = (TextView) v.findViewById(R.id.refund_count);
        mRefundMoney = (TextView) v.findViewById(R.id.refund_money);


        v.findViewById(R.id.trade_continer).setOnClickListener(this);
        v.findViewById(R.id.refund_continer).setOnClickListener(this);

        mSwipe.setColorSchemeResources(R.color.accent);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
            }
        });

        mToday.setChecked(true);
        tabIndex = R.id.instant_today;
        startDate = endDate = TimeUtil.getToday();
        mQueryTime.setText(getString(R.string.query_time, startDate));
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSegment.setOnCheckedChangeListener(this);
    }

    @Override
    protected void requestData() {
        if (null == getActivity()) {
            return;
        }
        if (Utils.isNetworkAvailable(getActivity())) {
            setContentEmpty(false);
            mSwipe.setRefreshing(true);
            TransFlowCtrl.getInstantSummary(startDate, endDate, new Response.Listener<InstantSummaryModel>() {
                        @Override
                        public void onResponse(InstantSummaryModel modle) {
                            if (modle != null && isAdded()) {
                                initInstantSummaryView(modle);
                                stopRefresh();
                                setContentShown(true);
                            }
                        }
                    }, new DialogErrorListener() {

                        @Override
                        protected void postDisposeError() {
                            super.postDisposeError();
                            stopRefresh();
                        }
                    }
            );
        } else {
            if (isShowDlg && isAdded()) {
                mDialog.setMessage(getResources().getString(R.string.error_message_text_offline))
                        .show();
                isShowDlg = false;
            }
            setContentShown(false);
            setContentEmpty(true);
        }
    }

    @Override
    public void onClick(View v) {
        Bundle args = new Bundle();
        switch (v.getId()) {
            case R.id.instant_other:
                selectDate();
                break;
            case R.id.trade_continer:
                if ("0".equals(tradeCount)) {
                    Utils.showShortToast(getActivity(), getString(R.string.no_data));
                    return;
                }
                args.putString(STARTDATE, startDate);
                args.putString(ENDDATE, endDate);
                args.putString("onlyRefund", "0");
                PlatformTopbarActivity.startActivity(getActivity(), InstantDetailListFragment.class.getName(),
                        getString(R.string.instant_summary), args);
                break;
            case R.id.refund_continer:
                args.putString(STARTDATE, startDate);
                args.putString(ENDDATE, endDate);
                args.putString("onlyRefund", "1");
                PlatformTopbarActivity.startActivity(getActivity(), InstantDetailListFragment.class.getName(),
                        getString(R.string.instant_summary), args);
                break;
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.instant_today:
                tabIndex = R.id.instant_today;
                startDate = endDate = TimeUtil.getToday();
                requestData();
                break;
            case R.id.instant_yesterday:
                tabIndex = R.id.instant_yesterday;
                startDate = endDate = TimeUtil.getYesterday();
                requestData();
                break;
            case R.id.instant_other:
                break;
        }
    }

    private void stopRefresh() {
        if (mSwipe.isRefreshing()) {
            mSwipe.setRefreshing(false);
        }
    }

    private void initInstantSummaryView(InstantSummaryModel modle) {
        String queryTime;
        if (startDate.equals(endDate)) {
            queryTime = startDate;
        } else {
            queryTime = getResources().getString(R.string.query_interval_time, startDate, endDate);
        }
        mQueryTime.setText(getResources().getString(R.string.query_time, queryTime));
        tradeCount = modle.getInstantSummary().tradeCount;
        mTradeCount.setText(modle.getInstantSummary().tradeCount);
        mTradeMoney.setText(NumberUtil.moneyFormat(modle.getInstantSummary().tradeMoney, 2));

        if ("0".equals(modle.getInstantSummary().refundCount)) {//没有退款
            mRefundContiner.setVisibility(View.GONE);
        } else {
            mRefundContiner.setVisibility(View.VISIBLE);
            mRefundCount.setText(modle.getInstantSummary().refundCount);
            mRefundMoney.setText(NumberUtil.moneyFormat(modle.getInstantSummary().refundMoney, 2));
        }
    }

    /**
     * 设置Tab高亮显示
     *
     * @param tabIndex
     */
    private void setTabFocus(int tabIndex) {
        switch (tabIndex) {
            case R.id.instant_today:
                mToday.setChecked(true);
                break;
            case R.id.instant_yesterday:
                mYesterday.setChecked(true);
                break;
            case R.id.instant_other:
                mOther.setChecked(true);
                break;
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
//    private void showError(VolleyError error) {
//        String errorInfo = error.getMessage();
//        Throwable t = error.getCause();
//        if (t != null) {
//            if (t instanceof JSONException) {
//                errorInfo = Utils.getString(R.string.error_message_unknown);
//            } else if (t instanceof IOException
//                    || t instanceof SocketException) {
//                errorInfo = Utils.getString(R.string.error_message_network);
//            }
//        }
//        if(errorInfo == null) {
//            if(error instanceof TimeoutError) {
//                errorInfo = Utils.getString(R.string.error_message_timeout);
//            }else {
//                errorInfo = Utils.getString(R.string.error_message_network_link);
//            }
//        }
//        mDialog.setMessage(errorInfo)
//                .show();
//        isShowDlg = false;
//    }

    private void selectDate() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                InstantBuyFragment.this,
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
        LogUtil.i("fangke", "FromDate============" + FromDate);

        String ToDate = yearEnd + "-" + DataFormat(monthOfYearEnd + 1) + "-" + DataFormat(dayOfMonthEnd);
        LogUtil.i("fangke", "ToDate============" + ToDate);

        if (TimeUtil.compare_date(FromDate, TimeUtil.getToday())) {
            Toast.makeText(getActivity(), getString(R.string.date_error_1), Toast.LENGTH_LONG).show();
        } else if (TimeUtil.compare_date(ToDate, TimeUtil.getToday())) {
            Toast.makeText(getActivity(), getString(R.string.date_error_2), Toast.LENGTH_LONG).show();
        } else if (TimeUtil.compare_date(FromDate, ToDate)) {
            Toast.makeText(getActivity(), getString(R.string.date_error_3), Toast.LENGTH_LONG).show();
        } else {
            startDate = FromDate;
            endDate = ToDate;
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


}
