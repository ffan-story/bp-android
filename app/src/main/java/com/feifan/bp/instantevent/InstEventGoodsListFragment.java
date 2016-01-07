package com.feifan.bp.instantevent;

import android.os.Bundle;
import android.os.Handler;
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
import com.feifan.bp.util.LogUtil;
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
    private int mIntTotalCount = 4;
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
        mSwipeLayout.setColorSchemeResources(R.color.accent);
        mSwipeLayout.setOnRefreshListener(this);
        setupPagination();
        return view;
    }

    private void fetchGoodsListData(int pageIndex){

        mStrEventId = "GP1451008488478000000";
        InstCtrl.getInstEventGoodsList(mStrEventId, "2077985", "9052789", pageIndex, new Response.Listener<InstEventGoodsListModel>() {
            @Override
            public void onResponse(InstEventGoodsListModel mModel) {

                setContentShown(true);
                mIntTotalCount = mModel.getTotalCount();
                if (mIntTotalCount<=0 ) {

                }
                if (mModel.getArryListGoodsData()== null) {
                    return;
                }
                if (arryListGoodsData == null || arryListGoodsData.size() <= 0) {
                    arryListGoodsData = new ArrayList<>();
                    arryListGoodsData = mModel.getArryListGoodsData();
                    if (arryListGoodsData != null && arryListGoodsData.size() > 0 ) {
                        //有数据
                    } else {
                        //无数据
                    }
                } else {
                    for (int i = 0; i < mModel.getArryListGoodsData().size(); i++) {
                        arryListGoodsData.add(mModel.getArryListGoodsData().get(i));
                    }
                }
                adapter.notifyDataSetChanged();
                loading = false;
                mIntTotalCount = mModel.getTotalCount();
                setContentShown(true);

//                if (arryListGoodsData.isEmpty() || arryListGoodsData.size()==0){
//                   arryListGoodsData = mModel.getArryListGoodsData();
//               }else{
//                   arryListGoodsData.addAll(mModel.getArryListGoodsData());
//               }

                LogUtil.i("congjing","size==="+arryListGoodsData.size());
                adapter.notifyData(arryListGoodsData);
            }
        });
    }

    private void setupPagination() {
        if (paginate != null) {
            paginate.unbind();
        }
        adapter = new InstEventGoodsListAdapter(getActivity(),arryListGoodsData,true);
        int layoutOrientation = OrientationHelper.VERTICAL;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), layoutOrientation, false);
        ((LinearLayoutManager) layoutManager).setReverseLayout(false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new SlideInUpAnimator());
        mRecyclerView.setAdapter(adapter);

        paginate = Paginate.with(mRecyclerView, this)
                // Set the offset from the end of the list at which the load more event needs to be triggered
                .setLoadingTriggerThreshold(0)
                .addLoadingListItem(true)
                .build();

    }

    @Override
    protected void requestData() {
        setContentShown(true);
        pageIndex = 1;
        fetchGoodsListData(pageIndex);
    }



    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
    
    @Override
    public void onLoadMore() {
        loading = true;
        pageIndex++;
        fetchGoodsListData(pageIndex);
        Toast.makeText(getActivity(), "已加载第" + (pageIndex-1 + 1) + "页", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean isLoading() {
        return loading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return pageIndex * Constants.LIST_MAX_LENGTH >=mIntTotalCount;
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        arryListGoodsData = new ArrayList<>();
        fetchGoodsListData(pageIndex);
    }

    private void stopRefresh() {
        if (mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }

}

