package com.feifan.bp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.feifan.bp.base.BaseActivity;


public class BrowserActivity extends BaseActivity {
    private static final String TAG = BrowserActivity.class.getName();
    /** 参数键名称－URL */
    public static final String EXTRA_KEY_URL = "url";

    private WebView mWebView;

    public static void startActivity(Context context, String url) {
        Intent i = new Intent(context, BrowserActivity.class);
        i.putExtra(EXTRA_KEY_URL, url);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        mWebView = (WebView)findViewById(R.id.browser_content);
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
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtil.i(TAG, "receive " + url);
            Uri uri = Uri.parse(url);
            String schema = uri.getScheme();
            if(schema.equals(Constants.URL_SCHEME_PLATFORM)) {

                if(url.contains(Constants.URL_PATH_LOGIN)) {      // 重新登录
                    PlatformState.getInstance().getUserProfile().clear();
                    startActivity(LaunchActivity.buildIntent());
                } else if(url.contains(Constants.URL_PATH_EXIT)) {
                    finish();
                }

            }

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

}
