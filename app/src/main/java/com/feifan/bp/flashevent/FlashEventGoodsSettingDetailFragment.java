package com.feifan.bp.flashevent;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.internal.widget.ViewStubCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.base.PlatformFragment;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.util.LogUtil;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class FlashEventGoodsSettingDetailFragment extends ProgressFragment implements View.OnClickListener{
    String tag = "FlashEventGoodsSettingDetailFragment";
    private LayoutInflater mInflater;
    private ListView mLineGoodsNumber,mLineGoodsDiscount;
//    private List<FlashEventSetDetailModel.FlashEventSetDetailData> mList = new ArrayList<>();

    private List<String> mList = new ArrayList<>();
    private EditText mEdVendorDiscount ;
    private boolean isInputVendorDiscount = true;
    private boolean isInputGoodsNumber = true;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FlashEventGoodsSettingDetailFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FlashEventGoodsSettingDetailFragment newInstance() {
        FlashEventGoodsSettingDetailFragment fragment = new FlashEventGoodsSettingDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_flash_event_setting);
        View view = stub.inflate();

        mEdVendorDiscount = (EditText)view.findViewById(R.id.ed_flash_goods_vendor_discount);
        mEdVendorDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        (view.findViewById(R.id.bnt_flash_save_setting)).setOnClickListener(this);
        (view.findViewById(R.id.bnt_flash_atonce_submit)).setOnClickListener(this);

        mLineGoodsNumber = (ListView)view.findViewById(R.id.lv_goods_number);
        mLineGoodsDiscount = (ListView)view.findViewById(R.id.lv_goods_discount);
        mLineGoodsNumber.setAdapter(new Adapter(getActivity(),false));
        mLineGoodsDiscount.setAdapter(new Adapter(getActivity(),true));
        setContViewData();
        Utils.settingListViewHeight(mLineGoodsNumber);
        Utils.settingListViewHeight(mLineGoodsDiscount);
        return view;
    }

    @Override
    protected void requestData() {

        setContentShown(true);
    }

    private void setContViewData(){
        for (int i = 0; i < 6; i++) {
            mList.add("绿色植物：");
        }
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bnt_flash_save_setting://保存设置
                PlatformTopbarActivity.startActivityForResult(getActivity(), FlashEventSignUpDetailFragment.class.getName(), "报名详情");
                break;
            case R.id.bnt_flash_atonce_submit://立即提交
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
            if (isDiscount){
                final ViewDiscountHolder discountHolder;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.flash_event_goods_discount_item, parent, false);
                    discountHolder = ViewDiscountHolder.findViews(convertView);
                } else {
                    discountHolder = (ViewDiscountHolder) convertView.getTag();
                }
                discountHolder.mTvGoodsDisCountContent.setText(Html.fromHtml(String.format(getActivity().getResources().getString(R.string.discount_content), "绿色植物", "400")));
            }else{
                final ViewHolder holder;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.flash_event_goods_number_item, parent, false);
                    holder = ViewHolder.findAndCacheViews(convertView);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.mTvGoodsName.setText(mList.get(position));
                holder.mTvGoodsNumber.setText(String.format(getActivity().getResources().getString(R.string.flash_goods_number), 900));
                holder.mTvGoodsAmount.setText(String.format(getActivity().getResources().getString(R.string.flash_goods_amount), 303));
                if (position ==0){
                    holder.mTvGoodsTips.setVisibility(View.VISIBLE);
                }else{
                    holder.mTvGoodsTips.setVisibility(View.GONE);
                }
                holder.mTvGoodsPartakeNumber.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        for (int i = 0;i<mList.size();i++){
                            if (mList.get(i) != null){
                                isInputGoodsNumber = true;
                            }
                        }
                        if (isInputGoodsNumber && isInputVendorDiscount){
                            //修改button样式
                        }
                        LogUtil.i(tag,"position==="+position);
                        LogUtil.i(tag,"s==="+s.toString());
                    }
                });
            }

//            FlashEventSetDetailModel.FlashEventSetDetailData mSetDetailData = mList.get(position);
//            holder.mTvGoodsName.setText(mSetDetailData.getmStrGoodsName());
//            holder.mTvGoodsAmount.setText(mSetDetailData.getmStrGoodsAmount());
//            holder.mTvGoodsNumber.setText(mSetDetailData.getmStrGoodsNumber());
//            holder.mTvGoodsPartakeNumber.setText(mSetDetailData.getmStrGoodsPartakeNumber());
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
            holder.mTvGoodsPartakeNumber = (EditText) view.findViewById(R.id.ed_flash_goods_partake_number);
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
}
