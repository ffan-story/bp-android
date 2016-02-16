package com.feifan.bp.marketinganalysis;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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

import java.util.ArrayList;
import java.util.List;

/**
 * 核销明细
 * Created by apple on 16-1-21.
 */
public class DetailFragment extends ProgressFragment implements Paginate.Callbacks, SwipeRefreshLayout.OnRefreshListener {
    public static final String EXTRA_CHARGE_OFF_ID = "charge_off_id";
    public static final String EXTRA_CHARGE_OFF_START_TIME = "charge_off_start_time";
    public static final String EXTRA_CHARGE_OFF_END_TIME = "charge_off_end_time";
    public static final String EXTRA_CHARGE_OFF_END_NAME = "red_name";
    public static final String EXTRA_CHARGE_OFF_END_COUNT = "red_count";

    private DetailAdapter mRedDetaAdap;
    private String mCouponId;
    private String mSDate, mEDate;
    private Paginate paginate;
    private String mSRedName, mERedCount;

    private String mBeginKey = "";
    private boolean loading = false;
    private int mRequestDataSize = 0;

    public List<DetailModel.RedDetailModel> mListRedDetail;
    private TextView mDetailTitle,mChargeOffCount,mTvNoData;
    private RecyclerView mRecycler;
    private RelativeLayout mNoNetView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCouponId = getArguments().getString(EXTRA_CHARGE_OFF_ID);
        mSDate = getArguments().getString(EXTRA_CHARGE_OFF_START_TIME);
        mEDate = getArguments().getString(EXTRA_CHARGE_OFF_END_TIME);
        mSRedName = getArguments().getString(EXTRA_CHARGE_OFF_END_NAME);
        mERedCount = getArguments().getString(EXTRA_CHARGE_OFF_END_COUNT);
    }

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_marketing_detail);
        View view = stub.inflate();
        mDetailTitle = (TextView) view.findViewById(R.id.detail_title);
        mChargeOffCount = (TextView) view.findViewById(R.id.detail_total_count);
        mRecycler = (RecyclerView) view.findViewById(R.id.list);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        SpacesItemDecoration space = new SpacesItemDecoration(15);
        mRecycler.addItemDecoration(space);

        mNoNetView = (RelativeLayout) view.findViewById(R.id.detail_no_net);
        mTvNoData = (TextView) view.findViewById(R.id.fragment_marketing_detail_no_data);

        view.findViewById(R.id.reload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentShown(false);
                requestData();
            }
        });
        getToolbar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecycler.scrollToPosition(0);
            }
        });

        if (!TextUtils.isEmpty(mSRedName)){
            mDetailTitle.setText(mSRedName);
        }
        if (!TextUtils.isEmpty(mERedCount)) {
            mChargeOffCount.setText(mERedCount);
        }

        mNoNetView.findViewById(R.id.reload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData();
            }
        });

        getToolbar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBeginKey = "";
                requestData();
            }
        });
        return view;
    }

    @Override
    protected void requestData() {
        if (!isAdded()) {
            return;
        }
        setContentShown(true);
        if (Utils.isNetworkAvailable(getActivity())) {
            mRecycler.setVisibility(View.VISIBLE);
            mTvNoData.setVisibility(View.GONE);
            mNoNetView.setVisibility(View.GONE);
            fetchData(false);
        } else {
            mRecycler.setVisibility(View.GONE);
            mTvNoData.setVisibility(View.GONE);
            mNoNetView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取网络数据
     *
     * @param isLoadMore
     */
    private void fetchData(final boolean isLoadMore) {
        setContentShown(true);
        if (!isAdded()) {
            return;
        }
        if (loading && !isLoadMore) {
            setContentShown(false);
        }

        AlysisCtrl.getRedTypeListDetail(mCouponId, mSDate, mEDate, mBeginKey, new Response.Listener<DetailModel>() {
            @Override
            public void onResponse(DetailModel redDetailModel) {
                loading = false;
                setPageData(redDetailModel, isLoadMore);
            }
        });
    }

    /**
     * UI设置数据
     *
     * @param redDetailModel
     * @param isLoadMore
     */
    private void setPageData(DetailModel redDetailModel,boolean isLoadMore){
        mListRedDetail =redDetailModel.redDetailList;
        mRequestDataSize = mListRedDetail.size();
        mBeginKey = redDetailModel.mStrBeginKey;
        if (!isLoadMore) {
            setContentShown(true);
        }

        if (isLoadMore) {
            if (mRedDetaAdap != null) {
                mRedDetaAdap.notifyData(mListRedDetail);
            }
        } else {
            if (null == redDetailModel.redDetailList || redDetailModel.redDetailList.size() <= 0) {//为空
                mRecycler.setVisibility(View.GONE);
                mTvNoData.setVisibility(View.VISIBLE);
                return;
            } else {
                mRecycler.setVisibility(View.VISIBLE);
                mTvNoData.setVisibility(View.GONE);
            }

            mRedDetaAdap = new DetailAdapter(getActivity(), mListRedDetail);
            mRecycler.setAdapter(mRedDetaAdap);
            paginate = Paginate.with(mRecycler, DetailFragment.this)
                    .setLoadingTriggerThreshold(0)
                    .addLoadingListItem(true)
                    .build();
        }
        paginate.setHasMoreDataToLoad(!hasLoadedAllItems());

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onLoadMore() {
        loading = true;
        fetchData(true);
    }

    @Override
    public void hasLoadMore() {
        ToastUtil.showToast(getActivity(), "已经没有更多数据了");
    }

    @Override
    public boolean isLoading() {
        return loading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return !(mRequestDataSize == Integer.parseInt(Constants.LIST_LIMIT));
    }

    @Override
    public void onRefresh() {
        mBeginKey = "";
        mListRedDetail = new ArrayList<>();
        requestData();
    }

    protected Toolbar getToolbar() {
        Activity a = getActivity();
        if (a instanceof PlatformTopbarActivity) {
            return ((PlatformTopbarActivity) a).getToolbar();
        }
        return null;
    }
}
