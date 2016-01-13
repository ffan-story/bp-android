package com.feifan.bp.instantevent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.widget.ViewStubCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.feifan.bp.Constants;
import com.feifan.bp.R;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.widget.paginate.Paginate;
import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;


/**
 * 商品列表
 * Created by congjing on 15-12-22.
 */
public class InstEventGoodsListFragment extends ProgressFragment implements Paginate.Callbacks,SwipeRefreshLayout.OnRefreshListener {
    public static final String EXTRA_PARTAKE_EVENT_ID = "partake_event_id";
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;

    private InstEventGoodsListAdapter adapter;




    private String mStrEventId = "";
    private int pageIndex = 1;
    private boolean loading = false;
    private Paginate paginate;

    public  ArrayList<InstEventGoodsListModel.GoodsListData> arryListGoodsData =new ArrayList<>();

    public InstEventGoodsListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static InstEventGoodsListFragment newInstance() {
        InstEventGoodsListFragment fragment = new InstEventGoodsListFragment();
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
        int layoutOrientation = OrientationHelper.VERTICAL;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), layoutOrientation, false);
        ((LinearLayoutManager) layoutManager).setReverseLayout(false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new SlideInUpAnimator());
        mSwipeLayout.setColorSchemeResources(R.color.accent);
        mSwipeLayout.setOnRefreshListener(this);
        return view;
    }

    private void fetchGoodsListData(final int pageIndex,final boolean isLoadMore){

        InstCtrl.getInstEventGoodsList(mStrEventId,pageIndex, new Response.Listener<InstEventGoodsListModel>() {
            @Override
            public void onResponse(InstEventGoodsListModel model) {
                if (!isLoadMore) {
                    setContentShown(true);
                }
                if (model.getArryListGoodsData() != null && model.getArryListGoodsData().size() != 0) {
                    totalPages = calculatePage(model.getTotalCount(),Constants.LIST_MAX_LENGTH);
                    arryListGoodsData = model.getArryListGoodsData();
                    if (isLoadMore) {
                        if (adapter != null) {
                            adapter.notifyData(arryListGoodsData);
                            Toast.makeText(getActivity(), "已加载第" + pageIndex + "页 , 共" + totalPages + "页", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        adapter = new InstEventGoodsListAdapter(getActivity(), arryListGoodsData,mStrEventId);
                        mRecyclerView.setAdapter(adapter);
                        paginate = Paginate.with(mRecyclerView, InstEventGoodsListFragment.this)
                                .setLoadingTriggerThreshold(0)
                                .addLoadingListItem(true)
                                .build();
                    }
                    stopRefresh();
                    paginate.setHasMoreDataToLoad(!hasLoadedAllItems());
                }
        }
        });
    }


    @Override
    protected void requestData() {
        setContentShown(true);
        pageIndex = 1;
        fetchGoodsListData(pageIndex,false);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
    
    @Override
    public void onLoadMore() {
        loading = true;
        pageIndex++;
        fetchGoodsListData(pageIndex, true);
    }

    @Override
    public boolean isLoading() {
        return loading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return pageIndex == totalPages;
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        arryListGoodsData = new ArrayList<>();
        fetchGoodsListData(pageIndex,false);
    }

    private void stopRefresh() {
        if (mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }

    int totalPages=0;
    private int calculatePage(int totalCount, int pageSize) {
        if(totalCount<pageSize){
            return 1;
        }else{
            return totalCount/pageSize;
        }
    }
}

