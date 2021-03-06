package com.feifan.bp.home.writeoff;


import android.os.Bundle;
import android.os.Handler;
import android.support.v7.internal.widget.ViewStubCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.Statistics;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ui.ProgressFragment;
import com.feifan.bp.browser.SimpleBrowserFragment;
import com.feifan.bp.base.network.UrlFactory;
import com.feifan.bp.base.network.response.DialogErrorListener;
import com.feifan.material.MaterialDialog;
import com.feifan.statlib.FmsAgent;

import org.json.JSONException;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;


/**
 * Created by konta on 2015/12/17.
 */
public class CodeQueryResultFragment extends ProgressFragment implements OnClickListener {
    public static final String CODE = "code";
    public static final String EXTRA_KEY_IS_COUPON = "isCouponCode";
    public static final String EXTRA_KEY_URL = "url";
    public static final String TAG = "CodeQueryResultFragment";

    // 券状态常量
    private static final int COUPON_STATUS_UNUSED = 3;     // 未核销
    private static final int COUPON_STATUS_USED = 4;       // 已核销
    private static final int COUPON_STATUS_EXPIRED = 6;    // 已过期

    // 提货码状态常量
    private static final int GOODS_STATUS_UNUSED = 1;      // 未核销
    private static final int GOODS_STATUS_USED = 2;        // 已核销
    private static final int GOODS_STATUS_EXPIRED = 3;     // 已过期
    private static final int GOODS_STATUS_REFUND = 4;      // 已过期（自动退款）

    private String code;
    private Boolean isCouponCode = false;

    private List<GoodsModel.ProductInfo> productInfos;
    private String orderNo;
    private String memberId;

    // 白名单相关-提货码
    private boolean isWhite;
    private String notice;
    private MaterialDialog noticeDlg;

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
    private TextView goodsTitle;

    // Dialog
    private MaterialDialog mDialog;
    private transient boolean isShowDlg = true;
    private MyAdapter myAdapter;

    private String plainCode;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        isCouponCode = getArguments().getBoolean(EXTRA_KEY_IS_COUPON);
        code = getArguments().getString(CODE);
        mDialog = new MaterialDialog(getActivity())
                      .setNegativeButton(R.string.common_confirm, getDialogListener(R.string.common_confirm));
        noticeDlg = new MaterialDialog(getActivity())
                        .setNegativeButton(R.string.common_cancel, getDialogListener(R.string.common_cancel))
                        .setPositiveButton(R.string.chargeoff_dialog_button_continue, getDialogListener(R.string.chargeoff_dialog_button_continue));

    }

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        View view;
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
        } else {//提货码
            stub.setLayoutResource(R.layout.fragment_code_goods);
            view = stub.inflate();
            ll_goods_code_result = (LinearLayout) view.findViewById(R.id.ll_goods_code_result);
            tv_goods_order = (TextView) view.findViewById(R.id.tv_goods_order);
            tv_goods_order.setOnClickListener(this);
            tv_goods_branch = (TextView) view.findViewById(R.id.tv_goods_branch);
            tv_goods_status = (TextView) view.findViewById(R.id.tv_goods_status);
            lv_goods_info = (ListView) view.findViewById(R.id.lv_goods_info);
            tv_goods_total_money = (TextView) view.findViewById(R.id.tv_goods_total_money);
            tv_goods_integrate_money = (TextView) view.findViewById(R.id.tv_goods_integrate_money);
            tv_goods_actual_money = (TextView) view.findViewById(R.id.tv_goods_actual_money);
            btn_code_use = (Button) view.findViewById(R.id.btn_goods_code_use);
            btn_code_use.setOnClickListener(this);

            goodsTitle = (TextView)view.findViewById(R.id.chargeoff_goods_info_title);
        }
        return view;
    }

    @Override
    protected void requestData() {
        if (!TextUtils.isEmpty(code)) {
            if (isCouponCode) {//券码
                if (Utils.isNetworkAvailable()) {
                    setContentEmpty(false);
                    CodeCtrl.queryCouponsResult(code, new Response.Listener<CodeModel>() {
                        @Override
                        public void onResponse(CodeModel codeModel) {
                            if (null != codeModel.getCouponsData() && isAdded()) {
                                memberId = codeModel.getCouponsData().getMemberId();
                                plainCode = codeModel.getCouponsData().getCertificateNo();
                                initCouponsView(codeModel);
                                setContentShown(true);
                            }

                        }

                    }, new DialogErrorListener(){
                                @Override
                                protected void preDisposeError() {
                                    super.preDisposeError();
                                    setContentShown(true);
                                }
                            }
                    );
                } else {
                    if (isShowDlg && isAdded()) {
                        mDialog.setMessage(getResources().getString(R.string.error_message_text_offline))
                                .show();
                        isShowDlg = false;
                    }
                    setContentShown(false);
                    setContentEmpty(true);
                }
            } else {//提货码
                if (Utils.isNetworkAvailable()) {
                    setContentEmpty(false);
                    CodeCtrl.queryGoodsResult(code, new Response.Listener<GoodsModel>() {
                        @Override
                        public void onResponse(GoodsModel goodsModel) {
                            if (null != goodsModel.baseInfo && isAdded()) {
                                orderNo = goodsModel.baseInfo.orderNo;
                                isWhite = goodsModel.baseInfo.userIsWhite;
                                notice = goodsModel.baseInfo.noticeMsg;
                                initGoodsView(goodsModel);
                                setContentShown(true);
                            }
                        }
                    }, new DialogErrorListener());
                }
            }
        }

    }

    /**
     * 填充优惠券详情页
     *
     * @param codeModel
     */
    private void initCouponsView(CodeModel codeModel) {
        rl_ticket_code_result.setVisibility(View.VISIBLE);
        tv_ticket_code.setText(codeModel.getCouponsData().getCertificateNo());
        tv_ticket_code_time.setText(codeModel.getCouponsData().getBuyTime());
        tv_ticket_code_timeout.setText(codeModel.getCouponsData().getValidEndTime());
        switch (codeModel.getCouponsData().getStatus()) {
            case COUPON_STATUS_UNUSED://未核销
                tv_ticket_code_status.setText(getResources().getString(R.string.chargeoff_never));
                btn_code_use.setVisibility(View.VISIBLE);
                break;
            case COUPON_STATUS_USED://已核销
                tv_ticket_code_status.setText(getResources().getString(R.string.chargeoff_already));
                btn_code_use.setVisibility(View.GONE);
                break;
            case COUPON_STATUS_EXPIRED://已过期
                tv_ticket_code_status.setText(getResources().getString(R.string.chargeoff_timeout));
                btn_code_use.setVisibility(View.GONE);
                break;
        }
        tv_ticket_code_title1.setText(codeModel.getCouponsData().getTitle());
        tv_ticket_code_title2.setText(codeModel.getCouponsData().getSubTitle());

    }

    /**
     * 填充提货码详情页
     *
     * @param goodsModel
     */
    private void initGoodsView(GoodsModel goodsModel) {
        ll_goods_code_result.setVisibility(View.VISIBLE);
        tv_goods_order.setText(goodsModel.baseInfo.orderNo);
        tv_goods_branch.setText(goodsModel.baseInfo.storeName);
        switch (goodsModel.baseInfo.singnStatus) {
            case GOODS_STATUS_UNUSED://未核销
                tv_goods_status.setText(getResources().getString(R.string.chargeoff_never));
                btn_code_use.setVisibility(View.VISIBLE);
                break;
            case GOODS_STATUS_USED://已核销
                tv_goods_status.setText(getResources().getString(R.string.chargeoff_already));
                btn_code_use.setVisibility(View.GONE);
                break;
            case GOODS_STATUS_EXPIRED://已过期
                tv_goods_status.setText(getResources().getString(R.string.chargeoff_timeout));
                btn_code_use.setVisibility(View.GONE);
                break;
            case GOODS_STATUS_REFUND://已过期（自动退款）
                tv_goods_status.setText(getResources().getString(R.string.chargeoff_refund));
                btn_code_use.setVisibility(View.GONE);
                break;
        }
        productInfos = goodsModel.baseInfo.productList;
        myAdapter = new MyAdapter();
        lv_goods_info.setAdapter(myAdapter);
        lv_goods_info.setSelector(R.drawable.goods_llistview_selector);
        tv_goods_total_money.setText(getString(R.string.chargeoff_goods_price_format, goodsModel.baseInfo.orderAmt));
        tv_goods_integrate_money.setText(getString(R.string.chargeoff_goods_price_format, goodsModel.baseInfo.usePointDiscount));
        tv_goods_actual_money.setText(getString(R.string.chargeoff_goods_price_format, goodsModel.baseInfo.realPayAmt));
        if(isWhite) {
            goodsTitle.setText(Html.fromHtml(getString(R.string.chargeoff_goods_info_white)));
        }else {
            goodsTitle.setText(R.string.chargeoff_goods_info_normal);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_code_rule://规则
                Bundle argsRule = new Bundle();
                argsRule.putString(EXTRA_KEY_URL, UrlFactory.getCodeCouponeDetail(plainCode));
                PlatformTopbarActivity.startActivity(getActivity(), SimpleBrowserFragment.class.getName(), getString(R.string.action_rule), argsRule);
                break;
            case R.id.tv_goods_order:
                Bundle argsOrder = new Bundle();
                argsOrder.putString(SimpleBrowserFragment.EXTRA_KEY_URL, UrlFactory.getOrderDetailUrl(orderNo));
                PlatformTopbarActivity.startActivity(getActivity(), SimpleBrowserFragment.class.getName(), getString(R.string.instant_check_history), argsOrder);
                break;
            case R.id.btn_goods_code_use://提货码
                if(isWhite) {
                    noticeDlg.setMessage(notice).show();
                }else{
                    //统计埋点  确认核销
                    FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_VERIFY_VERIFY);
                    btn_code_use.setEnabled(false);
                    checkGoodsCode(code, orderNo);
                }
                break;

            case R.id.btn_ticket_code_use://券码
                //统计埋点  确认核销
                FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_VERIFY_VERIFY);
                btn_code_use.setEnabled(false);
                checkCouponCode(code, memberId);
                break;
        }
    }

    /**
     * 券码核销
     *
     * @param code
     * @param memberId
     */
    private void checkCouponCode(String code, String memberId) {
        startWaiting();
        CodeCtrl.checkCouponCode(code, memberId, new Response.Listener() {
                    @Override
                    public void onResponse(Object o) {
                        if (isAdded()) {
                            tv_ticket_code_status.setText(getResources().getString(R.string.chargeoff_already));
                            btn_code_use.setVisibility(View.GONE);
                            stopWaiting();
                            Utils.showShortToast(getActivity().getApplicationContext(), R.string.check_success);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (null != getActivity()) {
                                        getActivity().finish();
                                    }
                                }
                            }, 1000);
                        }
                    }
                }, new DialogErrorListener() {
                    @Override
                    protected void preDisposeError() {
                        super.preDisposeError();
                        btn_code_use.setVisibility(View.GONE);
                        stopWaiting();
                    }
                }
//                new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                btn_code_use.setVisibility(View.GONE);
//                stopWaiting();
//                if (isShowDlg && isAdded()) {
//                    showError(volleyError);
//                }
//            }
//        }
        );
    }

    /**
     * 提货码核销
     *
     * @param code
     * @param orderNo
     */
    private void checkGoodsCode(String code, String orderNo) {
        startWaiting();
        CodeCtrl.checkGoodsCode(code, orderNo, new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                if (isAdded()) {
                    tv_goods_status.setText(getResources().getString(R.string.chargeoff_already));
                    btn_code_use.setVisibility(View.GONE);
                    stopWaiting();
                    Utils.showShortToast(getActivity().getApplicationContext(), R.string.check_success);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (null != getActivity()) {
                                getActivity().finish();
                            }
                        }
                    }, 1000);
                }
            }
        }, new DialogErrorListener(){
                    @Override
                    protected void preDisposeError() {
                        super.preDisposeError();
                        btn_code_use.setVisibility(View.GONE);
                        stopWaiting();
                    }
                }
//                new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                btn_code_use.setVisibility(View.GONE);
//                stopWaiting();
//                if (isShowDlg && isAdded()) {
//                    showError(volleyError);
//                }
//            }
//        }
        );

    }

    private void showError(VolleyError error) {
        String errorInfo = error.getMessage();
        Throwable t = error.getCause();
        if (t != null) {
            if (t instanceof JSONException) {
                errorInfo = Utils.getString(R.string.error_message_unknown);
            } else if (t instanceof IOException
                    || t instanceof SocketException) {
                errorInfo = Utils.getString(R.string.error_message_network);
            }
        }

        if(errorInfo == null) {
            if(error instanceof TimeoutError) {
                errorInfo = Utils.getString(R.string.error_message_timeout);
            }else {
                errorInfo = Utils.getString(R.string.error_message_network_link);
            }
        }
        mDialog.setMessage(errorInfo)
               .show();
        isShowDlg = false;
    }

    public class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return productInfos.size();

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.item_goods_info, null);
                holder = ViewHolder.findAndCacheViews(convertView);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (productInfos != null) {
                holder.googsItemTitle.setText(productInfos.get(position).title);
                holder.googsItemCount.setText("x" + productInfos.get(position).productCount);
                holder.googsItemPrice.setText("￥" + productInfos.get(position).productPrice);
            }
            return convertView;
        }

        @Override
        public Object getItem(int position) {

            return productInfos.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

    }

    public static class ViewHolder {
        private TextView googsItemTitle;
        private TextView googsItemCount;
        private TextView googsItemPrice;

        public static ViewHolder findAndCacheViews(View view) {
            ViewHolder holder = new ViewHolder();
            holder.googsItemTitle = (TextView) view.findViewById(R.id.googs_item_title);
            holder.googsItemCount = (TextView) view.findViewById(R.id.goods_item_count);
            holder.googsItemPrice = (TextView) view.findViewById(R.id.googs_item_price);
            view.setTag(holder);
            return holder;
        }
    }

    // Dialog Listener
    private OnClickListener mConfirmListener;    // 确定
    private OnClickListener mCancelListener;     // 取消
    private OnClickListener mContinueListener;   // 继续使用
    private OnClickListener getDialogListener(int resId) {
        switch (resId) {
            case R.string.common_confirm:
                if (mConfirmListener == null) {
                    mConfirmListener = new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                            isShowDlg = true;
                            getActivity().finish();
                        }
                    };
                }
                return mConfirmListener;
            case R.string.common_cancel:
                if(mCancelListener == null) {
                    mCancelListener = new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            noticeDlg.dismiss();
                        }
                    };
                }
                return mCancelListener;
            case R.string.chargeoff_dialog_button_continue:
                if(mContinueListener == null) {
                    mContinueListener = new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            noticeDlg.dismiss();
                            btn_code_use.setEnabled(false);
                            checkGoodsCode(code, orderNo);
                        }
                    };
                }
                return mContinueListener;
            default:
                return null;
        }

    }
}
