package com.feifan.bp.marketinganalysis;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.widget.ViewStubCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.feifan.bp.Constants;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.util.ToastUtil;
import com.feifan.bp.widget.paginate.Paginate;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * 红包核销明细
 * Created by apple on 16-1-21.
 */
public class AnalRedDetailFrag extends ProgressFragment implements Paginate.Callbacks, SwipeRefreshLayout.OnRefreshListener {
    public static final String EXTRA_CHARGE_OFF_ID = "charge_off_id";
    public static final String EXTRA_CHARGE_OFF_START_TIME = "charge_off_start_time";
    public static final String EXTRA_CHARGE_OFF_END_TIME = "charge_off_end_time";
    public static final String EXTRA_CHARGE_OFF_END_NAME = "red_name";
    public static final String EXTRA_CHARGE_OFF_END_COUNT = "red_count";

    SwipeRefreshLayout mSwipeLayout;
    RecyclerView mRecyclerView;
    private TextView mTvRedName, mTvRedChargeOffTotal;
    private TextView mTvNoData;
    private AlysisRedDetailAdapter mRedDetaAdap;
    private String mCouponId;
    private String mSDate, mEDate;
    private Paginate paginate;
    private String mSRedName, mERedCount;

    private String mBeginKey = "";
    private boolean loading = false;
    public List<AlysisRedDetailModel.RedDetailModel> mListRedDetail;
    private int mRequestDataSize = 0;
    private LinearLayout lineNoNet;

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
        stub.setLayoutResource(R.layout.fragment_anal_red_detail_list);
        View v = stub.inflate();
        mSwipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.list);
        int layoutOrientation = OrientationHelper.VERTICAL;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), layoutOrientation, false);
        ((LinearLayoutManager) layoutManager).setReverseLayout(false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new SlideInUpAnimator());
        mSwipeLayout.setColorSchemeResources(R.color.accent);
        mSwipeLayout.setOnRefreshListener(this);
        mTvRedName = (TextView) v.findViewById(R.id.tv_name);
        mTvRedChargeOffTotal = (TextView) v.findViewById(R.id.tv_charge_off_count);
        if (!TextUtils.isEmpty(mSRedName)) {
            mTvRedName.setText(mSRedName);
        }
        if (!TextUtils.isEmpty(mERedCount)) {
            mTvRedChargeOffTotal.setText(mERedCount);
        }
        mTvNoData = (TextView) v.findViewById(R.id.anal_tv_detail_no_data);
        lineNoNet = (LinearLayout) v.findViewById(R.id.no_net);
        lineNoNet.findViewById(R.id.btn_retry).setOnClickListener(new View.OnClickListener() {
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
        return v;
    }

    @Override
    protected void requestData() {
        if (!isAdded()) {
            return;
        }
        setContentShown(true);
        if (Utils.isNetworkAvailable(getActivity())) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mSwipeLayout.setVisibility(View.VISIBLE);
            mTvNoData.setVisibility(View.GONE);
            lineNoNet.setVisibility(View.GONE);
            fetchData(false);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mTvNoData.setVisibility(View.GONE);
            mSwipeLayout.setVisibility(View.GONE);
            lineNoNet.setVisibility(View.VISIBLE);
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

        AlysisCtrl.getRedTypeListDetail(mCouponId, mSDate, mEDate, mBeginKey, new Response.Listener<AlysisRedDetailModel>() {
            @Override
            public void onResponse(AlysisRedDetailModel redDetailModel) {
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
    public void setPageData(AlysisRedDetailModel redDetailModel, boolean isLoadMore) {
        mListRedDetail = redDetailModel.redDetailList;
        mRequestDataSize = mListRedDetail.size();
        mBeginKey = redDetailModel.mStrBeginKey;
        if (!isLoadMore) {
            setContentShown(true);
        }

        if (isLoadMore) {
            if (mRedDetaAdap != null) {
                mRedDetaAdap.notifyData(mListRedDetail);
                if (mListRedDetail.isEmpty() || mListRedDetail.size() < 10) {
                    ToastUtil.showToast(getActivity(),"已经没有更多数据了");
                }
            }
        } else {
            if (null == redDetailModel.redDetailList || redDetailModel.redDetailList.size() <= 0) {//为空
                mSwipeLayout.setVisibility(View.GONE);
                mTvNoData.setVisibility(View.VISIBLE);
                return;
            } else {
                mSwipeLayout.setVisibility(View.VISIBLE);
                mTvNoData.setVisibility(View.GONE);
            }

            mRedDetaAdap = new AlysisRedDetailAdapter(getActivity(), mListRedDetail);
            mRecyclerView.setAdapter(mRedDetaAdap);
            paginate = Paginate.with(mRecyclerView, AnalRedDetailFrag.this)
                    .setLoadingTriggerThreshold(0)
                    .addLoadingListItem(true)
                    .build();
        }
        stopRefresh();

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

    private void stopRefresh() {
        if (mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }

    protected Toolbar getToolbar() {
        Activity a = getActivity();
        if (a instanceof PlatformTopbarActivity) {
            return ((PlatformTopbarActivity) a).getToolbar();
        }
        return null;
    }
}
