package com.feifan.bp.home.code;


import java.util.List;

import com.android.volley.VolleyError;
import com.feifan.bp.base.ProgressFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.internal.widget.ViewStubCompat;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;

import com.feifan.bp.Utils;
import com.feifan.bp.browser.SimpleBrowserFragment;
import com.feifan.bp.network.UrlFactory;
import com.feifan.bp.util.LogUtil;
import com.feifan.material.MaterialDialog;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by konta on 2015/12/17.
 */
public class CodeQueryResultFragment extends ProgressFragment implements View.OnClickListener {
    public static final String CODE = "code";
    public static final String EXTRA_KEY_IS_COUPON = "isCouponCode";
    public static final String EXTRA_KEY_URL = "url";
    public static final String TAG = "CodeQueryResultFragment";
    private String code ;
    private Boolean isCouponCode = false ;

    private List<GoodsModel.ProductInfo> productInfos;
    private String orderNo;
    private String memberId;

    /**
     * 错误信息
     */

    private Button btn_code_use;
    private LinearLayout ll_goods_code_result;
    private RelativeLayout rl_ticket_code_result;
    private TextView tv_ticket_code;
    private TextView tv_ticket_code_time;
    private TextView tv_ticket_code_timeout;
    private TextView tv_ticket_code_status;
    private TextView tv_ticket_code_title1;
    private TextView tv_ticket_code_title2;
    private TextView tv_goods_order;
    private TextView tv_goods_branch;
    private TextView tv_goods_status;
    private TextView tv_goods_total_money;
    private TextView tv_goods_integrate_money;
    private TextView tv_goods_actual_money;
    private ListView lv_goods_info;

    // dialog
    private MaterialDialog mDialog;
    private transient boolean isShowDlg = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        isCouponCode = getArguments().getBoolean(EXTRA_KEY_IS_COUPON);
        code = getArguments().getString(CODE);
        initDialog();

    }


    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        View view;
        LogUtil.e(TAG, "isCouponCode=="+isCouponCode);
        if (isCouponCode){//券码
            stub.setLayoutResource(R.layout.fragment_ticket_code_result);
            view = stub.inflate();
            rl_ticket_code_result = (RelativeLayout) view.findViewById(R.id.rl_ticket_code_result);
            tv_ticket_code = (TextView) view.findViewById(R.id.tv_ticket_code);
            tv_ticket_code_time = (TextView) view.findViewById(R.id.tv_ticket_code_time);
            tv_ticket_code_timeout = (TextView) view.findViewById(R.id.tv_ticket_code_timeout);
            tv_ticket_code_status = (TextView) view.findViewById(R.id.tv_ticket_code_status);
            tv_ticket_code_title1 = (TextView) view.findViewById(R.id.tv_ticket_code_title1);
            tv_ticket_code_title2 = (TextView) view.findViewById(R.id.tv_ticket_code_title2);
            view.findViewById(R.id.ll_code_rule).setOnClickListener(this);
            btn_code_use = (Button) view.findViewById(R.id.btn_ticket_code_use);
            btn_code_use.setOnClickListener(this);
        }else{//提货码
            stub.setLayoutResource(R.layout.fragment_code_goods);
            view = stub.inflate();
            ll_goods_code_result = (LinearLayout) view.findViewById(R.id.ll_goods_code_result);
            tv_goods_order = (TextView) view.findViewById(R.id.tv_goods_order);
            tv_goods_branch = (TextView) view.findViewById(R.id.tv_goods_branch);
            tv_goods_status = (TextView) view.findViewById(R.id.tv_goods_status);
            lv_goods_info = (ListView) view.findViewById(R.id.lv_goods_info);
            tv_goods_total_money = (TextView) view.findViewById(R.id.tv_goods_total_money);
            tv_goods_integrate_money = (TextView) view.findViewById(R.id.tv_goods_integrate_money);
            tv_goods_actual_money = (TextView) view.findViewById(R.id.tv_goods_actual_money);
            btn_code_use = (Button) view.findViewById(R.id.btn_goods_code_use);
            btn_code_use.setOnClickListener(this);
        }
        return view;
    }

    private void initDialog() {
        mDialog = new MaterialDialog(getActivity())
                .setNegativeButton(R.string.common_close_text, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                        isShowDlg = true;
                        getActivity().finish();
                    }
                });
    }

    @Override
    protected void requestData() {
        if (!TextUtils.isEmpty(code)){
            if (isCouponCode){//券码
                if (Utils.isNetworkAvailable(getActivity())) {
                    setContentEmpty(false);
                    CodeCtrl.queryCouponsResult(code, new Response.Listener<CodeModel>() {
                        @Override
                        public void onResponse(CodeModel codeModel) {
                            memberId = codeModel.getCouponsData().getMemberId();
                            initCouponsView(codeModel);
                            setContentShown(true);
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            setContentShown(true);

                            if(isShowDlg && isAdded()) {
                                mDialog.setMessage(volleyError.getMessage())
                                        .show();

                                isShowDlg = false;
                            }
                        }

                    });
                } else {

                    if(isShowDlg && isAdded()) {
                        mDialog.setMessage(getResources().getString(R.string.error_message_text_offline))
                                .show();
                        isShowDlg = false;
                    }
                    setContentShown(false);
                    setContentEmpty(true);
                }
            }else{//提货码
                if (Utils.isNetworkAvailable(getActivity())) {
                    setContentEmpty(false);
                    CodeCtrl.queryGoodsResult(code, new Response.Listener<GoodsModel>() {
                        @Override
                        public void onResponse(GoodsModel goodsModel) {
                            orderNo = goodsModel.getGoodsData().getOrderNo();
                            initGoodsView(goodsModel);
                            setContentShown(true);
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            if (isShowDlg && isAdded()) {
                                mDialog.setMessage(volleyError.getMessage())
                                        .show();

                                isShowDlg = false;
                            }
                        }
                    });
                }
            }
        }

    }

    /**
     * 填充优惠券详情页
     * @param codeModel
     */
    private void initCouponsView(CodeModel codeModel){
        rl_ticket_code_result.setVisibility(View.VISIBLE);
        tv_ticket_code.setText(codeModel.getCouponsData().getCertificateNo());
        tv_ticket_code_time.setText(codeModel.getCouponsData().getBuyTime());
        tv_ticket_code_timeout.setText(codeModel.getCouponsData().getValidEndTime());
        switch (codeModel.getCouponsData().getStatus()){
            case 3://未核销
                tv_ticket_code_status.setText(getResources().getString(R.string.chargeoff_never));
                btn_code_use.setVisibility(View.VISIBLE);
                break;
            case 4://已核销
                tv_ticket_code_status.setText(getResources().getString(R.string.chargeoff_already));
                btn_code_use.setVisibility(View.GONE);
                break;
            case 6://已过期
                tv_ticket_code_status.setText(getResources().getString(R.string.chargeoff_timeout));
                btn_code_use.setVisibility(View.GONE);
                break;
        }
        tv_ticket_code_title1.setText(codeModel.getCouponsData().getSubTitle());
        tv_ticket_code_title2.setText(codeModel.getCouponsData().getTitle());

    }

    /**
     * 填充提货码详情页
     * @param goodsModel
     */
    private void initGoodsView(GoodsModel goodsModel){
        ll_goods_code_result.setVisibility(View.VISIBLE);
        tv_goods_order.setText(goodsModel.getGoodsData().getOrderNo());
        tv_goods_branch.setText(goodsModel.getGoodsData().getStoreName());
        switch (goodsModel.getGoodsData().getSingnStatus()){
            case 1://未核销
                tv_goods_status.setText(getResources().getString(R.string.chargeoff_never));
                btn_code_use.setVisibility(View.VISIBLE);
                break;
            case 2://已核销
                tv_goods_status.setText(getResources().getString(R.string.chargeoff_already));
                btn_code_use.setVisibility(View.GONE);
                break;
            case 3://已过期
                tv_goods_status.setText(getResources().getString(R.string.chargeoff_timeout));
                btn_code_use.setVisibility(View.GONE);
                break;
        }
        productInfos  = goodsModel.getGoodsData().getProductList();
        lv_goods_info.setAdapter(new MyAdapter());
        tv_goods_total_money.setText("￥" + goodsModel.getGoodsData().getOrderAmt());
        tv_goods_integrate_money.setText("￥" + goodsModel.getGoodsData().getUsePointDiscount());
        tv_goods_actual_money.setText("￥" + goodsModel.getGoodsData().getRealPayAmt());

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_code_rule://规则
                Bundle args = new Bundle();
                args.putString(EXTRA_KEY_URL, UrlFactory.getCodeCouponeDetail(code));
                PlatformTopbarActivity.startActivity(getActivity(), SimpleBrowserFragment.class.getName(), getString(R.string.action_rule), args);
                break;

            case R.id.btn_goods_code_use://提货码
                btn_code_use.setEnabled(false);
                checkGoodsCode(code, orderNo);
                break;

            case R.id.btn_ticket_code_use://券码
                btn_code_use.setEnabled(false);
                checkCouponCode(code,memberId);
                break;
        }
    }

    /**
     * 券码核销
     * @param code
     * @param memberId
     */
    private void checkCouponCode(String code,String memberId){
        startWaiting();
        CodeCtrl.checkCouponCode(code, memberId, new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                tv_ticket_code_status.setText(getResources().getString(R.string.chargeoff_already));
                btn_code_use.setVisibility(View.GONE);
                stopWaiting();
                Utils.showShortToast(getActivity().getApplicationContext(), R.string.check_success);
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        getActivity().finish();
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 1000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                btn_code_use.setVisibility(View.GONE);
                stopWaiting();
                if (isShowDlg && isAdded()) {
                    mDialog.setMessage(volleyError.getMessage())
                            .show();
                    isShowDlg = false;
                }
            }
        });
    }

    /**
     * 提货码核销
     * @param code
     * @param orderNo
     */
    private void checkGoodsCode(String code,String orderNo){
        startWaiting();
        CodeCtrl.checkGoodsCode(code, orderNo, new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                tv_goods_status.setText(getResources().getString(R.string.chargeoff_already));
                btn_code_use.setVisibility(View.GONE);
                stopWaiting();
                Utils.showShortToast(getActivity().getApplicationContext(), R.string.check_success);
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        getActivity().finish();
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 1000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                btn_code_use.setVisibility(View.GONE);
                stopWaiting();
//                setContentShown(true);
                if (isShowDlg && isAdded()) {
                    mDialog.setMessage(volleyError.getMessage())
                            .show();
                    isShowDlg = false;
                }
            }
        });

    }

    public class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return productInfos.size();

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                convertView = View.inflate(getContext(),R.layout.item_goods_info,null);
                holder = new ViewHolder();
                holder.googsItemTitle = (TextView)convertView.findViewById(R.id.googs_item_title);
                holder.googsItemCount = (TextView)convertView.findViewById(R.id.goods_item_count);
                holder.googsItemPrice= (TextView)convertView.findViewById(R.id.googs_item_price);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }

            holder.googsItemTitle.setText(productInfos.get(position).getTitle());
            holder.googsItemCount.setText("X" + productInfos.get(position).getProductCount());
            holder.googsItemPrice.setText("￥" + productInfos.get(position).getProductPrice());

            return convertView;
        }

        @Override
        public Object getItem(int position) {

            return position;
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

    }
    public static class ViewHolder{
        private TextView googsItemTitle;
        private TextView googsItemCount;
        private TextView googsItemPrice;
    }

}
