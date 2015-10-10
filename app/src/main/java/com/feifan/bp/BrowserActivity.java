package com.feifan.bp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
public class BrowserActivity extends BaseActivity {
    private static final String TAG = BrowserActivity.class.getName();
    /**
     * 参数键名称－URL
     */
    public static final String EXTRA_KEY_URL = "url";
    public static final String EXTRA_KEY_STAFF_MANAGE = "staff";


    private static final int TOOLBAR_STATUS_IDLE = 0;
    private static final int TOOLBAR_STATUS_STAFF = 1;
    private static final int TOOLBAR_STATUS_COUPON = 2;
    private static final int TOOLBAR_STATUS_COMMODITY = 3;
    private int mToolbarStatus = TOOLBAR_STATUS_IDLE;

    //type=0不限制大小
    private static final int IMG_PICK_TYPE_0 = 0;
    //type=1是640*640
    private static final int IMG_PICK_TYPE_1 = 1;
    //type=2是亲子类目规格为16:9，尺寸：最小640px*360px，最大1280px*720px
    private static final int IMG_PICK_TYPE_2 = 2;

    private int mImgPickType = IMG_PICK_TYPE_0;

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
        MenuInflater inflater = getMenuInflater();
        if (mToolbarStatus == TOOLBAR_STATUS_STAFF) {
            inflater.inflate(R.menu.menu_staff_manage, menu);
        } else if (mToolbarStatus == TOOLBAR_STATUS_COUPON) {
            inflater.inflate(R.menu.menu_coupon_add, menu);
        } else if (mToolbarStatus == TOOLBAR_STATUS_COMMODITY) {
            inflater.inflate(R.menu.menu_commodity_manage, menu);
        } else {
            menu.clear();
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void onClick(MenuItem item) {
        LogUtil.i(TAG, "onClick()");
        if (mWebViewProgress < 100) {
            return;
        }
        String url = "";
        int id = item.getItemId();
        switch (id) {
            case R.id.action_staff:
                url = NetUtils.getUrlFactory().staffAddForHtml(this);
                mWebView.loadUrl(url);
                LogUtil.i(TAG, "menu onClick() staff url=" + url);
                break;
            case R.id.action_coupon:
                url = NetUtils.getUrlFactory().couponAddForHtml(this);
                mWebView.loadUrl(url);
                LogUtil.i(TAG, "menu onClick() coupon url=" + url);
                break;
            case R.id.action_commodity:
                url = NetUtils.getUrlFactory().commodityManageForHtml(this);
                mWebView.loadUrl(url);
                LogUtil.i(TAG, "menu onClick() commodity url=" + url);
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
//                finish();
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                }
            }
        });
    }

    private void initWeb(WebView webView) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setDomStorageEnabled(true);

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
//            if (view.getProgress() == 100) {
//                getToolbar().setTitle(title);
//                if (getString(R.string.index_staff_list).equals(title)) {
//                    mToolbarStatus = TOOLBAR_STATUS_STAFF;
//                } else if (getString(R.string.index_coupon_http://sop.sit.ffan.com/H5App/index.html#/commodity/select_cat_menu?loginToken=8df8016cd3ed49f8fbe0ee8056937f11&uid=12156&appType=bpMobilelist).equals(title)) {
//                    mToolbarStatus = TOOLBAR_STATUS_COUPON;
//                } else if (getString(R.string.index_commodity_text).equals(title)) {
//                    mToolbarStatus = TOOLBAR_STATUS_COMMODITY;
//                } else {
//                    mToolbarStatus = TOOLBAR_STATUS_IDLE;
//                }
//                supportInvalidateOptionsMenu();
//            }
            getToolbar().setTitle(title);
            if (getString(R.string.index_staff_list).equals(title)) {
                mToolbarStatus = TOOLBAR_STATUS_STAFF;
            } else if (getString(R.string.index_coupon_list).equals(title)) {
                mToolbarStatus = TOOLBAR_STATUS_COUPON;
            } else if (getString(R.string.index_commodity_text).equals(title)) {
                mToolbarStatus = TOOLBAR_STATUS_COMMODITY;
            } else {
                mToolbarStatus = TOOLBAR_STATUS_IDLE;
            }
            supportInvalidateOptionsMenu();
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
                } else if (url.contains(Constants.URL_LOCAL_IMAGE)) {
                    mImgPickType = IMG_PICK_TYPE_0;
                    int pos = url.indexOf("?");
                    if (pos > -1) {
                        String paramsStr = url.substring(pos + 1, url.length());
                        LogUtil.i(TAG, "paramsStr=" + paramsStr);
                        Map<String, String> paramMap = new HashMap<>();
                        if (!TextUtils.isEmpty(paramsStr)) {
                            String[] paramArray = paramsStr.split("&");
                            if (paramArray != null) {

                                for (String item : paramArray) {
                                    String[] keyValues = item.split("=");
                                    if (keyValues == null || keyValues.length < 2) {
                                        continue;
                                    }
                                    paramMap.put(keyValues[0], keyValues[1]);
                                }
                            }
                        }
                        String type = paramMap.get("type");
                        LogUtil.i(TAG, "Image pick type=" + type);
                        if (!TextUtils.isEmpty(type)) {
                            mImgPickType = Integer.parseInt(type);
                        }

                    }
                    Crop.pickImage(BrowserActivity.this);
                }

            }

            return true;
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
        if (IMG_PICK_TYPE_1 == mImgPickType) {
            new Crop(this, source).output(outputUri).asSquare().start(this);
        } else if (IMG_PICK_TYPE_2 == mImgPickType) {
            new Crop(this, source).output(outputUri).withAspect(16, 9).start(this);
        } else if (IMG_PICK_TYPE_0 == mImgPickType) {
            new Crop(this, source).output(outputUri).asSquare().start(this);
        } else {
            new Crop(this, source).output(outputUri).asSquare().start(this);
        }

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
