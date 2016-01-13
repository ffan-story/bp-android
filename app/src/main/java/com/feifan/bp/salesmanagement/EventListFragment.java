package com.feifan.bp.salesmanagement;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.widget.ViewStubCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feifan.bp.PlatformTabActivity;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.widget.paginate.Paginate;
import com.feifan.bp.salesmanagement.PromotionListModel.PromotionDetailModel;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * 商户活动页面
 * Created by Frank on 15/12/18.
 */
public class EventListFragment extends ProgressFragment implements Paginate.Callbacks, SwipeRefreshLayout.OnRefreshListener, PlatformTabActivity.onPageSelectListener {

    public static final String REGISTER = "register";
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;

    private boolean isRegistered;//商家是否报名活动
    private boolean loading = false;
    private int page = 1;
    private Paginate paginate;

    private int totalPages = 0;
    private int pageSize = 10;
    private EventListAdapter adapter;

    private ArrayList<PromotionDetailModel> mPromotionList;

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_list);
        View v = stub.inflate();
        isRegistered = getArguments().getBoolean(REGISTER);
        initViews(v);
        return v;
    }

    private void initViews(View v) {
        mSwipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.list);
        mSwipeLayout.setColorSchemeResources(R.color.accent);

        int layoutOrientation = OrientationHelper.VERTICAL;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), layoutOrientation, false);
        ((LinearLayoutManager) layoutManager).setReverseLayout(false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new SlideInUpAnimator());
        mSwipeLayout.setOnRefreshListener(this);
        ((PlatformTabActivity) getActivity()).setOnPageSelectListener(this);
    }

    @Override
    protected void requestData() {
        setupPagination();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }


    private void setupPagination() {
        mPromotionList = new ArrayList<>();
        if (paginate != null) {
            paginate.unbind();
        }
        loading = false;
        page = 1;
        getPromotionList(false);
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
        page++;
        getPromotionList(true);
    }

    @Override
    public boolean isLoading() {
        return loading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return page == totalPages;
    }

    /**
     * 获取商户活动列表
     */
    private void getPromotionList(final boolean isLoadMore) {

        if (!isAdded()) {
            return;
        }
        if (loading && !isLoadMore) {
            setContentShown(false);
        }
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                setContentShown(true);
                setContentEmpty(true);
            }
        };
        Response.Listener<PromotionListModel> responseListener = new Response.Listener<PromotionListModel>() {

            @Override
            public void onResponse(PromotionListModel model) {
                if (!isLoadMore) {
                    setContentShown(true);
                }
                if (model.promotionList != null) {
                    if (model.promotionList.size() == 0) {
                        Toast.makeText(getActivity(), "无活动数据...", Toast.LENGTH_SHORT).show();
                    } else {
                        totalPages = calculatePage(model.totalSize, pageSize);
                        mPromotionList = model.promotionList;
                        if (isLoadMore) {
                            if (adapter != null) {
                                adapter.add(mPromotionList);
                                Toast.makeText(getActivity(), "已加载第" + page + "页 , 共" + totalPages + "页", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            adapter = new EventListAdapter(getActivity(), mPromotionList, isRegistered);
                            mRecyclerView.setAdapter(adapter);
                            paginate = Paginate.with(mRecyclerView, EventListFragment.this)
                                    // Set the offset from the end of the list at which the load more event needs to be triggered
                                    .setLoadingTriggerThreshold(0)
                                    .addLoadingListItem(true)
                                    .build();
                        }
                        paginate.setHasMoreDataToLoad(!hasLoadedAllItems());
                    }
                    stopRefresh();

                }
            }
        };
        //测试
        String storeId = UserProfile.getInstance().getAuthRangeId();
        String merchantId = UserProfile.getInstance().getMerchantId();
        String plazaId = UserProfile.getInstance().getPlazaId();

        int ifEnroll;
        if (isRegistered) {
            ifEnroll = 1; //活动已报名
        } else {
            ifEnroll = 0; //活动可报名
        }
        PromotionCtrl.getPromotionList(storeId, merchantId, plazaId, ifEnroll, page, pageSize, responseListener, errorListener);
    }

    private void stopRefresh() {
        if (mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }

    @Override
    public void onPageSelected() {

    }

    private int calculatePage(int totalCount, int pageSize) {
        if (totalCount < pageSize) {
            return 1;
        } else {
            return totalCount / pageSize;
        }
    }
}
