package com.feifan.bp.marketinganalysis;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.internal.widget.ViewStubCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.feifan.bp.Constants;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.util.ToastUtil;
import com.feifan.bp.widget.SpacesItemDecoration;
import com.feifan.bp.widget.paginate.Paginate;

import java.util.List;

/**
 * 营销分析明细页
 * Created by kontar on 2016/2/3.
 */
public class DetailFragment extends ProgressFragment implements Paginate.Callbacks{

    private static final String TAG = "DetailFragment";

    public static final String EXTRA_COUPON_ID = "couponId";
    public static final String EXTRA_COUPON_NAME = "couponName";
    public static final String EXTRA_CHARGEOFF_NUM = "mChargeOffNum";

    private TextView mDetailTitle,mChargeOffCount;
    private RecyclerView mRecycler;
    private RelativeLayout mNoNetView,mNodataView;
    private String mStartDate,mEndDate,mCouponId,mBeginKey = "";
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
        initData();
        initView(view);
        return view;
    }

    private void initData() {
        Bundle args = getActivity().getIntent().getBundleExtra(PlatformTopbarActivity.EXTRA_ARGS);
        mStartDate = args.getString(MarketingHomeFragment.EXTRA_STARTDATE);
        mEndDate = args.getString(MarketingHomeFragment.EXTRA_ENDTDATE);
        mCouponId = args.getString(EXTRA_COUPON_ID);
        mCouponName = args.getString(EXTRA_COUPON_NAME);
        mChargeOffNum = args.getString(EXTRA_CHARGEOFF_NUM);
    }

    private void initView(View view) {
        mDetailTitle = (TextView) view.findViewById(R.id.detail_title);
        mChargeOffCount = (TextView) view.findViewById(R.id.detail_total_count);
        mRecycler = (RecyclerView) view.findViewById(R.id.list);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        SpacesItemDecoration space = new SpacesItemDecoration(15);
        mRecycler.addItemDecoration(space);

        mNoNetView = (RelativeLayout) view.findViewById(R.id.detail_no_net);
        mNodataView = (RelativeLayout) view.findViewById(R.id.detail_no_data);
        view.findViewById(R.id.reload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentShown(false);
                mBeginKey = "";
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
                mRecycler.scrollToPosition(0);
            }
        });
    }

    @Override
    protected void requestData() {
        fetchData(false);
    }

    private void fetchData(final boolean isLoadMore) {
        if (Utils.isNetworkAvailable(getActivity())){
            mRecycler.setVisibility(View.VISIBLE);
            mNoNetView.setVisibility(View.GONE);
            mNodataView.setVisibility(View.GONE);
            MarketingCtrl.getDetailList(mCouponId, mStartDate, mEndDate, mBeginKey, new Response.Listener<MarketingDetailModel>() {
                @Override
                public void onResponse(MarketingDetailModel model) {
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
            mRecycler.setVisibility(View.GONE);
            mNoNetView.setVisibility(View.VISIBLE);
            mNodataView.setVisibility(View.GONE);
        }
    }

    private void fillView(MarketingDetailModel model, Boolean isLoadMore) {
        mDetailList = model.detailList;
        mRequestDataSize = mDetailList.size();
        mBeginKey = model.mBeginKey;
        if(!isLoadMore){
            if(null == mDetailList || mDetailList.size() <= 0){
                mNodataView.setVisibility(View.VISIBLE);
                return;
            }else {
                mNodataView.setVisibility(View.GONE);
            }
            mDetailListAdapter = new DetailListAdapter(getActivity(),mDetailList);
            mRecycler.setAdapter(mDetailListAdapter);
            mPaginate = Paginate.with(mRecycler,DetailFragment.this)
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
}
