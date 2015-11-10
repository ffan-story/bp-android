package com.feifan.bp.TransactionFlow;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.feifan.bp.PlatformState;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.TransactionFlow.FlashSummaryModel.FlashSummaryDetailModel;
import com.feifan.bp.TransactionFlow.FlashListModel.FlashDetailModel;
import com.feifan.bp.network.GetRequest;
import com.feifan.bp.network.JsonRequest;

import com.feifan.bp.util.TimeUtils;
import com.feifan.bp.widget.DividerItemDecoration;
import com.feifan.bp.widget.SegmentedGroup;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

import com.bartoszlipinski.recyclerviewheader.*;

/**
 * Created by Frank on 15/11/6.
 */
public class FlashBuyFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private FlowDetailAdapter flowDetailadapter;
    private SegmentedGroup segmentedGroup;
    private RadioButton rb_today, rb_yesterday, rb_other;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mFlowList;
    private TextView mDetailTitle;
    private LinearLayoutManager mLayoutManager;
    private RecyclerViewHeader header;

    private ArrayList<FlashSummaryDetailModel> tradeDetailList;
    private ArrayList<FlashDetailModel> flashDetailList;
    private FlowListAdapter flowListAdapter;
    private String startDate;
    private String endDate;

    private String mStoreId;
    private int mPageNum;

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
        mFlowList = (RecyclerView) v.findViewById(R.id.rv_detail);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mFlowList.setLayoutManager(mLayoutManager);
        mFlowList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        header = RecyclerViewHeader.fromXml(getActivity(), R.layout.header_fragment_flash_buy);
        header.attachTo(mFlowList);
        mFlowList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy == 0) {
                    mRefreshLayout.setEnabled(true);
                } else {
                    mRefreshLayout.setEnabled(false);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

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
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        //解决滑动ViewPager引起swiperefreshlayout刷新的冲突
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_MOVE:
                        mRefreshLayout.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                        mRefreshLayout.setEnabled(true);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        mRefreshLayout.setEnabled(true);
                        break;
                }
                return false;
            }
        });
        circleIndicator = (CircleIndicator) v.findViewById(R.id.indicator);
        segmentedGroup = (SegmentedGroup) v.findViewById(R.id.segmentedGroup);
        mDetailTitle = (TextView) v.findViewById(R.id.tv_detail);

        rb_today = (RadioButton) v.findViewById(R.id.today);
        rb_yesterday = (RadioButton) v.findViewById(R.id.yesterday);
        rb_other = (RadioButton) v.findViewById(R.id.other);
        rb_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "选择其他日期", Toast.LENGTH_LONG).show();
            }
        });
        segmentedGroup.setOnCheckedChangeListener(this);

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
    private void getFlashFlowList(final boolean isShowLoading,final boolean isLoadMore) {
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
                        if(isLoadMore){
                            mPageNum--;
                        }
                    }
                })
                .build()
                .targetClass(FlashListModel.class)
                .listener(new Response.Listener<FlashListModel>() {
                    @Override
                    public void onResponse(FlashListModel model) {
                        if(isLoadMore){
                            if (model.flashDetailList == null||model.flashDetailList.size() == 0){
                                Toast.makeText(getActivity(),"没有更多数据",Toast.LENGTH_LONG).show();
                            }else{
                                flashDetailList.addAll(model.flashDetailList);
                            }
                        }else{
                            flashDetailList = model.flashDetailList;
                        }
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
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.today:
                startDate = endDate = TimeUtils.getToday();
                mPageNum = 1;
                break;
            case R.id.yesterday:
                startDate = endDate = TimeUtils.getYesterday();
                mPageNum = 1;
                break;
            case R.id.other:
                mPageNum = 1;
                Toast.makeText(getActivity(), "选择其他日期", Toast.LENGTH_LONG).show();
                break;
        }
        getFlashFlowData(true);
        getFlashFlowList(true,false);
    }

    private void stopRefresh(){
        if(mRefreshLayout.isRefreshing()){
            mRefreshLayout.setRefreshing(false);
        }
    }
}
