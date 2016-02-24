package com.feifan.bp.marketinganalysis;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.widget.ViewStubCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.feifan.bp.Constants;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.util.ToastUtil;
import com.feifan.bp.widget.paginate.Paginate;

import java.util.List;

/**
 * 营销分析明细页
 * Created by kontar on 2016/2/3.
 */
public class DetailFragment extends ProgressFragment implements Paginate.Callbacks, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "DetailFragment";

    public static final String EXTRA_COUPON_ID = "couponId";
    public static final String EXTRA_COUPON_NAME = "couponName";
    public static final String EXTRA_CHARGEOFF_NUM = "mChargeOffNum";

    private SwipeRefreshLayout mSwipe;
    private TextView mDetailTitle,mChargeOffCount;
    private ListView mList;
    private RelativeLayout mNoNetView,mNodataView;
    private String mType,mStartDate,mEndDate,mCouponId,mBeginKey = "";
    private String mCouponName,mChargeOffNum;
    private List<MarketingDetailModel.DetailListModel> mDetailList;
    private DetailListAdapter mDetailListAdapter;
    private Paginate mPaginate;
    private boolean isLoading = false;
    private int mRequestDataSize = 0;

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_marketing_detail);
        View view = stub.inflate();
        View header = LayoutInflater.from(getContext()).inflate(R.layout.fragment_marketing_detail_header,null);
        initData();
        initView(view,header);
        return view;
    }

    private void initData() {
        Bundle args = getActivity().getIntent().getBundleExtra(PlatformTopbarActivity.EXTRA_ARGS);
        mType = args.getString(MarketingHomeFragment.TYPE);
        mStartDate = args.getString(MarketingHomeFragment.EXTRA_STARTDATE);
        mEndDate = args.getString(MarketingHomeFragment.EXTRA_ENDTDATE);
        mCouponId = args.getString(EXTRA_COUPON_ID);
        mCouponName = args.getString(EXTRA_COUPON_NAME);
        mChargeOffNum = args.getString(EXTRA_CHARGEOFF_NUM);
    }

    private void initView(View view,View header) {
        mSwipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe_marketing);
        mSwipe.setColorSchemeResources(R.color.accent);
        mSwipe.setOnRefreshListener(this);
        mDetailTitle = (TextView) view.findViewById(R.id.detail_title);
        mChargeOffCount = (TextView) view.findViewById(R.id.detail_total_count);
        mList = (ListView) view.findViewById(R.id.list);
        mList.addHeaderView(header);
        mList.setAdapter(null);
        mNoNetView = (RelativeLayout) header.findViewById(R.id.detail_no_net);
        mNodataView = (RelativeLayout) header.findViewById(R.id.detail_no_data);
        header.findViewById(R.id.reload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData();
            }
        });

        if(!TextUtils.isEmpty(mCouponName)){
            mDetailTitle.setText(String.format(getString(R.string.anal_coupon_name),mCouponName));
        }
        if(!TextUtils.isEmpty(mChargeOffNum)){
            mChargeOffCount.setText(mChargeOffNum);
        }
        getToolbar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList.setSelection(0);
            }
        });
    }

    @Override
    protected void requestData() {
        mBeginKey = "";
        fetchData(false);
    }

    private void fetchData(final boolean isLoadMore) {
        if (Utils.isNetworkAvailable(getActivity())){
            mNoNetView.setVisibility(View.GONE);
            mNodataView.setVisibility(View.GONE);
            mSwipe.setRefreshing(true);
            MarketingCtrl.getDetailList(mType,mCouponId, mStartDate, mEndDate, mBeginKey, new Response.Listener<MarketingDetailModel>() {
                @Override
                public void onResponse(MarketingDetailModel model) {
                    stopRefresh();
                    setContentEmpty(false);
                    setContentShown(true);
                    if (isAdded() && null != model) {
                        isLoading =false;
                        fillView(model, isLoadMore);
                    }
                }
            });
        }else{
            setContentEmpty(false);
            setContentShown(true);
            stopRefresh();
            mList.setAdapter(null);
            mNoNetView.setVisibility(View.VISIBLE);
            mNodataView.setVisibility(View.GONE);
        }
    }

    private void fillView(MarketingDetailModel model, Boolean isLoadMore) {
        mDetailList = model.detailList;
        mRequestDataSize = mDetailList.size();
        mBeginKey = model.mBeginKey;
        if(!isLoadMore){
            if(null == mList || mDetailList.size() <= 0){
                mList.setAdapter(null);
                mNodataView.setVisibility(View.VISIBLE);
                return;
            }else {
                mNodataView.setVisibility(View.GONE);
            }
            mDetailListAdapter = new DetailListAdapter(getActivity(), mDetailList);
            mList.setAdapter(mDetailListAdapter);
            mPaginate = Paginate.with(mList,DetailFragment.this)
                    .setLoadingTriggerThreshold(0)
                    .addLoadingListItem(true)
                    .build();
        }else{
            if(null != mDetailListAdapter){
                mDetailListAdapter.notifyData(mDetailList);
            }
        }
        mPaginate.setHasMoreDataToLoad(!hasLoadedAllItems());
    }

    @Override
    public void onLoadMore() {
        isLoading = true;
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
        return mRequestDataSize != Integer.parseInt(Constants.LIST_LIMIT);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    protected Toolbar getToolbar() {
        Activity a = getActivity();
        if (a instanceof PlatformTopbarActivity) {
            return ((PlatformTopbarActivity) a).getToolbar();
        }
        return null;
    }

    @Override
    public void onRefresh() {
        requestData();
    }

    protected void stopRefresh() {
        if(mSwipe.isRefreshing()){
            mSwipe.setRefreshing(false);
        }
    }
}
