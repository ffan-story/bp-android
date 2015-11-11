package com.feifan.bp.TransactionFlow;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.PlatformState;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.TransactionFlow.FlashSummaryModel.FlashSummaryDetailModel;
import com.feifan.bp.TransactionFlow.FlashListModel.FlashDetailModel;
import com.feifan.bp.home.check.IndicatorFragment;
import com.feifan.bp.network.GetRequest;
import com.feifan.bp.network.JsonRequest;

import com.feifan.bp.util.LogUtil;
import com.feifan.bp.util.TimeUtils;
import com.feifan.bp.widget.LoadingMoreListView;
import com.feifan.bp.widget.SegmentedGroup;

import java.util.ArrayList;
import java.util.Calendar;

import me.relex.circleindicator.CircleIndicator;

import com.feifan.bp.widget.OnLoadingMoreListener;
import com.feifan.material.datetimepicker.date.DatePickerDialog;

/**
 * Created by Frank on 15/11/6.
 */
public class FlashBuyFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener,
        MenuItem.OnMenuItemClickListener,
        DatePickerDialog.OnDateSetListener,
        OnLoadingMoreListener {

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private FlowDetailAdapter flowDetailadapter;
    private SegmentedGroup segmentedGroup;
    private RadioButton rb_today, rb_yesterday, rb_other;
    private SwipeRefreshLayout mRefreshLayout;
    private LoadingMoreListView mFlowList;
    private TextView mDetailTitle;

    private ArrayList<FlashSummaryDetailModel> tradeDetailList;
    private ArrayList<FlashDetailModel> flashDetailList;
    private FlowListAdapter flowListAdapter;
    private String startDate;
    private String endDate;

    private String mStoreId;
    private int mPageNum = 1;
    private int mTotalCount = 0;
    private Boolean mCheckFlag = false;

    public FlashBuyFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_flash_buy, null);
        View header = inflater.inflate(R.layout.header_fragment_flash_buy, null);
        mFlowList = (LoadingMoreListView) v.findViewById(R.id.rv_detail);
        mFlowList.addHeaderView(header);
        mFlowList.setOnLoadingMoreListener(this);


        mRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe);
        mRefreshLayout.setColorSchemeResources(R.color.accent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNum = 1;
                getFlashFlowData(false);
                getFlashFlowList(false, false);
            }
        });

        viewPager = (ViewPager) header.findViewById(R.id.viewpager);
        circleIndicator = (CircleIndicator) header.findViewById(R.id.indicator);
        segmentedGroup = (SegmentedGroup) header.findViewById(R.id.segmentedGroup);
        mDetailTitle = (TextView) header.findViewById(R.id.tv_detail);

        rb_today = (RadioButton) header.findViewById(R.id.today);
        rb_yesterday = (RadioButton) header.findViewById(R.id.yesterday);
        rb_other = (RadioButton) header.findViewById(R.id.other);
        segmentedGroup.setOnCheckedChangeListener(this);
        rb_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckFlag) {
                    initDialog();
                }
            }
        });

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        mRefreshLayout.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mRefreshLayout.setEnabled(true);
                        break;
                }
                return false;
            }
        });

        initDatas();
        return v;
    }

    private void initDatas() {
        mStoreId = UserProfile.getInstance().getAuthRangeId();
        tradeDetailList = new ArrayList<>();
        flashDetailList = new ArrayList<>();
        rb_today.setChecked(true);
    }

    /**
     * 获取闪购对账流水明细
     */
    private void getFlashFlowData(final boolean isShowLoading) {
        if (isShowLoading) {
            ((TransFlowTabActivity) getActivity()).showProgressBar(true);
        }

        //测试
        String url = "http://api.sit.ffan.com/mapp/v1/mapp/transactionspecific";

        JsonRequest<FlashSummaryModel> request = new GetRequest.Builder<FlashSummaryModel>(url)
                .param("endDate", endDate)
                .param("startDate", startDate)
                .param("action", "flashsummary")
                .errorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (isShowLoading) {
                            ((TransFlowTabActivity) getActivity()).hideProgressBar();
                        }
                        stopRefresh();
                    }
                })
                .build()
                .targetClass(FlashSummaryModel.class)
                .listener(new Response.Listener<FlashSummaryModel>() {
                    @Override
                    public void onResponse(FlashSummaryModel model) {
                        tradeDetailList = model.flashSummaryList;
                        flowDetailadapter = new FlowDetailAdapter(getChildFragmentManager(), tradeDetailList);
                        viewPager.setAdapter(flowDetailadapter);
                        circleIndicator.setViewPager(viewPager);
                        if (isShowLoading) {
                            ((TransFlowTabActivity) getActivity()).hideProgressBar();
                        }
                        stopRefresh();
                    }
                });
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    /**
     * 获取闪购对账流水单明细列表
     */
    private void getFlashFlowList(final boolean isShowLoading, final boolean isLoadMore) {
        if (isShowLoading) {
            ((TransFlowTabActivity) getActivity()).showProgressBar(true);
        }

        //测试
        String url = "http://api.sit.ffan.com/mapp/v1/mapp/transactionspecific";

        JsonRequest<FlashListModel> request = new GetRequest.Builder<FlashListModel>(url)
                .param("endDate", endDate)
                .param("startDate", startDate)
                .param("pageIndex", String.valueOf(mPageNum))
                .param("limit", "10")
                .errorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (isShowLoading) {
                            ((TransFlowTabActivity) getActivity()).hideProgressBar();
                        }
                        stopRefresh();
                        if (isLoadMore) {
                            mPageNum--;
                        }
                    }
                })
                .build()
                .targetClass(FlashListModel.class)
                .listener(new Response.Listener<FlashListModel>() {
                    @Override
                    public void onResponse(FlashListModel model) {
                        if (isLoadMore) {
                            if (model.flashDetailList == null || model.flashDetailList.size() == 0) {
                                Toast.makeText(getActivity(), "获得数据为空", Toast.LENGTH_LONG).show();
                            } else {
                                flashDetailList.addAll(model.flashDetailList);
                            }
                        } else {
                            flashDetailList = model.flashDetailList;
                        }
                        mTotalCount = model.totalCount;
                        flowListAdapter = new FlowListAdapter(getActivity(), flashDetailList);
                        mFlowList.setAdapter(flowListAdapter);
                        mDetailTitle.setText(getActivity().getString(R.string.flash_detail_title, startDate, endDate, model.totalCount));
                        if (isShowLoading) {
                            ((TransFlowTabActivity) getActivity()).hideProgressBar();
                        }
                        stopRefresh();
                    }
                });
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_option, menu);
        menu.findItem(R.id.check_menu_directions).setOnMenuItemClickListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        toolbar.setTitle(R.string.transaction_flow);
        toolbar.setNavigationIcon(R.mipmap.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//
//                return false;
//            }
//        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.today:
                startDate = endDate = TimeUtils.getToday();
                mPageNum = 1;
                getFlashFlowData(true);
                getFlashFlowList(true, false);
                mCheckFlag = false;
                break;
            case R.id.yesterday:
                startDate = endDate = TimeUtils.getYesterday();
                mPageNum = 1;
                getFlashFlowData(true);
                getFlashFlowList(true, false);
                mCheckFlag = false;
                break;
            case R.id.other:
                mPageNum = 1;
                initDialog();
                break;
        }
    }

    private void initDialog() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                FlashBuyFragment.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setAccentColor(getResources().getColor(R.color.accent));
        dpd.show(getFragmentManager(), "Datepickerdialog");
        dpd.setOnDateSetListener(this);
        dpd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mCheckFlag = true;
            }
        });
    }

    private void stopRefresh() {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intent = new Intent(getActivity(), PlatformTopbarActivity.class);
        intent.putExtra(OnFragmentInteractionListener.INTERATION_KEY_TO, IndicatorFragment.class.getName());
        startActivity(intent);
        return false;
    }

    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        mCheckFlag = true;

        String FromDate = year + "-" + DataFormat(monthOfYear + 1) + "-" + DataFormat(dayOfMonth);
        LogUtil.i("fangke", "FromDate============" + FromDate);

        String ToDate = yearEnd + "-" + DataFormat(monthOfYearEnd + 1) + "-" + DataFormat(dayOfMonthEnd);
        LogUtil.i("fangke", "ToDate============" + ToDate);

        if (TimeUtils.compare_date(FromDate, TimeUtils.getToday())) {
            Toast.makeText(getActivity(), "起始时间不能超过今天", Toast.LENGTH_LONG).show();
        } else if (TimeUtils.compare_date(ToDate, TimeUtils.getToday())) {
            Toast.makeText(getActivity(), "截止时间不能超过今天", Toast.LENGTH_LONG).show();
        } else if (TimeUtils.compare_date(FromDate, ToDate)) {
            Toast.makeText(getActivity(), "起始时间不能超过截止时间", Toast.LENGTH_LONG).show();
        } else {
            startDate = FromDate;
            endDate = FromDate;
            getFlashFlowData(true);
            getFlashFlowList(true, false);
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
    public void onLoadingMore() {
        if (flashDetailList.size() >= mTotalCount) {
            Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_LONG).show();
            mFlowList.hideFooterView();
        } else {
            mPageNum++;
            getFlashFlowList(true, true);
        }
    }
}
