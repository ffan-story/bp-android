package com.feifan.bp.instantevent;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.internal.widget.ViewStubCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.base.ProgressFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品设置详情
 *congjing
 */
public class InstantEventGoodsSettingDetailFragment extends ProgressFragment implements View.OnClickListener{
    String tag = "InstantEventGoodsSettingDetailFragment";
    public static final String EXTRA_EVENT_ID = "event_id";
    public static final String EXTRA_EVENT_IS_GOODS_SETTINGDETAIL= "is_setting_detail";

    private LayoutInflater mInflater;
    private ListView mLineGoodsNumber,mLineGoodsDiscount;
    private List<InstantEventSetDetailModel.InstantEventSetDetailData> mList = new ArrayList<>();

//    private List<String> mList = new ArrayList<>();
    private EditText mEdVendorDiscount ;
    private TextView mTvFeifanDiscount;
    private TextView mTvVendorDiscountTips;
    private boolean isInputVendorDiscount = true;
    private boolean isInputGoodsNumber = true;

    private Adapter goodsNumberAdapter ,goodsDiscountAdapter;

    private RelativeLayout mRelSignupDetailRefuse;
    TextView mTvSignupStatus,mTvSignupRefuseCause;
    private String mStrEventId ;
    private boolean mISGoodsSettingDetail = false;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public InstantEventGoodsSettingDetailFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static InstantEventGoodsSettingDetailFragment newInstance() {
        InstantEventGoodsSettingDetailFragment fragment = new InstantEventGoodsSettingDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mISGoodsSettingDetail = getArguments().getBoolean(EXTRA_EVENT_IS_GOODS_SETTINGDETAIL);
        mStrEventId = getArguments().getString(EXTRA_EVENT_ID);
    }

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_instant_event_setting);
        View view = stub.inflate();
        mRelSignupDetailRefuse= (RelativeLayout)view.findViewById(R.id.rel_signup_detail_refuse);
        if (!mISGoodsSettingDetail){
            mRelSignupDetailRefuse.setVisibility(View.VISIBLE);
            mTvSignupStatus = (TextView)mRelSignupDetailRefuse.findViewById(R.id.tv_signup_detail_status);
            mTvSignupRefuseCause = (TextView)mRelSignupDetailRefuse.findViewById(R.id.tv_signup_refuse_cause);
        }else{
            mRelSignupDetailRefuse.setVisibility(View.GONE);
        }

        mEdVendorDiscount = (EditText)view.findViewById(R.id.ed_flash_goods_vendor_discount);
        mTvFeifanDiscount = (TextView)view.findViewById(R.id.feifan_discount);
        mTvVendorDiscountTips = (TextView)view.findViewById(R.id.flash_goods_vendor_discount_tips);

        mEdVendorDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    mTvVendorDiscountTips.setVisibility(View.VISIBLE);
                    mTvVendorDiscountTips.setText(getActivity().getResources().getString(R.string.instant_please_intput_vendor_discount_tips));
                    mList = new InstantEventSetDetailModel(0).arryInstantEventData;
                } else if (!TextUtils.isEmpty(s)) {
                    myData.setmLongGoodsDiscount(Long.parseLong(s.toString()));
                    if (myData.getmLongGoodsDiscount()< 0) {
                        mTvVendorDiscountTips.setVisibility(View.VISIBLE);
                        mTvVendorDiscountTips.setText("输入优惠金额不合法");
                        mList = new InstantEventSetDetailModel(0).arryInstantEventData;
                    } else {
                        mTvVendorDiscountTips.setVisibility(View.GONE);
                        mList = new InstantEventSetDetailModel(Long.parseLong(s.toString())).arryInstantEventData;
                    }
                }
                goodsDiscountAdapter.notifyDataSetChanged();
            }
        });
        (view.findViewById(R.id.bnt_instant_save_setting)).setOnClickListener(this);
        (view.findViewById(R.id.bnt_instant_atonce_submit)).setOnClickListener(this);

        mLineGoodsNumber = (ListView)view.findViewById(R.id.lv_goods_number);
        mLineGoodsDiscount = (ListView)view.findViewById(R.id.lv_goods_discount);
        goodsNumberAdapter = new Adapter(getActivity(),false);
        goodsDiscountAdapter = new Adapter(getActivity(),true);

        mLineGoodsNumber.setAdapter(goodsNumberAdapter);
        mLineGoodsDiscount.setAdapter(goodsDiscountAdapter);

        setContViewData();
        settingListViewHeight(mLineGoodsNumber);
        settingListViewHeight(mLineGoodsDiscount);
        return view;
    }

    @Override
    protected void requestData() {

        setContentShown(true);
    }

    InstantEventSetDetailModel.InstantEventSetDetailData myData ;
    private void setContViewData(){

        myData =  new InstantEventSetDetailModel(30).mEventSetDetailData;
        mList = new InstantEventSetDetailModel(30).arryInstantEventData;
        mTvFeifanDiscount.setText(String.format(getActivity().getResources().getString(R.string.instant_feifan_discount),myData.mLongFeifanDiscount)) ;
        if (!mISGoodsSettingDetail){
            mTvSignupStatus.setText(Html.fromHtml(String.format(getResources().getString(R.string.instant_signup_status), getResources().getString(R.string.instant_current_status),"已拒绝")));
            mTvSignupRefuseCause.setText(Html.fromHtml(String.format(getResources().getString(R.string.instant_signup_status), getResources().getString(R.string.instant_refuse_cause), "库存不足！！test！！！")));
        }
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bnt_instant_save_setting://保存设置
                Bundle args = new Bundle();
                args.putString(InstantEventSignUpDetailFragment.EXTRA_EVENT_ID,"23232");
                PlatformTopbarActivity.startActivityForResult(getActivity(), InstantEventSignUpDetailFragment.class.getName(), getString(R.string.register_detail));
                break;
            case R.id.bnt_instant_atonce_submit://立即提交
                break;
            default:
                break;
        }
    }


    class Adapter extends BaseAdapter {
        private final LayoutInflater mInflater;
        Context context;

        /**
         * true:合计价格
         * false:参与活动的商品数量
         */
        private boolean isDiscount = false;

        /**
         *
         * @param context
         * @param isDiscount  true 商品折扣列表  false:参与活动的商品数量
         */
        public Adapter(Context context,boolean isDiscount) {
            this.context = context;
            mInflater = LayoutInflater.from(context);
            this.isDiscount = isDiscount;
        }

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (mList.isEmpty()){
                return null ;
            }
           final InstantEventSetDetailModel.InstantEventSetDetailData mGoodsDetailData = mList.get(position);
            if (isDiscount){
                final ViewDiscountHolder discountHolder;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.instant_event_goods_discount_item, parent, false);
                    discountHolder = ViewDiscountHolder.findViews(convertView);
                } else {
                    discountHolder = (ViewDiscountHolder) convertView.getTag();
                }
                discountHolder.mTvGoodsDisCountContent.setText(Html.fromHtml(String.format(getActivity().getResources().getString(R.string.instant_discount_content), mGoodsDetailData.mStrGoodsName, mGoodsDetailData.getmLongGoodsDiscount())));
            }else{
                final ViewHolder holder;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.instant_event_goods_number_item, parent, false);
                    holder = ViewHolder.findAndCacheViews(convertView);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.mTvGoodsName.setText(String.format(getActivity().getResources().getString(R.string.instant_goods_name),mGoodsDetailData.mStrGoodsName));
                holder.mTvGoodsNumber.setText(String.format(getActivity().getResources().getString(R.string.instant_goods_number), String.valueOf(mGoodsDetailData.mIntGoodsNumber)));
                holder.mTvGoodsAmount.setText(String.format(getActivity().getResources().getString(R.string.instant_goods_amount),  String.valueOf(mGoodsDetailData.mLongGoodsAmount)));

                holder.mTvGoodsPartakeNumber.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mGoodsDetailData.mIntGoodsPartakeNumber =Integer.parseInt(s.toString());
                        if (TextUtils.isEmpty(s.toString())){
                            holder.mTvGoodsTips.setVisibility(View.INVISIBLE);
                        }else{
                            if (mGoodsDetailData.mIntGoodsPartakeNumber > mGoodsDetailData.mIntGoodsNumber){
                                holder.mTvGoodsTips.setVisibility(View.VISIBLE);
                            }else{
                                holder.mTvGoodsTips.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                });
            }
            return convertView;
        }
    }

    /**
     * 参与活动商品数量列表
     */
    private static class ViewHolder {
        private TextView mTvGoodsName;
        private EditText mTvGoodsPartakeNumber;
        private TextView mTvGoodsAmount;
        private TextView mTvGoodsNumber;
        private TextView mTvGoodsTips;
        public static ViewHolder findAndCacheViews(View view) {
            ViewHolder holder = new ViewHolder();
            holder.mTvGoodsName = (TextView) view.findViewById(R.id.tv_goods_name);
            holder.mTvGoodsPartakeNumber = (EditText) view.findViewById(R.id.ed_instant_goods_partake_number);
            holder.mTvGoodsAmount = (TextView) view.findViewById(R.id.tv_goods_amount);
            holder.mTvGoodsNumber = (TextView) view.findViewById(R.id.tv_goods_number);
            holder.mTvGoodsTips = (TextView) view.findViewById(R.id.tv_goods_tips);
            view.setTag(holder);
            return holder;
        }
    }


    /**
     * 合计价格商品列表 holder
     */
    private static class ViewDiscountHolder {
        private TextView mTvGoodsDisCountContent;
        public static ViewDiscountHolder findViews(View view) {
            ViewDiscountHolder holder = new ViewDiscountHolder();
            holder.mTvGoodsDisCountContent = (TextView) view.findViewById(R.id.tv_discount);
            view.setTag(holder);
            return holder;
        }
    }


    /**
     * setting listview height
     * @param listView
     */
    public static void settingListViewHeight(ListView listView) {
        BaseAdapter listViewAdapter =  (BaseAdapter) listView.getAdapter();
        if (listViewAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for ( int i = 0; i < listViewAdapter.getCount(); i++) {
            View listItem = listViewAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params. height = totalHeight + listView.getDividerHeight()* (listViewAdapter.getCount());
        listView.setLayoutParams(params);
    }
}
