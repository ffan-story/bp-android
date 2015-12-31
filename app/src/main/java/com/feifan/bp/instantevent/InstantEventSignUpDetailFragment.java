package com.feifan.bp.instantevent;

import android.os.Bundle;
import android.support.v7.internal.widget.ViewStubCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.browser.SimpleBrowserFragment;
import com.feifan.bp.network.UrlFactory;

import java.text.DecimalFormat;

/**
 *
 * 报名详情  审核通过
 * Created by congjing on 15-12-22.
 */
public class InstantEventSignUpDetailFragment extends ProgressFragment {
    public static final String EXTRA_PARTAKE_EVENT_ID = "partake_event_id";
    private LayoutInflater mInflater;
    private LinearLayout mLineGoodsNumber,mLineGoodsAmount;
    private TextView mTvVendorDiscount,mTvFeiFanDiscount,mTvStatic;
    private String mStrEventId ;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public InstantEventSignUpDetailFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static InstantEventSignUpDetailFragment newInstance() {
        InstantEventSignUpDetailFragment fragment = new InstantEventSignUpDetailFragment();
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
        stub.setLayoutResource(R.layout.fragment_instant_signup_detail);
        View view = stub.inflate();
        mLineGoodsNumber = (LinearLayout)view.findViewById(R.id.line_goods_number);
        mLineGoodsAmount = (LinearLayout)view.findViewById(R.id.line_goods_amount);
        mTvVendorDiscount = (TextView)view.findViewById(R.id.tv_vendor_discount);
        mTvFeiFanDiscount = (TextView)view.findViewById(R.id.tv_feifan_discount);
        mTvStatic = (TextView)view.findViewById(R.id.tv_signup_detail_status);
        view.findViewById(R.id.btn_audit_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle argsRule = new Bundle();
                argsRule.putString(SimpleBrowserFragment.EXTRA_KEY_URL, UrlFactory.getUrlPathHistoryAudit("1234433"));
                PlatformTopbarActivity.startActivity(getActivity(), SimpleBrowserFragment.class.getName(), getString(R.string.instant_check_history), argsRule);
            }
        });
        setContView();
        return view;
    }

    @Override
    protected void requestData() {
        setContentShown(true);
    }
    InstantEventSetDetailModel myModele;
    InstantEventSetDetailModel.InstantEventSetDetailData myData ;
    private void setContView(){
        myModele = new InstantEventSetDetailModel();
        myData = myModele.mEventSetDetailData;
        mTvStatic.setText(Html.fromHtml(String.format(getResources().getString(R.string.instant_signup_detail_status), getResources().getString(R.string.instant_current_status), myModele.mStrStatus)));
        mTvVendorDiscount.setText(Html.fromHtml(String.format(getResources().getString(R.string.instant_discount_content), getResources().getString(R.string.instant_vendor_discount), formatAmount(myModele.mDoubleVendorDiscount))));
        mTvFeiFanDiscount.setText(String.format(getResources().getString(R.string.instant_feifan_discount), formatAmount(myModele.mDoubleFeifanDiscount)));

        mInflater = LayoutInflater.from(getActivity());
        for (int i=0;i<3;i++){
            View mLineGoodsNumberView = mInflater.inflate(R.layout.instant_event_signup_detail_number_item, null);
            if (i ==2){
                mLineGoodsNumberView.findViewById(R.id.line).setVisibility(View.GONE);
            }

            ((TextView) mLineGoodsNumberView.findViewById(R.id.tv_goods_name)).setText(String.format(getString(R.string.instant_goods_name),myData.mStrGoodsName));
            ((TextView) mLineGoodsNumberView.findViewById(R.id.tv_goods_number)).setText(String.format(getResources().getString(R.string.instant_colon),String.format(getResources().getString(R.string.instant_goods_number)), myData.mIntGoodsNumber));
            ((TextView) mLineGoodsNumberView.findViewById(R.id.tv_goods_amount)).setText(String.format(getResources().getString(R.string.instant_goods_amount),formatAmount(myData.mDoubleGoodsAmount)));

            mLineGoodsNumber.addView(mLineGoodsNumberView);
            View mLineGoodsAmountView  = mInflater.inflate(R.layout.instant_event_goods_discount_item, null);
            ((TextView) mLineGoodsAmountView.findViewById(R.id.tv_discount)).setText(Html.fromHtml(String.format(getResources().getString(R.string.instant_discount_content), myData.mStrGoodsName, formatAmount(myData.getmDoubleGoodsDiscount()))));
            mLineGoodsAmount.addView(mLineGoodsAmountView);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
    DecimalFormat mFormat =null;
    public String formatAmount(double amount){
        if (mFormat == null){
            mFormat =new DecimalFormat("#0.00");
        }
        return mFormat.format(amount);
    }

}

