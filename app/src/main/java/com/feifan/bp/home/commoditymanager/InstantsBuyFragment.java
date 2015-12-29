package com.feifan.bp.home.commoditymanager;

import android.os.Bundle;
import android.support.v7.internal.widget.ViewStubCompat;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feifan.bp.PlatformTabActivity;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.browser.BrowserActivity;
import com.feifan.bp.network.UrlFactory;
import com.feifan.material.MaterialDialog;

import org.json.JSONException;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;
import java.util.Random;

/**
 * 商品管理 — 闪购商品列表
 * Created by konta on 2015/12/22.
 */
public class InstantsBuyFragment extends ProgressFragment implements PlatformTabActivity.onPageSelectListener, View.OnClickListener {
    private static final String TAG = "InstantsBuyFragment";
    public static final String EXTRA_KEY_URL = "url";
    private List<InstantsBuyModle.Commodity> commodities;
    private String mUrl;
    private TextView tvCMTempSave;
    private TextView tvCMAudit;
    private TextView tvCMThroughed;
    private TextView tvCMRejected;

    private MaterialDialog mDialog;
    private transient boolean isShowDlg = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mUrl = getArguments().getString(EXTRA_KEY_URL);
        initDialog();
    }

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        ((PlatformTabActivity) getActivity()).setOnPageSelectListener(this);
        stub.setLayoutResource(R.layout.fragment_commodity_instants_buy);
        View view = stub.inflate();
        RelativeLayout rlCMTempSave = (RelativeLayout) view.findViewById(R.id.rl_instants_tempsave);
        LinearLayout llCMAudit = (LinearLayout) view.findViewById(R.id.ll_instants_buy_audit);
        LinearLayout llCMThroughed = (LinearLayout) view.findViewById(R.id.ll_instants_buy_throughed);
        RelativeLayout llCMRejected = (RelativeLayout) view.findViewById(R.id.rl_instants_buy_rejected);
        RelativeLayout llCMAdd = (RelativeLayout) view.findViewById(R.id.rl_instants_buy_add);

        tvCMTempSave = (TextView) view.findViewById(R.id.instants_buy_tempsave);
        tvCMAudit = (TextView) view.findViewById(R.id.instants_buy_audit);
        tvCMThroughed = (TextView) view.findViewById(R.id.instants_buy_throughed);
        tvCMRejected = (TextView) view.findViewById(R.id.instants_buy_rejected);
        rlCMTempSave.setOnClickListener(this);
        llCMAudit.setOnClickListener(this);
        llCMThroughed.setOnClickListener(this);
        llCMRejected.setOnClickListener(this);
        llCMAdd.setOnClickListener(this);
        return view;
    }

    private void initDialog() {
        mDialog = new MaterialDialog(getActivity())
                .setNegativeButton(R.string.common_confirm, new View.OnClickListener() {
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
        if (Utils.isNetworkAvailable(getContext())){
            CommodityManagerCtrl.getCommodityInfo(new Response.Listener<InstantsBuyModle>() {
                @Override
                public void onResponse(InstantsBuyModle instantsBuyModle) {
                    if (null != instantsBuyModle) {
                        commodities = instantsBuyModle.getCommodities();
                        initCommodityView(commodities);
                        setContentShown(true);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (isShowDlg && isAdded()) {
                        showError(volleyError);
                    }
                }
            });

        }else{
            setContentShown(false);
            setContentEmpty(true);
        }
    }

    private void initCommodityView(List<InstantsBuyModle.Commodity> commodities) {
        for (int i = 0; i < commodities.size(); i++){
            String status = commodities.get(i).status;
            int count = commodities.get(i).totalCount;
            if ("00".equals(status)){//临时保存
                tvCMTempSave.setText("" + count);
            }else if("01".equals(status)){//待审核
                tvCMAudit.setText("" + count);
            }else if("02".equals(status)){//已通过
                tvCMThroughed.setText("" + count);
            }else if("03".equals(status)){//已驳回
                tvCMRejected.setText("" + count);
            }
        }
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onPageSelected() {}

    @Override
    public void onClick(View v) {
        if(null != v.getTag()){//状态条目
        String url = UrlFactory.urlForHtmlTest(UrlFactory.getInstantsForHtml(v.getTag().toString()));
        BrowserActivity.startActivity(getContext(), url);
        }else if(v.getId() == R.id.rl_instants_buy_add){//添加商品
            //http://10.1.80.126/H5App/index.html#/commodity/select_cat_menu
            String addUrl = UrlFactory.urlForHtmlTest("H5App/index.html#/commodity/select_cat_menu?ts=".concat(new Random().nextInt() + ""));
            BrowserActivity.startActivity(getContext(), addUrl);
        }
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
        mDialog.setMessage(errorInfo)
                .show();
        isShowDlg = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestData();
    }
}
