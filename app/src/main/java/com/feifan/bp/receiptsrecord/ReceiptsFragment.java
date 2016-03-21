package com.feifan.bp.receiptsrecord;

import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.widget.ViewStubCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.feifan.bp.PlatformState;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.network.response.ToastErrorListener;
import com.feifan.bp.util.TimeUtil;
import com.feifan.bp.util.ToastUtil;
import com.feifan.bp.widget.SegmentedGroup;
import com.feifan.bp.widget.paginate.Paginate;
import com.feifan.material.datetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.List;

/**
 * 收款流水
 * Created by konta on 2016/3/17.
 */
public class ReceiptsFragment extends ProgressFragment implements DatePickerDialog.OnDateSetListener,
        RadioGroup.OnCheckedChangeListener ,
        SwipeRefreshLayout.OnRefreshListener,
        Paginate.Callbacks{

    private SegmentedGroup mSegmentedGroup;
    private RadioButton btn1, btn2, btn3;
    private TextView mQueryTime;
    private int tabIndex,pageIndex;
    private SwipeRefreshLayout mSwipe;
    private ListView receiptsList;
    private String mStartDate,mEndDate;
    private ReceiptsAdapter mAdapter;
    private List<ReceiptsModel.ReceiptsRecord> mReceiptsList;
    private Paginate mPaginate;
    private boolean isLoading;
    private RelativeLayout mNoDataView,mNoNetView; //无数据 & 无网络页

    //打开收款流水页
    public static void start(){
        PlatformTopbarActivity.startActivityFromOther(PlatformState.getApplicationContext(), ReceiptsFragment.class.getName(), "收款流水");
    }

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_receipts);
        View view = stub.inflate();
        mSegmentedGroup = (SegmentedGroup) view.findViewById(R.id.segmented_group);
        btn1 = (RadioButton) view.findViewById(R.id.btn1);
        btn2 = (RadioButton) view.findViewById(R.id.btn2);
        btn3 = (RadioButton) view.findViewById(R.id.btn3);
        mQueryTime = (TextView)view.findViewById(R.id.query_time);
        mSegmentedGroup.setOnCheckedChangeListener(this);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });
        mSwipe = (SwipeRefreshLayout) view.findViewById(R.id.date_select_swipe);
        mSwipe.setColorSchemeColors(R.color.accent);
        mSwipe.setOnRefreshListener(this);
        receiptsList  = (ListView) view.findViewById(R.id.receipts_list);
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.no_data_net_view,null,false);
        mNoDataView = (RelativeLayout) header.findViewById(R.id.no_data);
        mNoNetView = (RelativeLayout) header.findViewById(R.id.no_net);
        mNoDataView.setVisibility(View.GONE);
        mNoNetView.setVisibility(View.GONE);
        receiptsList.addHeaderView(header);
        header.findViewById(R.id.reload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData();
            }
        });
        btn1.setChecked(true);
        return view;
    }

    @Override
    protected void requestData() {
        pageIndex = 0;
        fetchData(false);
    }

    private void fetchData(final boolean isLoadMore) {
        if(Utils.isNetworkAvailable(getActivity())) {
            mSwipe.setRefreshing(true);
            mNoNetView.setVisibility(View.GONE);
            ReceiptsCtrl.getReceiptsRecords(mStartDate, mEndDate, pageIndex + "", new Response.Listener<ReceiptsModel>() {
                @Override
                public void onResponse(ReceiptsModel model) {
                    if (isAdded() && null != model) {
                        stopRefresh();
                        setContentEmpty(false);
                        setContentShown(true);
                        fillView(model, isLoadMore);
                    }
                }
            }, new ToastErrorListener());
        }else{
            receiptsList.setAdapter(null);
            mNoDataView.setVisibility(View.GONE);
            mNoNetView.setVisibility(View.VISIBLE);
            stopRefresh();
        }
    }

    private void fillView(ReceiptsModel model, boolean isLoadMore){
        mReceiptsList = model.receiptsList;
        if(null != mReceiptsList && mReceiptsList.size() > 0){ // 有数据
            mNoDataView.setVisibility(View.GONE);
        }else if(!isLoadMore){
            receiptsList.setAdapter(null);
            mNoDataView.setVisibility(View.VISIBLE);
            return;
        }
        if(isLoadMore){
            if(null != mAdapter){
                mAdapter.notifyData(mReceiptsList);
            }
        }else {
            mAdapter = new ReceiptsAdapter(getActivity(),mReceiptsList);
            receiptsList.setAdapter(mAdapter);
            mPaginate = Paginate.with(receiptsList,ReceiptsFragment.this)
                    .setLoadingTriggerThreshold(0)
                    .addLoadingListItem(true)
                    .build();
        }
        mPaginate.setHasMoreDataToLoad(!hasLoadedAllItems());
    }

    @Override
    public void onRefresh() {
        requestData();
    }

    private void stopRefresh() {
        if(mSwipe.isRefreshing()){
            mSwipe.setRefreshing(false);
        }
    }

    @Override
    public void onLoadMore() {
        isLoading = true;
        pageIndex++;
        fetchData(true);
    }

    @Override
    public void hasLoadMore() {
        ToastUtil.showToast(getActivity(), getString(R.string.anal_no_more_data));
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return pageIndex == 3;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.btn1:
                mStartDate = mEndDate = TimeUtil.getToday();
                if(tabIndex == 0){  //避免重复请求
                    tabIndex = R.id.btn1;
                }else{
                    tabIndex = R.id.btn1;
                    requestData();
                }
                break;
            case R.id.btn2:
                tabIndex = R.id.btn2;
                mStartDate = mEndDate = TimeUtil.getYesterday();
                requestData();
                break;
            case R.id.btn3:
                break;
        }
        setQueryTime();
    }

    // 设置SegmentGroup选中状态
    protected void setTabFocus(int tabIndex) {
        switch (tabIndex){
            case R.id.btn1:
                btn1.setChecked(true);
                break;
            case R.id.btn2:
                btn2.setChecked(true);
                break;
            case R.id.btn3:
                btn3.setChecked(true);
                break;
        }
    }

    protected void selectDate() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                ReceiptsFragment.this,
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
        tabIndex = R.id.btn3;
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

    private void setQueryTime() {
        if (TimeUtil.getIntervalDays(mStartDate, mEndDate) >= 1){
            mQueryTime.setText(getString(R.string.query_when_time, mStartDate, mEndDate));
        }else{
            mQueryTime.setText(getString(R.string.query_time, mStartDate));
        }
    }

    protected String DataFormat(int data) {
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