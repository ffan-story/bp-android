package com.feifan.bp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.feifan.bp.base.BaseActivity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class BrowserActivity extends BaseActivity {
    private static final String TAG = BrowserActivity.class.getName();
    /**
     * 参数键名称－URL
     */
    public static final String EXTRA_KEY_URL = "url";

    private WebView mWebView;
    private View mProgressBar;

    public static void startActivity(Context context, String url) {
        Intent i = new Intent(context, BrowserActivity.class);
        i.putExtra(EXTRA_KEY_URL, url);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        mWebView = (WebView) findViewById(R.id.browser_content);
        mProgressBar = findViewById(R.id.progress_bar);
        initWeb(mWebView);

        // 载入网页
        String url = getIntent().getStringExtra(EXTRA_KEY_URL);
        LogUtil.i(TAG, url);
        mWebView.loadUrl(url);
        PlatformState.getInstance().setLastUrl(url);
    }

    @Override
    protected boolean isShowToolbar() {
        return false;
    }

    private void initWeb(WebView webView) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(new PlatformWebViewClient());
        webView.requestFocus();

    }

    private class PlatformWebViewClient extends WebViewClient {

        private static final int TIME_OUT_SECONDS = 60;
        private ScheduledExecutorService mService;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtil.i(TAG, "receive " + url);
            Uri uri = Uri.parse(url);
            String schema = uri.getScheme();
            if (schema.equals(Constants.URL_SCHEME_PLATFORM)) {

                if (url.contains(Constants.URL_PATH_LOGIN)) {      // 重新登录
                    PlatformState.getInstance().getUserProfile().clear();
                    startActivity(LaunchActivity.buildIntent());
                } else if (url.contains(Constants.URL_PATH_EXIT)) {
                    finish();
                } else if (url.contains(Constants.URL_PATH_HOME)) {
                    // 目前关闭当前界面即显示主界面
                    finish();
                }

            }

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgressBar.setVisibility(View.VISIBLE);
            if (mService == null || mService.isShutdown() || mService.isTerminated()) {
                mService = Executors.newSingleThreadScheduledExecutor();
            }
            mService.schedule(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mWebView.getProgress() < 100) {
                                mService.shutdownNow();
                                mProgressBar.setVisibility(View.GONE);
                                // TODO change ui to inform timeout here, finish now
                                finish();
                            }
                        }
                    });

                }
            }, TIME_OUT_SECONDS, TimeUnit.SECONDS);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mProgressBar.setVisibility(View.GONE);
            if (mService != null && !mService.isTerminated() && !mService.isShutdown()) {
                mService.shutdownNow();
            }

        }
    }

}
