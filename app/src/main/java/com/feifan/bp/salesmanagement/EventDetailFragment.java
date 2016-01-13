package com.feifan.bp.salesmanagement;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.internal.widget.ViewStubCompat;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.feifan.bp.PlatformState;

import android.widget.Button;
import android.widget.RelativeLayout;

import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.network.UrlFactory;
import com.feifan.bp.util.LogUtil;


/**
 * 活动详情页面
 */

public class EventDetailFragment extends ProgressFragment implements View.OnClickListener {

    private static final String TAG = "EventDetailFragment";
    public static final String EXTRA_KEY_ID = "id";
    public static final String EXTRA_KEY_NAME = "name";
    public static final String EXTRA_KEY_FLAG = "flag";

    private String mPromotionId, mPromotionName;
    private String mUrl;
    private WebView mWebView;
    private Button mBtnRegister;
    private RelativeLayout mRlBottom;
    private Boolean isShowCommit;

    public EventDetailFragment() {
    }

    public static EventDetailFragment newInstance() {
        return new EventDetailFragment();
    }

    public static EventDetailFragment newInstance(String id, String name, boolean flag) {
        Bundle args = new Bundle();
        args.putString(EXTRA_KEY_ID, id);
        args.putString(EXTRA_KEY_NAME, name);
        args.putBoolean(EXTRA_KEY_FLAG, flag);
        EventDetailFragment fragment = new EventDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPromotionId = getArguments().getString(EXTRA_KEY_ID);
        mPromotionName = getArguments().getString(EXTRA_KEY_NAME);
        isShowCommit = getArguments().getBoolean(EXTRA_KEY_FLAG);
    }

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_event_detail);
        View v = stub.inflate();
        mWebView = (WebView) v.findViewById(R.id.browser_content);
        mBtnRegister = (Button) v.findViewById(R.id.btn_register);
        mRlBottom = (RelativeLayout) v.findViewById(R.id.rl_bottom);
        if(isShowCommit){
            mRlBottom.setVisibility(View.VISIBLE);
        }else{
            mRlBottom.setVisibility(View.GONE);
        }
        mBtnRegister.setOnClickListener(this);
        initWeb(mWebView);
        return v;
    }

    @Override
    protected void requestData() {
        setContentShown(false);
        if (mPromotionId != null) {
            if (Utils.isNetworkAvailable(getActivity())) {
                setContentEmpty(false);
                mUrl = UrlFactory.promotionDetailForHtml(mPromotionId);
                mWebView.loadUrl(mUrl);
                PlatformState.getInstance().setLastUrl(mUrl);
                LogUtil.i(TAG, "mUrl==" + mUrl);
            } else {
                setContentEmpty(true);
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    private void initWeb(WebView webView) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSavePassword(false);

        // 缓存相关
        if (PlatformState.getInstance().isCacheClearable()) {
            webView.clearCache(true);
            webView.clearHistory();
            webView.clearFormData();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
        }

        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setAppCachePath(getActivity().getCacheDir().getAbsolutePath());
        webView.setWebViewClient(new PlatformWebViewClient());
        webView.requestFocus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                EventDetailFragment.this.getActivity().finish();
                RegisterDetailActivity.startActivity(getActivity(), mPromotionId, mPromotionName,false);
                break;
        }
    }

    private class PlatformWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            setContentShown(true);

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            setContentEmpty(true);
        }
    }
}
