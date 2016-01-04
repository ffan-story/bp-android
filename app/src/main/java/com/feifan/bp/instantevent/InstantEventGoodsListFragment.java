package com.feifan.bp.instantevent;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.widget.ViewStubCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.feifan.bp.R;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.widget.paginate.Paginate;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * 商品列表
 * Created by congjing on 15-12-22.
 */
public class InstantEventGoodsListFragment extends ProgressFragment implements Paginate.Callbacks,SwipeRefreshLayout.OnRefreshListener {
    public static final String EXTRA_PARTAKE_EVENT_ID = "partake_event_id";
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;
    private Handler handler;
    private Paginate paginate;
    private InstantEventGoodsListAdapter adapter;

    private int pageSize = 0;


    private String mStrEventId = "";

    public InstantEventGoodsListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static InstantEventGoodsListFragment newInstance() {
        InstantEventGoodsListFragment fragment = new InstantEventGoodsListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStrEventId = getArguments().getString(EXTRA_PARTAKE_EVENT_ID);
    }


    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_list);
        View view = stub.inflate();
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mSwipeLayout.setColorSchemeResources(R.color.accent);
        mSwipeLayout.setOnRefreshListener(this);
        setupPagination();
        return view;
    }

    private void setupPagination() {
        if (paginate != null) {
            paginate.unbind();
        }

        adapter = new InstantEventGoodsListAdapter(getActivity(), new InstantEventGoodsListModel("绿色植物","1","2134").arryListGoodsData,true);
        int layoutOrientation = OrientationHelper.VERTICAL;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), layoutOrientation, false);
        ((LinearLayoutManager) layoutManager).setReverseLayout(false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new SlideInUpAnimator());
        mRecyclerView.setAdapter(adapter);

        paginate = Paginate.with(mRecyclerView, this)
                // Set the offset from the end of the list at which the load more event needs to be triggered
                .setLoadingTriggerThreshold(1)
                .addLoadingListItem(true)
                .build();

    }

    @Override
    protected void requestData() {
        setContentShown(true);
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
        return false;
    }

    @Override
    public void onRefresh() {

    }

}

