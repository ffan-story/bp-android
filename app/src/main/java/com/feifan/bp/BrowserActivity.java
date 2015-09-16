package com.feifan.bp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.feifan.bp.account.AccountManager;
import com.feifan.bp.base.BaseActivity;
import com.feifan.bp.crop.Crop;
import com.feifan.bp.net.NetUtils;
import com.feifan.bp.net.UploadHttpClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;


public class BrowserActivity extends BaseActivity {
    private static final String TAG = BrowserActivity.class.getName();
    /**
     * 参数键名称－URL
     */
    public static final String EXTRA_KEY_URL = "url";
    public static final String EXTRA_KEY_STAFF_MANAGE = "staff";

    private WebView mWebView;
    private boolean mIsStaffManagementPage = false;
    private int mWebViewProgress = 0;

    public static void startActivity(Context context, String url) {
        startActivity(context, url, false);
    }

    public static void startActivity(Context context, String url, boolean staffManage) {
        Intent i = new Intent(context, BrowserActivity.class);
        i.putExtra(EXTRA_KEY_URL, url);
        i.putExtra(EXTRA_KEY_STAFF_MANAGE, staffManage);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        mWebView = (WebView) findViewById(R.id.browser_content);
        initWeb(mWebView);

        // 载入网页
        String url = getIntent().getStringExtra(EXTRA_KEY_URL);
        mIsStaffManagementPage = getIntent().getBooleanExtra(EXTRA_KEY_STAFF_MANAGE, false);
        LogUtil.i(TAG, url);
        LogUtil.i(TAG, "staff=" + mIsStaffManagementPage);
        mWebView.loadUrl(url);
        PlatformState.getInstance().setLastUrl(url);
        LogUtil.i(TAG, "loadUrl()");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mIsStaffManagementPage && mShowToolbarItem) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_staff_manage, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void onClick(MenuItem item) {
        LogUtil.i(TAG, "onClick()");
        int id = item.getItemId();
        switch (id) {
            case R.id.action_staff:
                if (mWebViewProgress < 100) {
                    break;
                }
                LogUtil.i(TAG, "onOptionsItemSelected()  load staff url");
                String url = NetUtils.getUrlFactory().staffAddForHtml(this);
                mWebView.loadUrl(url);
//                BrowserActivity.startActivity(this, url);
                break;
            default:

        }
    }

    @Override
    protected boolean isShowToolbar() {
        return true;
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.ic_left_arrow);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_staff:
                        break;
                }
                return false;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initWeb(WebView webView) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.addJavascriptInterface(new JavaScriptInterface(), "android");

        webView.setWebViewClient(new PlatformWebViewClient());
        webView.setWebChromeClient(new PlatformWebChromeClient());
        webView.requestFocus();

    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private boolean mShowToolbarItem = false;

    private class PlatformWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mWebViewProgress = newProgress;
            LogUtil.i(TAG, "onProgressChanged() progress=" + newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            LogUtil.i(TAG, "onReceiveTitle() title=" + title);
            if (view.getProgress() == 100) {
                getToolbar().setTitle(title);
                if (getString(R.string.index_staff_list).equals(title)) {
                    mShowToolbarItem = true;
                } else {
                    mShowToolbarItem = false;
                }
                supportInvalidateOptionsMenu();
            }
        }
    }

    private class PlatformWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtil.i(TAG, "receive " + url);
            Uri uri = Uri.parse(url);
            String schema = uri.getScheme();
            if (schema.equals(Constants.URL_SCHEME_PLATFORM)) {

                if (url.contains(Constants.URL_PATH_LOGIN)) {      // 重新登录
                    AccountManager.instance(BrowserActivity.this).clear();
                    startActivity(LaunchActivity.buildIntent(BrowserActivity.this));
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
            LogUtil.i(TAG, "onPageStarted() progress=" + view.getProgress());
            showProgressBar(true);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            LogUtil.i(TAG, "onPageFinished() progress=" + view.getProgress());
            hideProgressBar();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
        Uri outputUri = Uri.fromFile(new File(getCacheDir(), "cropped"));
        new Crop(this, source).output(outputUri).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            uploadPicture(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadPicture(Uri uri) {
        try {
            InputStream in = getContentResolver().openInputStream(uri);
            RequestParams params = new RequestParams();
            params.put("image", in);

            String url = NetUtils.getUrlFactory().uploadPicture();

            showProgressBar(false);
            UploadHttpClient.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      byte[] responseBody) {
                    hideProgressBar();
                    String result = new String(responseBody);
                    LogUtil.i(TAG, "upload response=" + result);
                    try {
                        if (statusCode == 200) {
                            JSONObject jobj = new JSONObject(result);
                            JSONObject data = jobj.optJSONObject("data");
                            String name = data.optString("name");
                            jsPictureMd5(name);
                        } else {
                            Utils.showShortToast(BrowserActivity.this,
                                    R.string.error_message_upload_picture_fail, Gravity.CENTER);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] responseBody, Throwable error) {
                    hideProgressBar();
                    Utils.showShortToast(BrowserActivity.this,
                            R.string.error_message_upload_picture_fail, Gravity.CENTER);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Utils.showShortToast(BrowserActivity.this,
                    R.string.error_message_upload_picture_fail, Gravity.CENTER);
        }
    }

    private void jsPictureMd5(String md5) {
        if (mWebView != null) {
            mWebView.loadUrl("javascript:imageCallback('" + md5 + "')");
        }
    }

    final Handler myHandler = new Handler();

    class JavaScriptInterface {

        @JavascriptInterface
        public void onChangeTitle(final String title) {
            LogUtil.i(TAG, "onChangeTitle() title=" + title);
            myHandler.post(new Runnable() {
                @Override
                public void run() {
//                    getToolbar().setTitle(title);
                }
            });

        }
    }

}
