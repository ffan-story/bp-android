package com.feifan.bp.transactionflow;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feifan.bp.R;
import com.feifan.bp.base.BaseFragment;

/**
 * Created by konta on 2016/1/7.
 */
public class InstantBuyFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_swip_erefresh,null);
        View view = inflater.inflate(R.layout.fragment_instant_buy,null);

        SwipeRefreshLayout mSwipe = (SwipeRefreshLayout) v.findViewById(R.id.instant_swipe);

        return v;
    }
}
