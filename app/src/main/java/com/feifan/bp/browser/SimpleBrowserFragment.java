package com.feifan.bp.browser;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.internal.widget.ViewStubCompat;

import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.feifan.bp.Constants;
import com.feifan.bp.PlatformState;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.Statistics;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ui.ProgressFragment;

import com.feifan.bp.base.network.UrlFactory;
import com.feifan.bp.util.LogUtil;
import com.feifan.statlib.FmsAgent;

/**
 * 概览
 * 单纯用于网页的显示
 * <p/>
 * Created by Frank on 15/12/1.
 */
public class SimpleBrowserFragment extends ProgressFragment {

    private static final String TAG = "SimpleBrowserFragment";
    public static final String EXTRA_KEY_URL = "url";

    private String mUrl;
    private WebView mWebView;

    public SimpleBrowserFragment() {
    }

    public static SimpleBrowserFragment newInstance() {
        return new SimpleBrowserFragment();
    }

    public static SimpleBrowserFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(EXTRA_KEY_URL, url);
        SimpleBrowserFragment fragment = new SimpleBrowserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getArguments().getString(EXTRA_KEY_URL);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //统计埋点 概览
            FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_STOREANA_OVERVIEW);
        }
    }

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_browser);
        View v = stub.inflate();
        mWebView = (WebView) v.findViewById(R.id.browser_content);
        initWeb(mWebView);
        return v;
    }

    @Override
    protected void requestData() {
        setContentShown(false);
        if (mUrl != null) {
            if (Utils.isNetworkAvailable()) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(!isContentShown()){
                            setContentShown(true);
                            setContentEmpty(true);
                        }

                    }
                },10000);
                setContentEmpty(false);
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
        Bundle args = new Bundle();
        args.putString(EXTRA_KEY_URL, UrlFactory.storeDescriptionForHtml());
        PlatformTopbarActivity.startActivity(getActivity(), SimpleBrowserFragment.class.getName(), getString(R.string.indicator_title), args);
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
    protected MenuInfo getMenuInfo() {
        return new MenuInfo(R.id.menu_analysis_help, Constants.NO_INTEGER, R.string.indicator_title);
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
