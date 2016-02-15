package com.feifan.bp.marketinganalysis;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.internal.widget.ViewStubCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.widget.SpacesItemDecoration;
import com.feifan.bp.widget.paginate.Paginate;

/**
 * 营销分析明细页
 * Created by kontar on 2016/2/3.
 */
public class DetailFragment extends ProgressFragment implements Paginate.Callbacks{

    private TextView mDetailTitle,mChargeOffCount;
    private RecyclerView mRecycler;
    private RelativeLayout mNoNetView;

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_marketing_detail);
        View view = stub.inflate();
        initView(view);
        return view;
    }

    private void initView(View view) {
        mDetailTitle = (TextView) view.findViewById(R.id.detail_title);
        mChargeOffCount = (TextView) view.findViewById(R.id.detail_total_count);
        mRecycler = (RecyclerView) view.findViewById(R.id.list);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        SpacesItemDecoration space = new SpacesItemDecoration(15);
        mRecycler.addItemDecoration(space);

        mNoNetView = (RelativeLayout) view.findViewById(R.id.detail_no_net);

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
    }

    @Override
    protected void requestData() {
        if (Utils.isNetworkAvailable(getActivity())){
            mRecycler.setVisibility(View.VISIBLE);
            mNoNetView.setVisibility(View.GONE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setContentEmpty(false);
                    setContentShown(true);
                    fillView(false);
                }
            },1000);
        }else{
            setContentEmpty(false);
            setContentShown(true);
            mRecycler.setVisibility(View.GONE);
            mNoNetView.setVisibility(View.VISIBLE);
        }
    }

    private void fillView(Boolean isLoadMore) {
        mDetailTitle.setText("一条大河向东流");
        mChargeOffCount.setText("10");
        if(isLoadMore){

        }
        mRecycler.setAdapter(new DetailListAdapter(getActivity()));
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void hasLoadMore() {

    }

    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return false;
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
