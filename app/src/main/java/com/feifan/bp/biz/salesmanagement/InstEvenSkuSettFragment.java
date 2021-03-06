package com.feifan.bp.biz.salesmanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.internal.widget.ViewStubCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.JsonWriter;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.feifan.bp.Constants;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ui.ProgressFragment;
import com.feifan.bp.browser.SimpleBrowserFragment;
import com.feifan.bp.base.network.UrlFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品设置详情
 * congjing
 */
public class InstEvenSkuSettFragment extends ProgressFragment implements View.OnClickListener {
    public static final String EXTRA_PARTAKE_EVENT_ID = "partake_event_id";
    public static final String EXTRA_PARTAKE_GOODS_CODE = "partake_goods_code";
    public static final String EXTRA_EVENT_IS_STATUS_REFUSE = "is_status_refuse";
    public static final String EXTRA_EVENT_GOODS_ACTION = "goods_action";

    private ListView mLineGoodsNumber, mLineGoodsDiscount;
    private List<InstEvenSkuSettModel.InstantEventSetDetailData> mList = new ArrayList<>();

    private EditText mEdVendorDiscount;
    private TextView mTvFeifanDiscount;
    private TextView mTvVendorDiscountTips;

    /**
     * 商户优惠金额输入是否合法
     * true:输入合法
     * false：不合法
     */
    private boolean isVendorDiscountFlag = false;

    /**
     * true： 未输入商户优惠金额
     *
     */
    private boolean isVendorDiscountEmptyFlag = true;

    private Adapter goodsNumberAdapter, goodsDiscountAdapter;

    private RelativeLayout mRelSignupDetailRefuse;
    TextView mTvSignupStatus, mTvSignupRefuseCause;
    private String mStrEventId, mStrGoodsCode;


    /**
     * true:从商品列表-添加报名商品，false:从未提交/审核拒绝-商品详情页
     */
    private boolean isGoosActionAdd = true;

    /**
     * true:审核拒绝-设置详情页面，false:从商品列表/未提交-设置详情页面
     */
    private boolean isStatusRefuse = false;
    /**
     * 保存提交标示
     * 1：提交
     * 0：保存
     */
    private String mCommitFlag = "";

    /**
     * add:新增（商品列表-添加报名商品），edit：编辑（未提交/审核拒绝-商品详情页）
     */
    private String mGoodsAction = "";

    /**
     * 商家优惠金额
     */
    private String mStrInputDiscount = "0.00";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public InstEvenSkuSettFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static InstEvenSkuSettFragment newInstance() {
        InstEvenSkuSettFragment fragment = new InstEvenSkuSettFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStrEventId = getArguments().getString(EXTRA_PARTAKE_EVENT_ID);
        mStrGoodsCode = getArguments().getString(EXTRA_PARTAKE_GOODS_CODE);
        isGoosActionAdd = getArguments().getBoolean(EXTRA_EVENT_GOODS_ACTION, true);
        if (!isGoosActionAdd) {
            isStatusRefuse = getArguments().getBoolean(EXTRA_EVENT_IS_STATUS_REFUSE, false);
        }
    }

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_instant_event_setting);
        View view = stub.inflate();
        mRelSignupDetailRefuse = (RelativeLayout) view.findViewById(R.id.rel_signup_detail_refuse);
        if (isGoosActionAdd) {//添加新商品商品
            mRelSignupDetailRefuse.setVisibility(View.GONE);
        } else {//编辑商品
            mRelSignupDetailRefuse.setVisibility(View.VISIBLE);
            mTvSignupStatus = (TextView) mRelSignupDetailRefuse.findViewById(R.id.tv_signup_detail_status);
            mTvSignupRefuseCause = (TextView) mRelSignupDetailRefuse.findViewById(R.id.tv_signup_refuse_cause);
            mRelSignupDetailRefuse.findViewById(R.id.btn_audit_history).setOnClickListener(this);
        }

        mEdVendorDiscount = (EditText) view.findViewById(R.id.ed_flash_goods_vendor_discount);
        mTvFeifanDiscount = (TextView) view.findViewById(R.id.feifan_discount);
        mTvVendorDiscountTips = (TextView) view.findViewById(R.id.flash_goods_vendor_discount_tips);
        mEdVendorDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {//设置小数后显示两数
                if (!TextUtils.isEmpty(s.toString())) {
                    mStrInputDiscount = s.toString();
                } else {
                    mStrInputDiscount = "";
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
                InstEvenSkuSettModel.InstantEventSetDetailData mSetGoodsDetailData;
                if (TextUtils.isEmpty(mStrInputDiscount)) {
                    isVendorDiscountEmptyFlag = true;
                    isVendorDiscountFlag = false;
                    mTvVendorDiscountTips.setVisibility(View.VISIBLE);
                    mTvVendorDiscountTips.setText(getActivity().getResources().getString(R.string.instant_please_input_vendor_discount_tips));

                    myModel.mDoubleVendorDiscount = 0;
                    myData.setmDoubleGoodsDiscount();
                    mList = myModel.arryInstantEventData;
                } else if (!TextUtils.isEmpty(mStrInputDiscount) && !mList.isEmpty()) {
                    for (int i = 0; i < mList.size(); i++) {
                        mSetGoodsDetailData = mList.get(i);
                        myModel.mDoubleVendorDiscount = Double.parseDouble(mStrInputDiscount);
                        mSetGoodsDetailData.setmDoubleGoodsDiscount();
                        mList = myModel.arryInstantEventData;
                        mList.set(i, mSetGoodsDetailData);
                    }
                    if (myData.getmDoubleGoodsDiscount() < 0) {//优惠后的金额为负数
                        mTvVendorDiscountTips.setVisibility(View.VISIBLE);
                        mTvVendorDiscountTips.setText(getActivity().getResources().getString(R.string.instant_input_vendor_tips));
                        isVendorDiscountEmptyFlag = false;
                        isVendorDiscountFlag = false;
                    } else {
                        mTvVendorDiscountTips.setVisibility(View.GONE);
                        isVendorDiscountEmptyFlag = false;
                        isVendorDiscountFlag = true;
                    }
                }
                goodsDiscountAdapter.notifyDataSetChanged();
            }
        });

        if (isStatusRefuse){
            ((Button)view.findViewById(R.id.bnt_instant_atonce_submit)).setText(getActivity().getString(R.string.instant_signup_reset_comm));
        }
        (view.findViewById(R.id.bnt_instant_save_setting)).setOnClickListener(this);
        (view.findViewById(R.id.bnt_instant_atonce_submit)).setOnClickListener(this);

        mLineGoodsNumber = (ListView) view.findViewById(R.id.lv_goods_number);
        mLineGoodsDiscount = (ListView) view.findViewById(R.id.lv_goods_discount);
        return view;
    }

    @Override
    protected void requestData() {
        setContentShown(true);
        //add:新增 ；edit：编辑
        mGoodsAction = isGoosActionAdd ? "add" : "edit";
        com.feifan.bp.biz.salesmanagement.InstCtrl.getInstEventGoodsSetingDeta(mStrEventId, mStrGoodsCode, mGoodsAction, new Response.Listener<InstEvenSkuSettModel>() {
            @Override
            public void onResponse(InstEvenSkuSettModel detailModel) {
                if (detailModel != null) {
                    setContViewData(detailModel);
                    if (!isGoosActionAdd) {
                        mEdVendorDiscount.setText(String.valueOf(detailModel.mDoubleVendorDiscount));
                    }
                }
            }
        });
    }

    InstEvenSkuSettModel myModel;
    InstEvenSkuSettModel.InstantEventSetDetailData myData;

    private void setContViewData(InstEvenSkuSettModel detailModel) {
        myModel = detailModel;
        myData = detailModel.mEventSetDetailData;
        mList = detailModel.arryInstantEventData;
        // false:参与活动的商品数量
        goodsNumberAdapter = new Adapter(getActivity(), false);
        goodsDiscountAdapter = new Adapter(getActivity(), true);
        mLineGoodsNumber.setAdapter(goodsNumberAdapter);
        mLineGoodsDiscount.setAdapter(goodsDiscountAdapter);
        settingListViewHeight(mLineGoodsNumber);
        settingListViewHeight(mLineGoodsDiscount);

        mTvFeifanDiscount.setText(String.format(getActivity().getResources().getString(R.string.instant_feifan_discount), formatAmount(myModel.mDoubleFeifanDiscount)));
       if (!isGoosActionAdd){//设置状态-拒绝（显示审核历史）
           if (!isStatusRefuse){
               mTvSignupStatus.setText(Html.fromHtml(String.format(getResources().getString(R.string.instant_signup_detail_status), getResources().getString(R.string.instant_current_status), myModel.mStrApproveStatus)));
           }else{
               mTvSignupStatus.setText(Html.fromHtml(String.format(getResources().getString(R.string.instant_signup_status), getResources().getString(R.string.instant_current_status), myModel.mStrApproveStatus)));
           }
            if (TextUtils.isEmpty(myModel.mStrApproveDes) || !isStatusRefuse){
                mTvSignupRefuseCause.setVisibility(View.GONE);
            }else {
                mTvSignupRefuseCause.setVisibility(View.VISIBLE);
                mTvSignupRefuseCause.setText(Html.fromHtml(String.format(getResources().getString(R.string.instant_signup_status), getResources().getString(R.string.instant_refuse_cause), myModel.mStrApproveDes)));
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    /**
     * 是否添加了sku数量
     * true：添加合法sku数量
     * false：没有添加
     */
    private boolean isSetEvenGoodsNumb = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bnt_instant_save_setting://保存设置
            case R.id.bnt_instant_atonce_submit://立即提交
                for (int i = 0; i < mList.size(); i++) {//库存校验
                    if (mList.get(i).mIntGoodsPartakeNumber != 0 && mList.get(i).mIntGoodsPartakeNumber < mList.get(i).mIntGoodsTotal) {
                        isSetEvenGoodsNumb = true;
                    }

                    if (mList.get(i).mIntGoodsPartakeNumber > mList.get(i).mIntGoodsTotal) {//库存不足
                        Utils.showShortToast(getActivity(), getActivity().getString(R.string.instant_goods_understock_tips));
                        return;
                    }
                }

                if (!isSetEvenGoodsNumb){
                    for (int i = 0; i < mList.size(); i++) {//库存校验
                        if (mList.get(i).mIntGoodsPartakeNumber == 0) {
                            Utils.showShortToast(getActivity(), getActivity().getString(R.string.instant_goods_understock_tips1));
                            return;
                        }
                     }
                }

                if (isVendorDiscountEmptyFlag){
                    Utils.showShortToast(getActivity(), getActivity().getString(R.string.instant_please_input_vendor_discount_tips));
                    return;
                }
                if (!isVendorDiscountFlag) {
                    Utils.showShortToast(getActivity(), getActivity().getString(R.string.instant_input_vendor_tips));
                    return;
                }

                //0:保存;1:提交
                mCommitFlag = (v.getId() == R.id.bnt_instant_save_setting) ? "0" : "1";
                //add:新增 edit：编辑
                mGoodsAction = isGoosActionAdd ? "add" : "edit";
                try {
                    saveAndCommGoodsDeta(mGoodsAction, mStrEventId, mCommitFlag, myModel.mStrCode, myModel.mStrGoodsSn, writeGoodsSkuListArray(mList), mStrInputDiscount);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.btn_audit_history://验证历史
                Bundle argsRule = new Bundle();
                argsRule.putString(SimpleBrowserFragment.EXTRA_KEY_URL, UrlFactory.getUrlPathHistoryAudit(mStrEventId, mStrGoodsCode));
                PlatformTopbarActivity.startActivity(getActivity(), SimpleBrowserFragment.class.getName(), getString(R.string.instant_check_history), argsRule);
                break;

            default:
                break;
        }
    }

    /**
     * 添加活动商品-保存或者提交-设置详情
     *
     * @param goodsAction             add:新增，edit：编辑
     * @param promotionCode           --活动id
     * @param commitFlag--0:保存，1:提交
     * @param goodsCode               商品id
     * @param goodsSn
     * @param goodsSkuList
     * @param merchantCutAmount－－商家补贴
     */
    private void saveAndCommGoodsDeta(String goodsAction, String promotionCode, String commitFlag, String goodsCode, String goodsSn, String goodsSkuList, String merchantCutAmount) {
        InstCtrl.postInstGoodsDetaSaveAndComm(goodsAction, promotionCode, commitFlag, goodsCode, goodsSn, goodsSkuList, merchantCutAmount,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object o) {
                        Intent intent = new Intent();
                        if (!TextUtils.isEmpty(mCommitFlag) && mCommitFlag.equals("0")) {
                            Utils.showShortToast(getActivity().getApplicationContext(), "保存成功");
                            intent.putExtra(Constants.RETURN_STATUS, Constants.RETURN_SAVE);
                        } else {
                            Utils.showShortToast(getActivity().getApplicationContext(), "提交成功");
                            intent.putExtra(Constants.RETURN_STATUS, Constants.RETURN_COMMIT);
                        }
                        getActivity().setResult(Activity.RESULT_OK, intent);
                        getActivity().finish();
                    }
                });
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
         * @param context
         * @param isDiscount true 商品折扣列表  false:参与活动的商品数量
         */
        public Adapter(Context context, boolean isDiscount) {
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
            if (mList.isEmpty()) {
                return null;
            }
            final InstEvenSkuSettModel.InstantEventSetDetailData mGoodsDetailData = mList.get(position);
            if (isDiscount) {//true  显示商品 优惠后价格
                final ViewDiscountHolder discountHolder;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.instant_event_goods_discount_item, parent, false);
                    discountHolder = ViewDiscountHolder.findViews(convertView);
                } else {
                    discountHolder = (ViewDiscountHolder) convertView.getTag();
                }

                //设置商品合计金额，
                if (TextUtils.isEmpty(mGoodsDetailData.mStrGoodsName)) {//无商品name
                    discountHolder.mTvGoodsDisCountContent.setText(Html.fromHtml(String.format(getResources().getString(R.string.instant_discount_notname_content), formatAmount(mGoodsDetailData.getmDoubleGoodsDiscount()))));
                } else {//有商品name
                    discountHolder.mTvGoodsDisCountContent.setText(Html.fromHtml(String.format(getResources().getString(R.string.instant_discount_content),
                            mGoodsDetailData.mStrGoodsName, formatAmount(mGoodsDetailData.getmDoubleGoodsDiscount()))));
                }
            } else {//设置商品数量列表
                final ViewHolder holder;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.instant_event_goods_number_item, parent, false);
                    holder = ViewHolder.findAndCacheViews(convertView);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                //设置商品数量
                if (TextUtils.isEmpty(mGoodsDetailData.mStrGoodsName)) {//无商品name
                    holder.mTvGoodsName.setVisibility(View.INVISIBLE);
                } else {//有商品name
                    holder.mTvGoodsName.setVisibility(View.VISIBLE);
                    holder.mTvGoodsName.setText(String.format(getActivity().getResources().getString(R.string.instant_goods_name), mGoodsDetailData.mStrGoodsName));
                }

                holder.mTvGoodsNumber.setText(String.format(getActivity().getResources().getString(R.string.instant_colon), getActivity().getResources().getString(R.string.instant_goods_remain_number), String.valueOf(mGoodsDetailData.mIntGoodsTotal)));
                holder.mTvGoodsAmount.setText(String.format(getActivity().getResources().getString(R.string.instant_goods_amount), formatAmount((mGoodsDetailData.mDoubleGoodsAmount))));
                if (!isGoosActionAdd) {
                    holder.mTvGoodsPartakeNumber.setText(String.valueOf(mGoodsDetailData.mIntGoodsPartakeNumber));
                }
                holder.mTvGoodsPartakeNumber.addTextChangedListener(new TextWatcher() {
                    InstEvenSkuSettModel.InstantEventSetDetailData mSetGoodsDetailData;

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mSetGoodsDetailData = mList.get(position);
                        if (TextUtils.isEmpty(s.toString())) {
                            mSetGoodsDetailData.mIntGoodsPartakeNumber = 0;
                        } else {
                            mSetGoodsDetailData.mIntGoodsPartakeNumber = Integer.parseInt(s.toString());
                        }

                        if (mSetGoodsDetailData.mIntGoodsPartakeNumber > mSetGoodsDetailData.mIntGoodsTotal) {
                            holder.mTvGoodsTips.setVisibility(View.VISIBLE);
                            holder.mTvGoodsTips.setText(getActivity().getResources().getString(R.string.instant_goods_understock_tips));
                        } else {
                            holder.mTvGoodsTips.setVisibility(View.INVISIBLE);
                        }
                        mSetGoodsDetailData = mList.get(position);
                        mList.set(position, mSetGoodsDetailData);
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
     *
     * @param listView
     */
    public void settingListViewHeight(ListView listView) {
        BaseAdapter listViewAdapter = (BaseAdapter) listView.getAdapter();
        if (listViewAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listViewAdapter.getCount(); i++) {
            View listItem = listViewAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + listView.getDividerHeight() * (listViewAdapter.getCount());
        listView.setLayoutParams(params);
    }

    DecimalFormat mFormat = null;

    public String formatAmount(double amount) {
        if (mFormat == null) {
            mFormat = new DecimalFormat("#0.00");
        }
        return mFormat.format(amount);
    }

    /**
     * @param DetailData
     * @throws IOException
     */
    private String writeGoodsSkuListArray(List<InstEvenSkuSettModel.InstantEventSetDetailData> DetailData) throws IOException {
        if (DetailData != null && DetailData.size() > 0) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.beginArray();
            for (InstEvenSkuSettModel.InstantEventSetDetailData message : DetailData) {
                writer.beginObject();
                writer.name("skuId").value(message.mId);
                writer.name("skuSn").value(message.mSkuSn);
                writer.name("subTotal").value(message.mIntGoodsPartakeNumber);
                writer.endObject();
            }
            writer.endArray();
            writer.close();
            byte[] bytes = out.toByteArray();
            return new String(bytes);
        } else {
            return null;
        }
    }


}
