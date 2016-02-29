package com.feifan.bp.home.commoditymanager;

import android.os.Bundle;
import android.support.v7.internal.widget.ViewStubCompat;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.feifan.bp.PlatformTabActivity;
import com.feifan.bp.R;
import com.feifan.bp.Statistics;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.browser.BrowserActivity;
import com.feifan.bp.network.UrlFactory;
import com.feifan.material.MaterialDialog;
import com.feifan.statlib.FmsAgent;

import org.json.JSONException;

import java.io.IOException;
import java.net.SocketException;
/**
 * 商品管理 — 闪购商品列表
 * Created by konta on 2015/12/22.
 */
public class InstantsBuyFragment extends ProgressFragment implements PlatformTabActivity.onPageSelectListener, View.OnClickListener {
    private static final String TAG = "InstantsBuyFragment";
    public static final String EXTRA_KEY_URL = "url";

    private InstantsBuyModle.CommodityEntry commodityEntry;
    private String mUrl;

    private TextView mTempSaveCount;
    private TextView mAuditCount;
    private TextView mThroughedCount;
    private TextView mRejectedCount;

    private MaterialDialog mDialog;
    private transient boolean isShowDlg = true;
    private boolean isResume = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mUrl = getArguments().getString(EXTRA_KEY_URL);
        initDialog();
        isResume = false;
    }

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        ((PlatformTabActivity) getActivity()).setOnPageSelectListener(this);
        stub.setLayoutResource(R.layout.fragment_commodity_instants_buy);
        View view = stub.inflate();

        mTempSaveCount = (TextView) view.findViewById(R.id.instants_tempsave_count);
        mAuditCount = (TextView) view.findViewById(R.id.instants_audit_count);
        mThroughedCount = (TextView) view.findViewById(R.id.instants_throughed_count);
        mRejectedCount = (TextView) view.findViewById(R.id.instants_rejected_count);

        view.findViewById(R.id.instants_tempsave_container).setOnClickListener(this);
        view.findViewById(R.id.instants_audit_container).setOnClickListener(this);
        view.findViewById(R.id.instants_throughed_container).setOnClickListener(this);
        view.findViewById(R.id.instants_rejected_container).setOnClickListener(this);
        view.findViewById(R.id.instants_add_container).setOnClickListener(this);

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
            setContentEmpty(false);
            CommodityManagerCtrl.getCommodityInfo(new Response.Listener<InstantsBuyModle>() {
                @Override
                public void onResponse(InstantsBuyModle instantsBuyModle) {
                    if (null != instantsBuyModle.getCommodityEntry() && isAdded()) {
                        commodityEntry = instantsBuyModle.getCommodityEntry();
                        initCommodityView(commodityEntry);
                        setContentShown(true);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    setContentShown(true);
                    if (isShowDlg && isAdded()) {
                        showError(volleyError);
                    }
                }
            });

        }else{
            if (isShowDlg && isAdded()) {
                mDialog.setMessage(getResources().getString(R.string.error_message_text_offline))
                        .show();
                isShowDlg = false;
            }
            setContentShown(false);
            setContentEmpty(true);
        }
    }

    private void initCommodityView(InstantsBuyModle.CommodityEntry commodityEntry) {
        mTempSaveCount.setText(commodityEntry.tempSave + "");
        mAuditCount.setText(commodityEntry.audit + "");
        mThroughedCount.setText(commodityEntry.throughed + "");
        mRejectedCount.setText(commodityEntry.rejected + "");
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onPageSelected() {}

    @Override
    public void onClick(View v) {
        if(Utils.isNetworkAvailable(getContext())){
            if(null != v.getTag()){//商品列表
                String url = UrlFactory.getInstantsForHtmlUrl(v.getTag().toString());
                BrowserActivity.startActivity(getContext(), url);
            }else if(v.getId() == R.id.instants_add_container){//添加商品
                //统计埋点
                FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_GOODSMANA_PUB);
                String addUrl = UrlFactory.getCommodityManageForHtmlUrl();
                BrowserActivity.startActivity(getContext(), addUrl);
            }
        }else{
            Utils.showShortToast(getActivity(), R.string.error_message_text_offline, Gravity.CENTER);
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
        if(null == errorInfo) {
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

    @Override
    public void onResume() {
        super.onResume();
        if(isResume){
            requestData();
        }
        isResume = true;
    }
}
