package com.feifan.bp;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;


public class BrowserActivity extends FragmentActivity {

    /** 参数键名称－URL */
    public static final String EXTRA_KEY_URL = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        String url = getIntent().getStringExtra(EXTRA_KEY_URL);

        WebView webView = (WebView)findViewById(R.id.browser_content);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

}
