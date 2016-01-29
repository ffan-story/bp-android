package com.feifan.bp.marketinganalysis;

import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.widget.ViewStubCompat;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feifan.bp.PlatformTabActivity;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.util.TimeUtil;
import com.feifan.bp.widget.SegmentedGroup;
import com.feifan.material.datetimepicker.date.DatePickerDialog;
import java.util.Calendar;

/**
 *
 * Created by apple on 16-1-21.
 */
public abstract class AbstractAnalSubTotalPack extends ProgressFragment implements RadioGroup.OnCheckedChangeListener
        , SwipeRefreshLayout.OnRefreshListener
        , MenuItem.OnMenuItemClickListener
        , PlatformTabActivity.onPageSelectListener
        , DatePickerDialog.OnDateSetListener{

    public  TextView mTvNoData;
    public SwipeRefreshLayout mSRL;
    public ListView mLvSubTotal;
    public SegmentedGroup segmentedGroup;

    private RadioButton  mRdbToday,mRdbYesterday,mRdbCustom;
    public TextView mRedTvQueryTime;
    private int tabIndex;
    public RelativeLayout mRel2Row;
    public TextView mTvChargeOffTotal,mTvSubsidyMoneyFf,mTvSubsidyMoneyThird,mTvSubsidyMoneyVendor;
    private String mStrStarDate;
    private String mStrEndDate;

    public LinearLayout noNet ;

    public abstract void myRequestData(String sDate, String eDate);
    public abstract void mItemClick(int position);
    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_anal_red_subtotal);
        View v = stub.inflate();
        segmentedGroup = (SegmentedGroup) v.findViewById(R.id.red_segmentedGroup);
        segmentedGroup.setOnCheckedChangeListener(this);
        mRdbToday = (RadioButton) v.findViewById(R.id.red_rdb_today);
        mRdbYesterday = (RadioButton) v.findViewById(R.id.red_rdb_yesterday);
        mRdbCustom = (RadioButton) v.findViewById(R.id.red_rdb_custom);
        mRdbCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialog();
            }
        });

        mSRL = (SwipeRefreshLayout)v.findViewById(R.id.swipe_coupon_list);
        mLvSubTotal = (ListView) v.findViewById(R.id.more_list);
        mRedTvQueryTime = (TextView)v.findViewById(R.id.red_tv_query_time);
        mSRL.setOnRefreshListener(this);
        noNet = (LinearLayout)v.findViewById(R.id.no_net);
        noNet.findViewById(R.id.btn_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData();
            }
        });

        View header = View.inflate(getActivity(),R.layout.head_analysis_red,null);
        mTvChargeOffTotal= (TextView)header.findViewById(R.id.anal_tv_charge_off_total);
        mTvSubsidyMoneyFf=  (TextView)header.findViewById(R.id.anal_tv_subsidy_money_ff);
        mTvNoData =(TextView) header.findViewById(R.id.no_data_view);
        mRel2Row = (RelativeLayout)header.findViewById(R.id.rel_2row);
        mTvSubsidyMoneyThird = (TextView)mRel2Row.findViewById(R.id.anal_tv_subsidy_money_third);
        mTvSubsidyMoneyVendor = (TextView)mRel2Row.findViewById(R.id.anal_tv_subsidy_money_vendor);
        mLvSubTotal.addHeaderView(header);
        initData();
        mLvSubTotal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mItemClick(position);

            }
        });
        return v;
    }

    private void initData() {
        mRdbToday.setChecked(true);
        tabIndex = R.id.red_rdb_today;
        mRedTvQueryTime.setText(getString(R.string.query_time, TimeUtil.getToday()));
    }

    @Override
    protected void requestData() {
        if (!isAdded()){
            return;
        }

        setContentShown(true);
        if (Utils.isNetworkAvailable(getActivity())){
            noNet.setVisibility(View.GONE);
            mTvNoData.setVisibility(View.GONE);
            mSRL.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(mStrStarDate) && !TextUtils.isEmpty(mStrEndDate)){
                myRequestData(mStrStarDate, mStrEndDate);
            }
        } else{
            mSRL.setVisibility(View.GONE);
            noNet.setVisibility(View.VISIBLE);
            mTvNoData.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.red_rdb_today://今天
                tabIndex = R.id.red_rdb_today;
                mStrStarDate =TimeUtil.getToday();
                mStrEndDate = TimeUtil.getTomorrowday();
                myRequestData(mStrStarDate, mStrEndDate);
                break;
            case R.id.red_rdb_yesterday://昨天
                tabIndex = R.id.red_rdb_yesterday;
                mStrStarDate =TimeUtil.getYesterday();
                mStrEndDate = TimeUtil.getToday();
                myRequestData(mStrStarDate, mStrEndDate);
                break;
            case R.id.red_rdb_custom://自定义
                tabIndex = R.id.red_rdb_custom;
                break;
        }
    }


    private void initDialog() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        mStrStarDate = year + "-" + DataFormat(monthOfYear + 1) + "-" + DataFormat(dayOfMonth);
        mStrEndDate = TimeUtil.getAddOneDay(yearEnd + "-" + DataFormat(monthOfYearEnd + 1) + "-" + DataFormat(dayOfMonthEnd));
        myRequestData(mStrStarDate, mStrEndDate);
    }

    private void setTabFocus(int tabIndex) {
        switch (tabIndex){
            case R.id.red_rdb_today:
                mRdbToday.setChecked(true);
                break;
            case R.id.red_rdb_yesterday:
                mRdbYesterday.setChecked(true);
                break;
            case R.id.red_rdb_custom:
                mRdbCustom.setChecked(true);
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

    @Override
    public void onPageSelected() {

    }
}
