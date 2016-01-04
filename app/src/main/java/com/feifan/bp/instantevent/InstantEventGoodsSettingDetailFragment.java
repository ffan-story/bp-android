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
import com.feifan.bp.Utils;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.browser.SimpleBrowserFragment;
import com.feifan.bp.network.UrlFactory;
import com.feifan.bp.util.LogUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品设置详情
 *congjing
 */
public class InstantEventGoodsSettingDetailFragment extends ProgressFragment implements View.OnClickListener{
    public static final String EXTRA_PARTAKE_EVENT_ID = "partake_event_id";
    public static final String EXTRA_EVENT_IS_GOODS_SETTINGDETAIL= "is_setting_detail";

    private LayoutInflater mInflater;
    private ListView mLineGoodsNumber,mLineGoodsDiscount;
    private List<InstantEventSetDetailModel.InstantEventSetDetailData> mList = new ArrayList<>();

//    private List<String> mList = new ArrayList<>();
    private EditText mEdVendorDiscount ;
    private TextView mTvFeifanDiscount;
    private TextView mTvVendorDiscountTips;

    /**
     * true:输入合法
     * false：不合法
     */
    private boolean isVendorDiscountFlag = false;

    private Adapter goodsNumberAdapter ,goodsDiscountAdapter;

    private RelativeLayout mRelSignupDetailRefuse;
    TextView mTvSignupStatus,mTvSignupRefuseCause;
    private String mStrEventId ;

    /**
     * true:商品设置页
     * false:设置详情-拒绝（页面需显示 - 审核历史按钮）
     */
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
        mStrEventId = getArguments().getString(EXTRA_PARTAKE_EVENT_ID);
    }

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_instant_event_setting);
        View view = stub.inflate();
        mRelSignupDetailRefuse= (RelativeLayout)view.findViewById(R.id.rel_signup_detail_refuse);
        if (!mISGoodsSettingDetail){//设置详情-拒绝 （显示审核历史）
            mRelSignupDetailRefuse.setVisibility(View.VISIBLE);
            mTvSignupStatus = (TextView)mRelSignupDetailRefuse.findViewById(R.id.tv_signup_detail_status);
            mTvSignupRefuseCause = (TextView)mRelSignupDetailRefuse.findViewById(R.id.tv_signup_refuse_cause);
            mRelSignupDetailRefuse.findViewById(R.id.btn_audit_history).setOnClickListener(this);
        }else{//设置商品详情
            mRelSignupDetailRefuse.setVisibility(View.GONE);
        }

        mEdVendorDiscount = (EditText)view.findViewById(R.id.ed_flash_goods_vendor_discount);
        mTvFeifanDiscount = (TextView)view.findViewById(R.id.feifan_discount);
        mTvVendorDiscountTips = (TextView)view.findViewById(R.id.flash_goods_vendor_discount_tips);

        mEdVendorDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            String mStrInputDiscount = "0.00";
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {//设置小数后显示两数
                if (!TextUtils.isEmpty(s.toString())){
                    mStrInputDiscount = s.toString();
                }else{
                    mStrInputDiscount ="0";
                }

                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                        mStrInputDiscount = s.toString();
                        mEdVendorDiscount.setText(mStrInputDiscount);
                        mEdVendorDiscount.setSelection(mStrInputDiscount.length());
                    }
                }

                if (s.toString().trim().substring(0).equals(".")) {
                    mStrInputDiscount = "0" + s;
                    mEdVendorDiscount.setText(mStrInputDiscount);
                    mEdVendorDiscount.setSelection(2);
                }

                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mStrInputDiscount = s.subSequence(0, 1).toString();
                        mEdVendorDiscount.setText(mStrInputDiscount);
                        mEdVendorDiscount.setSelection(1);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                InstantEventSetDetailModel.InstantEventSetDetailData mSetGoodsDetailData;
                if (TextUtils.isEmpty(mStrInputDiscount)) {
                    isVendorDiscountFlag  = false;
                    mTvVendorDiscountTips.setVisibility(View.VISIBLE);
                    mTvVendorDiscountTips.setText(getActivity().getResources().getString(R.string.instant_please_input_vendor_discount_tips));

                    myModel.mDoubleVendorDiscount = 0;
                    myData.setmDoubleGoodsDiscount();
                    mList = myModel.arryInstantEventData;
                } else if (!TextUtils.isEmpty(mStrInputDiscount) && !mList.isEmpty()) {
                    for (int i=0;i<mList.size();i++){
                        mSetGoodsDetailData = mList.get(i);
                        myModel.mDoubleVendorDiscount = Double.parseDouble(mStrInputDiscount);
                        mSetGoodsDetailData.setmDoubleGoodsDiscount();
                        mList = myModel.arryInstantEventData;
                        mList.set(i,mSetGoodsDetailData);
                    }

                    if (mStrInputDiscount.equals("0")){
                        mTvVendorDiscountTips.setVisibility(View.VISIBLE);
                        mTvVendorDiscountTips.setText(getActivity().getResources().getString(R.string.instant_please_input_vendor_discount_tips));
                        isVendorDiscountFlag  = false;
                    }else if (myData.getmDoubleGoodsDiscount()<= 0) {//优惠后的金额为负数
                        mTvVendorDiscountTips.setVisibility(View.VISIBLE);
                        mTvVendorDiscountTips.setText("输入优惠金额不合法");
                        isVendorDiscountFlag  = false;
                    } else {
                        mTvVendorDiscountTips.setVisibility(View.GONE);
                        isVendorDiscountFlag  = true;
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

    InstantEventSetDetailModel myModel;
    InstantEventSetDetailModel.InstantEventSetDetailData myData ;
    private void setContViewData(){
        myModel=  new InstantEventSetDetailModel();
        myData = myModel.mEventSetDetailData;
        mList = myModel.arryInstantEventData;
        mTvFeifanDiscount.setText(String.format(getActivity().getResources().getString(R.string.instant_feifan_discount),formatAmount(myModel.mDoubleFeifanDiscount))) ;
        if (!mISGoodsSettingDetail){//设置状态-拒绝（显示审核历史）
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
            case R.id.bnt_instant_atonce_submit://立即提交
                for (int i=0;i<mList.size();i++){//库存校验
                    if (mList.get(i).mDoubleGoodsPartakeNumber == 0 ){
                        Utils.showShortToast(getActivity(), getActivity().getString(R.string.instant_please_input_goods_number));
                        return;
                    }else if(mList.get(i).mDoubleGoodsPartakeNumber > mList.get(i).mIntGoodsNumber){//库存不足
                        Utils.showShortToast(getActivity(), getActivity().getString(R.string.instant_goods_understock_tips));
                        return;
                    }

                    LogUtil.i("congjing","mList.get(i).mDoubleGoodsPartakeNumber=="+mList.get(i).mDoubleGoodsPartakeNumber);
                }

                if (!isVendorDiscountFlag){
                    Utils.showShortToast(getActivity(), getActivity().getString(R.string.instant_please_input_vendor_discount_tips));
                    return;
                }

                if (v.getId() == R.id.bnt_instant_save_setting){//保存设置
                    Bundle args = new Bundle();
                    args.putString(InstantEventSignUpDetailFragment.EXTRA_PARTAKE_EVENT_ID,"23232");
                    PlatformTopbarActivity.startActivity(getActivity(), InstantEventSignUpDetailFragment.class.getName(), getString(R.string.register_detail), args);
                }else if (v.getId() == R.id.bnt_instant_atonce_submit){//立即提交

                }

                break;
            case R.id.btn_audit_history://验证历史
                Bundle argsRule = new Bundle();
                argsRule.putString(SimpleBrowserFragment.EXTRA_KEY_URL, UrlFactory.getUrlPathHistoryAudit("1234433"));
                PlatformTopbarActivity.startActivity(getActivity(), SimpleBrowserFragment.class.getName(), getString(R.string.instant_check_history), argsRule);
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
            if (isDiscount){//true  显示商品 优惠后价格
                final ViewDiscountHolder discountHolder;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.instant_event_goods_discount_item, parent, false);
                    discountHolder = ViewDiscountHolder.findViews(convertView);
                } else {
                    discountHolder = (ViewDiscountHolder) convertView.getTag();
                }
                discountHolder.mTvGoodsDisCountContent.setText(Html.fromHtml(String.format(getActivity().getResources().getString(R.string.instant_discount_content), mGoodsDetailData.mStrGoodsName, formatAmount(mGoodsDetailData.getmDoubleGoodsDiscount()))));
            }else{//设置商品数量列表
                final ViewHolder holder;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.instant_event_goods_number_item, parent, false);
                    holder = ViewHolder.findAndCacheViews(convertView);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.mTvGoodsName.setText(String.format(getActivity().getResources().getString(R.string.instant_goods_name),mGoodsDetailData.mStrGoodsName));
                holder.mTvGoodsNumber.setText(String.format(getActivity().getResources().getString(R.string.instant_colon), getActivity().getResources().getString(R.string.instant_goods_number),String.valueOf(mGoodsDetailData.mIntGoodsNumber)));
                holder.mTvGoodsAmount.setText(String.format(getActivity().getResources().getString(R.string.instant_goods_amount), formatAmount((mGoodsDetailData.mDoubleGoodsAmount))));

                holder.mTvGoodsPartakeNumber.addTextChangedListener(new TextWatcher() {
                    InstantEventSetDetailModel.InstantEventSetDetailData mSetGoodsDetailData;
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mSetGoodsDetailData = mList.get(position);
                        if (TextUtils.isEmpty(s.toString())){
                            mSetGoodsDetailData.mDoubleGoodsPartakeNumber = 0;
                        }else{
                            mSetGoodsDetailData.mDoubleGoodsPartakeNumber = Double.parseDouble(s.toString());
                        }

                        if (mSetGoodsDetailData.mDoubleGoodsPartakeNumber > mSetGoodsDetailData.mIntGoodsNumber){
                            holder.mTvGoodsTips.setVisibility(View.VISIBLE);
                        }else{
                            holder.mTvGoodsTips.setVisibility(View.INVISIBLE);
                        }
                        mSetGoodsDetailData = mList.get(position);
                        mList.set(position,mSetGoodsDetailData);
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
    public void settingListViewHeight(ListView listView) {
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

    DecimalFormat mFormat =null;
    public String formatAmount(double amount){
        if (mFormat == null){
            mFormat =new DecimalFormat("#0.00");
        }
        return mFormat.format(amount);
    }
}
