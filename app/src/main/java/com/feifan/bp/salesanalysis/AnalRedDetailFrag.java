package com.feifan.bp.salesanalysis;

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
import android.widget.TextView;

import com.android.volley.Response;
import com.feifan.bp.PlatformTabActivity;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.widget.paginate.Paginate;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * 红包核销明细
 * Created by apple on 16-1-21.
 */
public class AnalRedDetailFrag extends ProgressFragment implements Paginate.Callbacks,SwipeRefreshLayout.OnRefreshListener {
    public static final String EXTRA_CHARGE_OFF_ID="charge_off_id";
    public static final String EXTRA_CHARGE_OFF_START_TIME="charge_off_start_time";
    public static final String EXTRA_CHARGE_OFF_END_TIME="charge_off_end_time";

    SwipeRefreshLayout mSwipeLayout;
    RecyclerView mRecyclerView;
    private TextView mTvRedName,mTvRedChargeOffTotal;
    private AlysisRedDetailAdapter mRedDetaAdap;
    private String mCouponId;
    private String mSDate,mEDate;

    private String mBeginKey = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCouponId = getArguments().getString(EXTRA_CHARGE_OFF_ID);
        mSDate = getArguments().getString(EXTRA_CHARGE_OFF_START_TIME);
        mEDate = getArguments().getString(EXTRA_CHARGE_OFF_END_TIME);
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

        mTvRedName = (TextView)v.findViewById(R.id.tv_name);
        mTvRedChargeOffTotal = (TextView)v.findViewById(R.id.tv_charge_off_count);

        getToolbar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData();
            }
        });

        return v;
    }

    @Override
    protected void requestData() {
        setContentShown(true);
        AlysisCtrl.getRedTypeListDetail(mCouponId, mSDate, mEDate,mBeginKey, new Response.Listener<AlysisRedDetailModel>() {
            @Override
            public void onResponse(AlysisRedDetailModel redDetailModel) {
                setPageData(redDetailModel);
            }
        });
    }

    public void setPageData(AlysisRedDetailModel redDetailModel){
        mTvRedName.setText(redDetailModel.mStrRedTitle);
        mTvRedChargeOffTotal.setText(redDetailModel.mStrRedTotal);
        mBeginKey = redDetailModel.mStrBeginKey;

        if (null != redDetailModel.redDetailList && redDetailModel.redDetailList.size()>0){
            mRedDetaAdap = new AlysisRedDetailAdapter(getActivity(), redDetailModel.redDetailList);
            mRecyclerView.setAdapter(mRedDetaAdap);
        }
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return !TextUtils.isEmpty(mBeginKey);
    }

    @Override
    public void onRefresh() {

    }

    protected Toolbar getToolbar() {
        Activity a = getActivity();
        if (a instanceof PlatformTopbarActivity) {
            return ((PlatformTopbarActivity) a).getToolbar();
        }
        return null;
    }
}
