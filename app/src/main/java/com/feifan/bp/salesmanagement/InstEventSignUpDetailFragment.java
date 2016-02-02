package com.feifan.bp.salesmanagement;

import android.os.Bundle;
import android.support.v7.internal.widget.ViewStubCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.browser.SimpleBrowserFragment;
import com.feifan.bp.network.UrlFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 报名详情  审核通过
 * Created by congjing on 15-12-22.
 */
public class InstEventSignUpDetailFragment extends ProgressFragment {
    public static final String EXTRA_PARTAKE_EVENT_ID = "partake_event_id";
    public static final String EXTRA_PARTAKE_GOODS_CODE = "partake_goods_code";
    public static final String EXTRA_PARTAKE_GOODS_IS_REFUSE= "partake_goods_is_refuse";
    private LayoutInflater mInflater;
    private LinearLayout mLineGoodsNumber,mLineGoodsAmount;
    private TextView mTvVendorDiscount,mTvFeiFanDiscount,mTvStatic;
    private String mStrEventId ,mStrGoodsCode;
    private boolean isRefuse = false;
    private List<com.feifan.bp.salesmanagement.InstEvenSkuSettModel.InstantEventSetDetailData> mList = new ArrayList<>();
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public InstEventSignUpDetailFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static InstEventSignUpDetailFragment newInstance() {
        InstEventSignUpDetailFragment fragment = new InstEventSignUpDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStrEventId = getArguments().getString(EXTRA_PARTAKE_EVENT_ID);
        mStrGoodsCode = getArguments().getString(EXTRA_PARTAKE_GOODS_CODE);
        isRefuse = getArguments().getBoolean(EXTRA_PARTAKE_GOODS_IS_REFUSE);
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
                argsRule.putString(SimpleBrowserFragment.EXTRA_KEY_URL, UrlFactory.getUrlPathHistoryAudit(mStrEventId, mStrGoodsCode));
                PlatformTopbarActivity.startActivity(getActivity(), SimpleBrowserFragment.class.getName(), getString(R.string.instant_check_history), argsRule);
            }
        });
        return view;
    }

    @Override
    protected void requestData(){
        if (!isAdded()){
            return;
        }
        setContentShown(true);
        String mgoodsAction ="edit";
        com.feifan.bp.salesmanagement.InstCtrl.getInstEventGoodsSetingDeta(mStrEventId, mStrGoodsCode, mgoodsAction, new Response.Listener<com.feifan.bp.salesmanagement.InstEvenSkuSettModel>() {
            @Override
            public void onResponse(com.feifan.bp.salesmanagement.InstEvenSkuSettModel detailModel) {
                if (detailModel != null) {
                    setContViewData(detailModel);
                }
            }
        });
    }

    com.feifan.bp.salesmanagement.InstEvenSkuSettModel.InstantEventSetDetailData myData ;
    private void setContViewData(com.feifan.bp.salesmanagement.InstEvenSkuSettModel detailModel){
         myData = detailModel.mEventSetDetailData;
         mList = detailModel.arryInstantEventData;

        if (isRefuse){
            mTvStatic.setText(Html.fromHtml(String.format(getResources().getString(R.string.instant_signup_status), getResources().getString(R.string.instant_current_status), detailModel.mStrApproveStatus)));
        }else{
            mTvStatic.setText(Html.fromHtml(String.format(getResources().getString(R.string.instant_signup_detail_status), getResources().getString(R.string.instant_current_status), detailModel.mStrApproveStatus)));
        }
        mTvVendorDiscount.setText(Html.fromHtml(String.format(getResources().getString(R.string.instant_discount_content), getResources().getString(R.string.instant_vendor_discount), formatAmount(detailModel.mDoubleVendorDiscount))));
        mTvFeiFanDiscount.setText(String.format(getResources().getString(R.string.instant_feifan_discount), formatAmount(detailModel.mDoubleFeifanDiscount)));

        mInflater = LayoutInflater.from(getActivity());
        for (int i=0;i<mList.size();i++){
            View mLineGoodsNumberView = mInflater.inflate(R.layout.instant_event_signup_detail_number_item, null);
            if (i ==2){
                mLineGoodsNumberView.findViewById(R.id.line).setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(mList.get(i).mStrGoodsName)){
                ((TextView) mLineGoodsNumberView.findViewById(R.id.tv_goods_name)).setText(String.valueOf(mList.get(i).mIntGoodsPartakeNumber));

            }else{
                ((TextView) mLineGoodsNumberView.findViewById(R.id.tv_goods_name)).setText(String.format(getString(R.string.instant_colon), mList.get(i).mStrGoodsName,mList.get(i).mIntGoodsPartakeNumber));

            }

             ((TextView) mLineGoodsNumberView.findViewById(R.id.tv_goods_number)).setText(String.format(getResources().getString(R.string.instant_colon),String.format(getResources().getString(R.string.instant_goods_remain_number)), mList.get(i).mIntGoodsTotal));
            ((TextView) mLineGoodsNumberView.findViewById(R.id.tv_goods_amount)).setText(String.format(getResources().getString(R.string.instant_goods_amount),formatAmount(myData.mDoubleGoodsAmount)));

            mLineGoodsNumber.addView(mLineGoodsNumberView);
            View mLineGoodsAmountView  = mInflater.inflate(R.layout.instant_event_goods_discount_item, null);
            ((TextView) mLineGoodsAmountView.findViewById(R.id.tv_discount)).
                    setText(Html.fromHtml(String.format(getResources().getString(R.string.instant_discount_content),
                            mList.get(i).mStrGoodsName, formatAmount(mList.get(i).getmDoubleGoodsDiscount()))));

            if (TextUtils.isEmpty(mList.get(i).mStrGoodsName)){
                ((TextView) mLineGoodsAmountView.findViewById(R.id.tv_discount)).
                        setText(Html.fromHtml(String.format(getResources().getString(R.string.instant_discount_notname_content),
                        formatAmount(mList.get(i).getmDoubleGoodsDiscount()))));

            }else{
                ((TextView) mLineGoodsAmountView.findViewById(R.id.tv_discount)).
                        setText(Html.fromHtml(String.format(getResources().getString(R.string.instant_discount_content),
                                mList.get(i).mStrGoodsName, formatAmount(mList.get(i).getmDoubleGoodsDiscount()))));
            }
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

