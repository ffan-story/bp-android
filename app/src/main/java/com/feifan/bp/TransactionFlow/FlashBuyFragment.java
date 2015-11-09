package com.feifan.bp.TransactionFlow;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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
    private FlowDetailAdapter adapter;
    private SegmentedGroup segmentedGroup;
    private RadioButton rb_today, rb_yesterday, rb_other;
    private RecyclerView mFlowList;
    private TextView mDetailTitle;

    private ArrayList<FlashSummaryDetailModel> tradeDetailList;
    private ArrayList<FlashDetailModel> flashDetailList;
    private String startDate;
    private String endDate;

    private String mStoreId;
    private String mPageNum = "1";

    public FlashBuyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_flash_buy, null);
        mFlowList = (RecyclerView) v.findViewById(R.id.rv_detail);
        mFlowList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFlowList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        RecyclerViewHeader header = RecyclerViewHeader.fromXml(getActivity(),R.layout.header_fragment_flash_buy);
        header.attachTo(mFlowList);

        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        circleIndicator = (CircleIndicator) v.findViewById(R.id.indicator);
        segmentedGroup = (SegmentedGroup) v.findViewById(R.id.segmentedGroup);
        mDetailTitle = (TextView) v.findViewById(R.id.tv_detail);

        rb_today = (RadioButton) v.findViewById(R.id.today);
        rb_yesterday = (RadioButton) v.findViewById(R.id.yesterday);
        rb_other = (RadioButton) v.findViewById(R.id.other);
        segmentedGroup.setOnCheckedChangeListener(this);

        initDatas();
        return v;
    }


    private void initDatas() {
        mStoreId = UserProfile.getInstance().getAuthRangeId();
        tradeDetailList = new ArrayList<>();
        flashDetailList = new ArrayList<>();
        startDate = endDate = TimeUtils.getToday();
        rb_today.setChecked(true);
        getFlashFlowData();
        getFlashFlowList();
    }

    /**
     * 获取闪购对账流水明细
     */
    private void getFlashFlowData() {
        ((TransFlowTabActivity) getActivity()).showProgressBar(true);

        //测试
        String url = "http://10.1.169.16:12154/mapp/transactiondetail";

        JsonRequest<FlashSummaryModel> request = new GetRequest.Builder<FlashSummaryModel>(url)
                .param("endDate", endDate)
                .param("startDate", startDate)
                .param("action", "flashsummary")
                .errorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ((TransFlowTabActivity) getActivity()).hideProgressBar();
                    }
                })
                .build()
                .targetClass(FlashSummaryModel.class)
                .listener(new Response.Listener<FlashSummaryModel>() {
                    @Override
                    public void onResponse(FlashSummaryModel model) {
                        tradeDetailList = model.flashSummaryList;
                        adapter = new FlowDetailAdapter(getChildFragmentManager(), tradeDetailList);
                        viewPager.setAdapter(adapter);
                        circleIndicator.setViewPager(viewPager);
                        ((TransFlowTabActivity) getActivity()).hideProgressBar();
                    }
                });
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    /**
     * 获取闪购对账流水单明细列表
     */
    private void getFlashFlowList() {
        ((TransFlowTabActivity) getActivity()).showProgressBar(true);

        //测试
        String url = "http://10.1.169.16:12154/mapp/transactiondetail";
        JsonRequest<FlashListModel> request = new GetRequest.Builder<FlashListModel>(url)
                .param("endDate", endDate)
                .param("startDate", startDate)
                .param("pageIndex", mPageNum)
                .param("limit", "10")
                .errorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ((TransFlowTabActivity) getActivity()).hideProgressBar();
                    }
                })
                .build()
                .targetClass(FlashListModel.class)
                .listener(new Response.Listener<FlashListModel>() {
                    @Override
                    public void onResponse(FlashListModel model) {
                        flashDetailList = model.flashDetailList;
                        FlowListAdapter flowListAdapter = new FlowListAdapter(getActivity(),flashDetailList);
                        mFlowList.setAdapter(flowListAdapter);

                        mDetailTitle.setText(getActivity().getString(R.string.flash_detail_title, startDate, endDate, model.totalCount));
                        ((TransFlowTabActivity) getActivity()).hideProgressBar();
                    }
                });
        PlatformState.getInstance().getRequestQueue().add(request);
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
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.today:
                startDate = endDate = TimeUtils.getToday();
                break;
            case R.id.yesterday:
                startDate = endDate = TimeUtils.getYesterday();
                break;
            case R.id.other:
                Toast.makeText(getActivity(), "选择其他日期", Toast.LENGTH_LONG).show();
                break;
        }
        getFlashFlowData();
        getFlashFlowList();
    }
}
