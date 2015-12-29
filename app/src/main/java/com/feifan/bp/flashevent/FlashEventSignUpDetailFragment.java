package com.feifan.bp.flashevent;

import android.os.Bundle;
import android.support.v7.internal.widget.ViewStubCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feifan.bp.R;
import com.feifan.bp.base.ProgressFragment;

/**
 *
 * 报名详情  审核通过
 * Created by congjing on 15-12-22.
 */
public class FlashEventSignUpDetailFragment extends ProgressFragment {
    public static final String EXTRA_EVENT_ID = "event_id";
    private LayoutInflater mInflater;
    private LinearLayout mLineGoodsNumber,mLineGoodsAmount;
    private TextView mTvVendorDiscount,mTvFeiFanDiscount,mTvStatic;
    private String mStrEventId ;
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
        mStrEventId = getArguments().getString(EXTRA_EVENT_ID);
    }


    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_flash_signup_detail);
        View view = stub.inflate();
        mLineGoodsNumber = (LinearLayout)view.findViewById(R.id.line_goods_number);
        mLineGoodsAmount = (LinearLayout)view.findViewById(R.id.line_goods_amount);
        mTvVendorDiscount = (TextView)view.findViewById(R.id.tv_vendor_discount);
        mTvFeiFanDiscount = (TextView)view.findViewById(R.id.tv_feifan_discount);
        mTvStatic = (TextView)view.findViewById(R.id.tv_signup_detail_status);
        setContView();
        return view;
    }

    @Override
    protected void requestData() {
        setContentShown(true);
    }

    FlashEventSetDetailModel.FlashEventSetDetailData myData ;
    private void setContView(){

        myData =  new FlashEventSetDetailModel(30).mEventSetDetailData;
        mTvStatic.setText(Html.fromHtml(String.format(getResources().getString(R.string.flash_signup_detail_status), getResources().getString(R.string.current_status), new FlashEventSetDetailModel(30).mStrStatus)));
        mTvVendorDiscount.setText(Html.fromHtml(String.format(getResources().getString(R.string.discount_content), getResources().getString(R.string.vendor_discount), String.valueOf(myData.mLongVendorDiscount))));
        mTvFeiFanDiscount.setText(String.format(getResources().getString(R.string.feifan_discount), myData.mLongFeifanDiscount));

        mInflater = LayoutInflater.from(getActivity());
        for (int i=0;i<3;i++){
            View mLineGoodsNumberView = mInflater.inflate(R.layout.flash_event_signup_detail_number_item, null);
            if (i ==2){
                mLineGoodsNumberView.findViewById(R.id.line).setVisibility(View.GONE);
            }

            ((TextView) mLineGoodsNumberView.findViewById(R.id.tv_goods_name)).setText(String.format(getString(R.string.flash_goods_name),myData.mStrGoodsName));
            ((TextView) mLineGoodsNumberView.findViewById(R.id.tv_goods_number)).setText(String.format(getResources().getString(R.string.flash_goods_number),myData.mIntGoodsNumber));
            ((TextView) mLineGoodsNumberView.findViewById(R.id.tv_goods_amount)).setText(String.format(getResources().getString(R.string.flash_goods_amount),myData.mLongGoodsAmount));

            mLineGoodsNumber.addView(mLineGoodsNumberView);
            View mLineGoodsAmountView  = mInflater.inflate(R.layout.flash_event_goods_discount_item, null);
            ((TextView) mLineGoodsAmountView.findViewById(R.id.tv_discount)).setText(Html.fromHtml(String.format(getResources().getString(R.string.discount_content), myData.mStrGoodsName, String.valueOf(myData.getmLongGoodsDiscount()))));
            mLineGoodsAmount.addView(mLineGoodsAmountView);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}

