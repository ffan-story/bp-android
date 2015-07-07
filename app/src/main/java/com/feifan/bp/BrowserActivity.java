package com.feifan.bp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


public class BrowserActivity extends FragmentActivity {
    private static final String TAG = BrowserActivity.class.getName();
    /** 参数键名称－URL */
    public static final String EXTRA_KEY_URL = "url";

    public static void startActivity(Context context, String url) {
        Intent i = new Intent(context, BrowserActivity.class);
        i.putExtra(EXTRA_KEY_URL, url);
        context.startActivity(i);
    }

    private TextView mTitleTxv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        initViews();

        WebView webView = (WebView)findViewById(R.id.browser_content);
        initWeb(webView);

        // 载入网页
        String url = getIntent().getStringExtra(EXTRA_KEY_URL);
        LogUtil.i(TAG, url);
        webView.loadUrl(url);
    }

    private void initViews() {
        View left = findViewById(R.id.title_bar_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTitleTxv = (TextView)findViewById(R.id.title_bar_center);
    }

    private void initWeb(WebView webView) {
        webView.setWebViewClient(new PlatformWebViewClient());
        webView.setWebChromeClient(new PlatfromChromeWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
    }

    private class PlatformWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO dispose platform:// schema request here
            Log.e(getClass().getSimpleName(), "not implemented yet");

            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    private class PlatfromChromeWebViewClient extends WebChromeClient {

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            mTitleTxv.setText(title);
        }
    }

}
