package com.feifan.bp.transactionflow;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.home.code.CodeQueryResultFragment;
import com.feifan.bp.salesmanagement.IndexSalesManageFragment;
import com.feifan.bp.util.LogUtil;
import com.feifan.bp.util.TimeUtil;
import com.feifan.bp.widget.SegmentedGroup;
import com.feifan.material.MaterialDialog;
import com.feifan.material.datetimepicker.date.DatePickerDialog;
import com.feifan.statlib.NetworkUtil;

import org.apache.http.protocol.RequestDate;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.SocketException;
import java.util.Calendar;

/**
 * Created by konta on 2016/1/7.
 */
public class InstantBuyFragment extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, DatePickerDialog.OnDateSetListener {

    private static final String TAG = "InstantBuyFragment";
    private RadioButton mToday, mYesterday, mOther;
    private TextView mTradeCount,mTradeMoney,mRefundCount,mRefundMoney,mQueryTime;
    RelativeLayout mRefundContiner;
    SwipeRefreshLayout mSwipe;

    private String startDate;
    private String endDate;

    private MaterialDialog mDialog;
    private transient boolean isShowDlg = true;
    private int tabIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_instant_swip_rerefresh,null);

        mSwipe = (SwipeRefreshLayout) v.findViewById(R.id.instant_swipe);

        SegmentedGroup mSegmentedGroup = (SegmentedGroup)v.findViewById(R.id.instant_segmentedGroup);
        mToday = (RadioButton) v.findViewById(R.id.instant_today);
        mYesterday = (RadioButton) v.findViewById(R.id.instant_yesterday);
        mOther = (RadioButton) v.findViewById(R.id.instant_other);

        mOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });

        mQueryTime = (TextView) v.findViewById(R.id.instant_query_time);
        mTradeCount = (TextView) v.findViewById(R.id.trade_count);
        mTradeMoney = (TextView) v.findViewById(R.id.trade_money);
        mRefundContiner = (RelativeLayout) v.findViewById(R.id.refund_continer);
        mRefundCount = (TextView) v.findViewById(R.id.refund_count);
        mRefundMoney = (TextView) v.findViewById(R.id.refund_money);


        v.findViewById(R.id.trade_continer).setOnClickListener(this);
        v.findViewById(R.id.refund_continer).setOnClickListener(this);
        mSegmentedGroup.setOnCheckedChangeListener(this);

        mSwipe.setColorSchemeResources(R.color.accent);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getInstantSummary();
            }
        });

        mToday.setChecked(true);
        tabIndex = R.id.instant_today;
        return v;
    }

    @Override
    public void onClick(View v) {
        Bundle args = new Bundle();
        switch (v.getId()){
            case R.id.trade_continer:
                Utils.showShortToast(getContext(), "交易明细");
                args.putString("startDate", startDate);
                args.putString("endDate", endDate);

                break;
            case R.id.refund_continer:
                Utils.showShortToast(getContext(), "退款明细");
                break;
        }
        PlatformTopbarActivity.startActivity(getActivity(), InstantDetailListFragment.class.getName(),
                "商品明细", args);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.instant_today:
                tabIndex = R.id.instant_today;
                startDate = endDate = TimeUtil.getToday();
                getInstantSummary();
                break;
            case R.id.instant_yesterday:
                tabIndex = R.id.instant_yesterday;
                startDate = endDate = TimeUtil.getYesterday();
                getInstantSummary();
                break;
            case R.id.instant_other:
//                selectDate();
                break;
        }
    }

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
    }

    private void getInstantSummary() {
        if(Utils.isNetworkAvailable(getActivity())){
            showProgressBar(true);
            TransFlowCtrl.getInstantSummary(startDate, endDate, new Response.Listener<InstantSummaryModle>() {
                @Override
                public void onResponse(InstantSummaryModle modle) {
                    if(modle != null && isAdded()){
                        initInstantSummaryView(modle);
                        stopRefresh();
                        hideProgressBar();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if(isShowDlg && isAdded()){
                        showError(volleyError);
                        stopRefresh();
                        hideProgressBar();
                    }
                }
            });
        }else {
            if (isShowDlg && isAdded()) {
                mDialog.setMessage(getResources().getString(R.string.error_message_text_offline))
                        .show();
                isShowDlg = false;
            }
        }

    }

    private void stopRefresh() {
        if (mSwipe.isRefreshing()) {
            mSwipe.setRefreshing(false);
        }
    }

    private void initInstantSummaryView(InstantSummaryModle modle) {
        String queryTime;
        if(startDate == endDate){
            queryTime = startDate;
        }else{
            queryTime = startDate + "至" + endDate;
        }
        mQueryTime.setText("查询时间:" + queryTime);
        mTradeCount.setText(modle.getInstantSummary().tradeCount);
        mTradeMoney.setText(modle.getInstantSummary().tradeMoney);

        mRefundCount.setText(modle.getInstantSummary().refundCount);
        mRefundMoney.setText(modle.getInstantSummary().refundMoney);

//        if("0".equals(modle.getInstantSummary().refundCount)){
//            mRefundContiner.setVisibility(View.GONE);
//        }else{
//            mRefundCount.setText(modle.getInstantSummary().refundCount);
//            mRefundMoney.setText(modle.getInstantSummary().refundMoney);
//        }
    }

    /**
     * 设置Tab高亮显示
     * @param tabIndex
     */
    private void setTabFocus(int tabIndex) {
        switch (tabIndex){
            case R.id.instant_today:
                mToday.setChecked(true);
                break ;
            case R.id.instant_yesterday:
                mYesterday.setChecked(true);
                break ;
            case R.id.instant_other:
                mOther.setChecked(true);
                break;
        }
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
//        super.setupToolbar(toolbar);
        toolbar.setTitle(R.string.transaction_flow);
        toolbar.setNavigationIcon(R.mipmap.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showShortToast(getContext(), "点击一下");
            }
        });
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
        if("message".equals(errorInfo)){
            errorInfo = Utils.getString(R.string.error_message_server_file);
        }
        mDialog.setMessage(errorInfo)
                .show();
        isShowDlg = false;
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

            getInstantSummary();
        }
    }

    private String DataFormat(int data) {
        if (data < 10) {
            return "0" + data;
        } else {
            return String.valueOf(data);
        }
    }

}
