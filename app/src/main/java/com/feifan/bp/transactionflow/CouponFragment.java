package com.feifan.bp.transactionflow;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feifan.bp.R;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.widget.LoadingMoreListView;

/**
 * 通用券
 * Created by Frank on 15/11/6.
 */
public class CouponFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swipe_load,null);
        View header = inflater.inflate(R.layout.fragment_coupon,null);

        LoadingMoreListView mLoad = (LoadingMoreListView) view.findViewById(R.id.load);
        mLoad.addHeaderView(header);

        RecyclerView mDetailList = (RecyclerView) header.findViewById(R.id.detail_list);
        mDetailList.setLayoutManager(new LinearLayoutManager(getContext()));
        mDetailList.setAdapter(new DetailListAdapter());

        return view;
    }

}
