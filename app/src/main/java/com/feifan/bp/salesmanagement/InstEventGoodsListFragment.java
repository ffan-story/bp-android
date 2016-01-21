package com.feifan.bp.salesmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.widget.ViewStubCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.feifan.bp.Constants;
import com.feifan.bp.PlatformTabActivity;
import com.feifan.bp.R;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.home.commoditymanager.BrandFragment;
import com.feifan.bp.home.commoditymanager.InstantsBuyFragment;
import com.feifan.bp.network.UrlFactory;
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
    private  RelativeLayout mRelEmpty;
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
        stub.setLayoutResource(R.layout.fragment_add_goods_list);
        View view = stub.inflate();
        mRelEmpty = (RelativeLayout)view.findViewById(R.id.rel_empty);
        mRelEmpty.findViewById(R.id.btn_empty).setOnClickListener(new View.OnClickListener() {//去商品管理
            @Override
            public void onClick(View v) {
                Bundle fragmentArgs = new PlatformTabActivity.ArgsBuilder()
                        .addFragment(InstantsBuyFragment.class.getName(), getString(R.string.commodity_instants_buy))
                        .addArgument(InstantsBuyFragment.class.getName(), InstantsBuyFragment.EXTRA_KEY_URL, UrlFactory.storeOverviewForHtml())
                        .addFragment(BrandFragment.class.getName(), getString(R.string.commodity_brand))
                        .addArgument(BrandFragment.class.getName(), BrandFragment.EXTRA_KEY_URL,UrlFactory.visitorsAnalysisForHtml())
                        .build();
                Intent intent = PlatformTabActivity.buildIntent(getContext(), getString(R.string.index_commodity_text), fragmentArgs);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

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

    /**
     *
     * @param pageIndex
     * @param isLoadMore
     */
    private void fetchGoodsListData(final int pageIndex,final boolean isLoadMore){
        if (!isAdded()) {
            return;
        }
        LogUtil.i("congjing","loading=="+loading);
        LogUtil.i("congjing","isLoadMore=="+isLoadMore);
        if (loading && !isLoadMore) {
            setContentShown(false);
        }

        InstCtrl.getInstEventGoodsList(mStrEventId, pageIndex, new Response.Listener<InstEventGoodsListModel>() {
            @Override
            public void onResponse(InstEventGoodsListModel model) {
                if (!isLoadMore) {
                    setContentShown(true);
                }
                if (model.getArryListGoodsData() != null && model.getArryListGoodsData().size() != 0) {
                    mRelEmpty.setVisibility(View.GONE);
                    mSwipeLayout.setVisibility(View.VISIBLE);
                    totalPages = calculatePage(model.getTotalCount(), Constants.LIST_MAX_LENGTH);
                    arryListGoodsData = model.getArryListGoodsData();
                    if (isLoadMore) {
                        if (adapter != null) {
                            adapter.notifyData(arryListGoodsData);
                            Toast.makeText(getActivity(), String.format(getActivity().getString(R.string.instant_loading_page), pageIndex, totalPages), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        adapter = new InstEventGoodsListAdapter(getActivity(), arryListGoodsData, mStrEventId);
                        mRecyclerView.setAdapter(adapter);
                        paginate = Paginate.with(mRecyclerView, InstEventGoodsListFragment.this)
                                .setLoadingTriggerThreshold(0)
                                .addLoadingListItem(true)
                                .build();
                    }
                    stopRefresh();
                    loading = hasLoadedAllItems();
                    paginate.setHasMoreDataToLoad(!hasLoadedAllItems());
                } else {
                    mRelEmpty.setVisibility(View.VISIBLE);
                    mSwipeLayout.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    protected void requestData() {
        setContentShown(true);
        pageIndex = 1;
        fetchGoodsListData(pageIndex, false);
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
        loading = false;
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
            if (totalCount%pageSize !=0){
                return totalCount/pageSize+1;
            }else{
                return totalCount/pageSize;
            }
        }
    }
}