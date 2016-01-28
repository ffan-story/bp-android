package com.feifan.bp.salesmanagement;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.base.LazyLoadFragment;
import com.feifan.bp.salesmanagement.GoodsListModel.GoodsDetailModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frank on 15/12/21.
 */
public class GoodsListFragment extends LazyLoadFragment {

    public static final String ENROLL_STATUS = "enrollStatus";
    public static final int STATUS_NO_COMMIT = 0; //未提交状态
    public static final int STATUS_AUDIT = 1;//审核中
    public static final int STATUS_AUDIT_PASS = 2;//审核通过
    public static final int STATUS_AUDIT_DENY = 3;//审核拒绝

    private RecyclerView mProductList;
    private GoodsListCommonAdapter mCommonAdapter;
    private List<GoodsDetailModel> datas;
    private int enrollStatus;//报名状态
    private boolean isCutOff;//报名活动是否截止

    private String mPromotionId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_list, container, false);
        initViews(view);
        lazyLoad();
        return view;
    }

    private void initViews(View view) {
        mProductList = (RecyclerView) view.findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        mProductList.setItemAnimator(new DefaultItemAnimator());
        mProductList.setLayoutManager(linearLayoutManager);
        mProductList.setHasFixedSize(true);
    }


    private void getGoodsList() {
        datas = new ArrayList<>();
        enrollStatus = getArguments().getInt(ENROLL_STATUS);
        mPromotionId = ((RegisterDetailActivity) getActivity()).promotionId;
        isCutOff = ((RegisterDetailActivity) getActivity()).isCutOff;
        //测试
        String storeId = UserProfile.getInstance().getAuthRangeId();
        final String merchantId = UserProfile.getInstance().getMerchantId();
        String promotionCode = mPromotionId;

        Response.Listener<GoodsListModel> listener = new Response.Listener<GoodsListModel>() {
            @Override
            public void onResponse(GoodsListModel model) {

                if (model.goodsList != null) {
                    datas = model.goodsList;
                    mCommonAdapter = new GoodsListCommonAdapter(getActivity(), datas, enrollStatus,mPromotionId,isCutOff);
                    mProductList.setAdapter(mCommonAdapter);
                }
            }
        };
        PromotionCtrl.getGoodsList(storeId, merchantId, promotionCode, String.valueOf(enrollStatus), listener);
    }

    @Override
    protected void lazyLoad() {
        if (!isVisible) {//取消viewpager的预加载
            return;
        }
        getGoodsList();
    }
}
