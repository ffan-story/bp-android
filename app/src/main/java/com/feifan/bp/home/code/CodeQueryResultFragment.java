package com.feifan.bp.home.code;


import java.util.List;

import com.feifan.bp.base.ProgressFragment;

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
import com.android.volley.VolleyError;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;

import com.feifan.bp.Utils;
import com.feifan.bp.home.storeanalysis.SimpleBrowserFragment;
import com.feifan.bp.network.UrlFactory;
import com.feifan.bp.util.LogUtil;
import com.feifan.material.MaterialDialog;

import java.util.ArrayList;
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

    private List<String> goodsList = new ArrayList<String>();
    private CodeModel codeModel;

    /**
     * 错误信息
     */
    private TextView tv_error_text;

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
    private RelativeLayout ll_code_rule;
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
        LogUtil.i(TAG, "code========="+code);
        initDialog();

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
            ll_code_rule = (RelativeLayout) view.findViewById(R.id.ll_code_rule);
            btn_code_use = (Button) view.findViewById(R.id.btn_ticket_code_use);
            ll_code_rule.setOnClickListener(this);
            btn_code_use.setOnClickListener(this);
        }else{//提货码
            stub.setLayoutResource(R.layout.fragment_goods_code_result);
            view = stub.inflate();
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
                .setPositiveButton(R.string.common_retry_text, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                        isShowDlg = true;
                    }
                })
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
                        public void onResponse(CodeModel codeModel1) {
                            codeModel = codeModel1;
                            initCoupons(codeModel);
                            setContentShown(true);
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            setContentShown(true);

                            if(isShowDlg && isAdded()) {
                                mDialog.setMessage( volleyError.getMessage())
                                        .setPositiveButton(R.string.common_retry_text, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                mDialog.dismiss();
                                                isShowDlg = true;
                                                requestData();
                                            }
                                        })
                                        .show();

                                isShowDlg = false;
                            }
                        }

                    });
                } else {

                    setContentEmpty(true);
                }
            }else{//提货吗
                if (Utils.isNetworkAvailable(getActivity())) {
                    setContentEmpty(false);
                    CodeCtrl.queryGoodsResult(code, new Response.Listener<CodeModel>() {
                        @Override
                        public void onResponse(CodeModel codeModel) {
                            setContentShown(true);
                            LogUtil.e(TAG, codeModel.getCouponsData().getBuyTime());
//                            initCoupons(codeModel);
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            setContentShown(true);
                            if (isShowDlg && isAdded()) {
                                mDialog.setMessage(volleyError.getMessage())
                                        .setPositiveButton(R.string.common_retry_text, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                mDialog.dismiss();
                                                isShowDlg = true;
                                                requestData();
                                            }
                                        })
                                        .show();

                                isShowDlg = false;
                            }
                        }
                    });
                }
            }
        }

    }

    private void initCoupons(CodeModel codeModel){
        rl_ticket_code_result.setVisibility(View.VISIBLE);
        tv_ticket_code.setText(codeModel.getCouponsData().getCertificateNo());
        tv_ticket_code_time.setText(codeModel.getCouponsData().getBuyTime());
        tv_ticket_code_timeout.setText(codeModel.getCouponsData().getValidEndTime());
        switch (codeModel.getCouponsData().getStatus()){
            case 3:
                tv_ticket_code_status.setText("未核销");
                btn_code_use.setVisibility(View.VISIBLE);
                break;
            case 4:
                tv_ticket_code_status.setText("已核销");
                btn_code_use.setVisibility(View.GONE);
                break;
            case 6:
                tv_ticket_code_status.setText("已过期");
                btn_code_use.setVisibility(View.GONE);
                break;
        }
        tv_ticket_code_title1.setText(codeModel.getCouponsData().getSubTitle());
        tv_ticket_code_title2.setText(codeModel.getCouponsData().getTitle());

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_code_rule:
                Bundle args = new Bundle();
                args.putString(EXTRA_KEY_URL, UrlFactory.getCodeCouponeDetail(code));
                PlatformTopbarActivity.startActivity(getActivity(), SimpleBrowserFragment.class.getName(), getString(R.string.action_rule), args);
                break;

            case R.id.btn_goods_code_use://提货码
//                checkGoodsCode(code,"");
                checkGoodsCode("9351000025","10101808494030");
                break;

            case R.id.btn_ticket_code_use://券码
                checkCouponCode(code,codeModel.getCouponsData().getMemberId());
                break;
        }
    }

    /**
     * 券码核销
     * @param code
     * @param memberId
     */
    private void checkCouponCode(String code,String memberId){
        CodeCtrl.checkCouponCode(code, memberId, new Response.Listener() {
            @Override
            public void onResponse(Object o) {
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
        });
    }

    /**
     * 提货码核销
     * @param code
     * @param orderNo
     */
    private void checkGoodsCode(String code,String orderNo){
        CodeCtrl.checkGoodsCode(code, orderNo, new Response.Listener() {
            @Override
            public void onResponse(Object o) {
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
        });

    }

    public class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }

}
