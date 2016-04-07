package com.feifan.bp.biz.receiptsrecord;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.widget.ViewStubCompat;
import android.text.TextUtils;
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
import com.feifan.bp.Constants;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.PlatformState;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.Statistics;
import com.feifan.bp.UserProfile;
import com.feifan.bp.Utils;
import com.feifan.bp.base.network.response.ToastErrorListener;
import com.feifan.bp.base.ui.ProgressFragment;
import com.feifan.bp.home.HomeCtrl;
import com.feifan.bp.util.TimeUtil;
import com.feifan.bp.util.ToastUtil;
import com.feifan.bp.widget.SegmentedGroup;
import com.feifan.bp.widget.paginate.Paginate;
import com.feifan.material.datetimepicker.date.DatePickerDialog;
import com.feifan.statlib.FmsAgent;

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
    private int mCurrentCount;
    private int mTotalCount = 0;
    private RelativeLayout mNoDataView,mNoNetView; //无数据 & 无网络页
    public static final String PAY_FLOW_ID = "payFlowId";  //push消息Id
    private boolean isMsgReaded = false;    //从push消息过来 才去设置消息未读状态

    //打开收款流水页
    public static void start(String payFlowId){
//        if(SystemUtil.isBPActivities(PlatformState.getApplicationContext())) {
//            Intent intent = new Intent(PlatformState.getApplicationContext(), PlatformTopbarActivity.class);
//            intent.putExtra(OnFragmentInteractionListener.INTERATION_KEY_TO, ReceiptsFragment.class.getName());
//            intent.putExtra(Constants.EXTRA_KEY_TITLE, PlatformState.getApplicationContext().getString(R.string.receipts_title));
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra(PAY_FLOW_ID, payFlowId);
//            PlatformState.getApplicationContext().startActivity(intent);
//        }else {
            Intent[] intents = new Intent[2];
            intents[0] = Intent.makeRestartActivityTask(new ComponentName(PlatformState.getApplicationContext(), com.feifan.bp.LaunchActivity.class));
            intents[1] = new Intent(PlatformState.getApplicationContext(), PlatformTopbarActivity.class);
            intents[1].putExtra(OnFragmentInteractionListener.INTERATION_KEY_TO, ReceiptsFragment.class.getName());
            intents[1].putExtra(Constants.EXTRA_KEY_TITLE, PlatformState.getApplicationContext().getString(R.string.receipts_title));
            intents[1].setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intents[1].putExtra(PAY_FLOW_ID, payFlowId);
            PlatformState.getApplicationContext().startActivities(intents);
//        }
    }

    public static Intent getPayFlowIntent(String payFlowId){
        Intent intent = new Intent(PlatformState.getApplicationContext(), PlatformTopbarActivity.class);
        intent.putExtra(OnFragmentInteractionListener.INTERATION_KEY_TO, ReceiptsFragment.class.getName());
        intent.putExtra(Constants.EXTRA_KEY_TITLE, PlatformState.getApplicationContext().getString(R.string.receipts_title));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(PAY_FLOW_ID, payFlowId);
        return intent;
    }

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_receipts);
        View view = stub.inflate();
        initMsgStatus();
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
        mSwipe.setColorSchemeResources(R.color.accent);
        mSwipe.setOnRefreshListener(this);
        receiptsList  = (ListView) view.findViewById(R.id.receipts_list);
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.view_no_data_net,null,false);
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

    /**
     * 更新消息状态
     */
    private void initMsgStatus(){
        FmsAgent.onEvent(PlatformState.getApplicationContext(), Statistics.FB_PUSHMES_READ);
        String payFlowId = getPayFlowId();
        if(!TextUtils.isEmpty(payFlowId) && !isMsgReaded){
            setMessageStatus(payFlowId);
        }
    }
    private String getPayFlowId(){
        if(isAdded()){
            Intent intent = getActivity().getIntent();
            if(intent != null && !TextUtils.isEmpty(intent.getStringExtra(PAY_FLOW_ID))){
                return intent.getStringExtra(PAY_FLOW_ID);
            }
        }
        return "";
    }

    private void setMessageStatus(String payFlowId) {
        if(Utils.isNetworkAvailable()){
            int uid = UserProfile.getInstance().getUid();
            if(uid != -1) {
                HomeCtrl.setMessageStatusRead(String.valueOf(uid), payFlowId, new Response.Listener() {
                    @Override
                    public void onResponse(Object o) {
                        setContentEmpty(false);
                        setContentShown(true);
                    }
                }, null);
            }
        }else {
            receiptsList.setAdapter(null);
            mNoDataView.setVisibility(View.GONE);
            mNoNetView.setVisibility(View.VISIBLE);
            stopRefresh();
        }
    }

    @Override
    protected void requestData() {
        mCurrentCount = 0;
        pageIndex = 0;
        fetchData(false);
    }

    private void fetchData(final boolean isLoadMore) {
        if(Utils.isNetworkAvailable()) {
            mSwipe.setRefreshing(true);
            mNoNetView.setVisibility(View.GONE);
            ReceiptsCtrl.getReceiptsRecords(mStartDate, mEndDate, pageIndex + "", new Response.Listener<ReceiptsModel>() {
                @Override
                public void onResponse(ReceiptsModel model) {
                    if (isAdded() && null != model) {
                        stopRefresh();
                        setContentEmpty(false);
                        setContentShown(true);
                        isLoading = false;
                        fillView(model, isLoadMore);
                        initMsgStatus();
                    }
                }
            }, new ToastErrorListener());
        }else{
            stopRefresh();
            setContentEmpty(false);
            setContentShown(true);
            receiptsList.setAdapter(null);
            mNoDataView.setVisibility(View.GONE);
            mNoNetView.setVisibility(View.VISIBLE);
        }
    }

    private void fillView(ReceiptsModel model, boolean isLoadMore){
        mReceiptsList = model.receiptsList;
        if(null != mReceiptsList && mReceiptsList.size() > 0){ // 有数据
            mNoDataView.setVisibility(View.GONE);
            mCurrentCount += mReceiptsList.size();
            mTotalCount = Integer.parseInt(model.totalCount);
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
        return mCurrentCount == mTotalCount;
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
