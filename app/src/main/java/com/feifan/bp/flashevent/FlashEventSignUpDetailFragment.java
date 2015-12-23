package com.feifan.bp.flashevent;

import android.os.Bundle;
import android.support.v7.internal.widget.ViewStubCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.feifan.bp.R;
import com.feifan.bp.base.PlatformFragment;
import com.feifan.bp.base.ProgressFragment;

/**
 *
 * 报名详情  审核通过
 * Created by congjing on 15-12-22.
 */
public class FlashEventSignUpDetailFragment extends ProgressFragment {
    private LayoutInflater mInflater;
    private LinearLayout mLineGoodsNumber,mLineGoodsAmount;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FlashEventSignUpDetailFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FlashEventSignUpDetailFragment newInstance() {
        FlashEventSignUpDetailFragment fragment = new FlashEventSignUpDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flash_signup_detail, container, false);

        mLineGoodsNumber = (LinearLayout)view.findViewById(R.id.line_goods_number);
        mLineGoodsAmount = (LinearLayout)view.findViewById(R.id.line_goods_amount);

        setContView();
        return view;
    }

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_flash_signup_detail);
        View view = stub.inflate();
        mLineGoodsNumber = (LinearLayout)view.findViewById(R.id.line_goods_number);
        mLineGoodsAmount = (LinearLayout)view.findViewById(R.id.line_goods_amount);
        setContView();
        return view;
    }

    @Override
    protected void requestData() {

    }

    private void setContView(){
        mInflater = LayoutInflater.from(getActivity());
        for (int i=0;i<3;i++){
            View mLineGoodsNumberView = mInflater.inflate(R.layout.flash_event_signup_detail_number_item, null);
            if (i ==2){
                mLineGoodsNumberView.findViewById(R.id.line).setVisibility(View.GONE);
            }
            mLineGoodsNumber.addView(mLineGoodsNumberView);

            View mLineGoodsAmountView  = mInflater.inflate(R.layout.flash_event_goods_discount_item, null);
            mLineGoodsAmount.addView(mLineGoodsAmountView);
        }

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}

