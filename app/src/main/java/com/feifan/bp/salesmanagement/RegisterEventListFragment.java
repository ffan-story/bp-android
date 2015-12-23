package com.feifan.bp.salesmanagement;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.widget.ViewStubCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.feifan.bp.PlatformTabActivity;
import com.feifan.bp.R;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.widget.paginate.Paginate;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * 可报名活动页面
 * Created by Frank on 15/12/18.
 */
public class RegisterEventListFragment extends ProgressFragment implements Paginate.Callbacks,SwipeRefreshLayout.OnRefreshListener,PlatformTabActivity.onPageSelectListener{

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;

    private boolean loading = false;
    private int page = 0;
    private Handler handler;
    private Paginate paginate;

    private int totalPages = 3;
    private int pageSize = 10;
    private EventListAdapter adapter;

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_list);
        View v = stub.inflate();
        mSwipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.list);
        mSwipeLayout.setColorSchemeResources(R.color.accent);
        mSwipeLayout.setOnRefreshListener(this);
        ((PlatformTabActivity)getActivity()).setOnPageSelectListener(this);
        handler = new Handler();
        setupPagination();
        return v;
    }

    @Override
    protected void requestData() {
        setContentShown(true);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }


    private void setupPagination() {
        if (paginate != null) {
            paginate.unbind();
        }
        handler.removeCallbacks(fakeCallback);
        adapter = new EventListAdapter(getActivity(),DataProvider.getRandomData(pageSize),false);
        loading = false;
        page = 0;

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
        stopRefresh();
    }

    @Override
    public void onRefresh() {
        Log.d("Swipe", "onRefresh");
        setupPagination();
    }

    @Override
    public void onLoadMore() {
        Log.d("Paginate", "onLoadMore");
        loading = true;
        handler.postDelayed(fakeCallback, 2000);
    }

    @Override
    public boolean isLoading() {
        return loading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return (page+1) == totalPages;
    }


    private Runnable fakeCallback = new Runnable() {
        @Override
        public void run() {
            page++;
            adapter.add(DataProvider.getRandomData(pageSize));
            loading = false;
            Toast.makeText(getActivity(),"已加载第"+(page+1)+"页",Toast.LENGTH_LONG).show();
        }
    };

    private void stopRefresh() {
        if (mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }

    @Override
    public void onPageSelected() {

    }
}
