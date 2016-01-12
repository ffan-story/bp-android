package com.feifan.bp.transactionflow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feifan.bp.R;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.widget.LoadingMoreListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by konta on 2016/1/12.
 */
public class InstantDetailListFragment extends BaseFragment{

    LoadingMoreListView mLoadDetailList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_instant_detail,null);
        mLoadDetailList = (LoadingMoreListView) v.findViewById(R.id.detail_loading_more);
        mLoadDetailList.setAdapter(new InstantDetailListAdapter(getContext()));
        return v;
    }
}
