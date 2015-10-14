package com.feifan.bp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.feifan.bp.account.AccountManager;
import com.feifan.bp.base.BaseActivity;
import com.feifan.bp.crop.Crop;
import com.feifan.bp.net.NetUtils;
import com.feifan.bp.net.UploadHttpClient;
import com.feifan.bp.util.IOUtils;
import com.feifan.bp.util.ImageUtil;
import com.feifan.bp.util.LogUtil;
import com.feifan.bp.widget.DialogPhoneLayout;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class BrowserActivity extends BaseActivity implements View.OnClickListener {
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
    private static final int TOOLBAR_STATUS_COMMODITY_DESC = 4;
    private int mToolbarStatus = TOOLBAR_STATUS_IDLE;

    //type=0不限制大小
    private static final int IMG_PICK_TYPE_0 = 0;
    //type=1是640*640
    private static final int IMG_PICK_TYPE_1 = 1;
    //type=2是亲子类目规格为16:9，尺寸：最小640px*360px，最大1280px*720px
    private static final int IMG_PICK_TYPE_2 = 2;
    //type=3是优惠券类目规格为16:9，尺寸固定为1280 x 720
    private static final int IMG_PICK_TYPE_3 = 3;

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
    protected void onDestroy() {
        if (null != phoneDialog) {
            phoneDialog.dismiss();
        }
        super.onDestroy();
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
        } else if(mToolbarStatus == TOOLBAR_STATUS_COMMODITY_DESC){
            inflater.inflate(R.menu.menu_commodity_manage_desc, menu);
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
            case R.id.action_commodity_desc:
                initLeaveWordsDialog();
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
            getToolbar().setTitle(title);
            if (getString(R.string.index_staff_list).equals(title)) {
                mToolbarStatus = TOOLBAR_STATUS_STAFF;
            } else if (getString(R.string.index_coupon_list).equals(title)) {
                mToolbarStatus = TOOLBAR_STATUS_COUPON;
            } else if (getString(R.string.index_commodity_text).equals(title)) {
                mToolbarStatus = TOOLBAR_STATUS_COMMODITY;
            } else if (getString(R.string.index_commodity_desc).equals(title)){
                mToolbarStatus = TOOLBAR_STATUS_COMMODITY_DESC;
            } else{
                mToolbarStatus = TOOLBAR_STATUS_IDLE;
            }
            supportInvalidateOptionsMenu();
        }
    }

    private void addImage(String url){
        mImgPickType = IMG_PICK_TYPE_0;
        String source = "both";
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
            if(paramMap.get("source") != null){
                source = paramMap.get("source");
            }
        }

        if("both".equals(source)) {
            initLeaveWordsDialog();
        }else if("photoLibrary".equals(source)){
            Crop.pickImage(BrowserActivity.this);
        }
    }
    private class PlatformWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtil.i(TAG, "receive==" + url);
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
                    addImage(url);
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
        super.onActivityResult(requestCode, resultCode, result);
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            handleCrop(resultCode, result);
        } else if(requestCode == Crop.REQUEST_CARMAER && resultCode == RESULT_OK){
            beginCrop(Uri.fromFile(new File(imgPath)));
        }

    }

    private void beginCrop(Uri source) {
        LogUtil.i(TAG, "bitmap.outWidth()=" + source);
        Uri outputUri = Uri.fromFile(new File(getCacheDir(), "cropped"));
        if (IMG_PICK_TYPE_1 == mImgPickType) {
            new Crop(this, source).output(outputUri).asSquare().start(this);
        } else if (IMG_PICK_TYPE_2 == mImgPickType) {
            new Crop(this, source).output(outputUri).withAspect(16, 9).withMaxSize(1280, 720).start(this);
        } else if (IMG_PICK_TYPE_0 == mImgPickType) {
            new Crop(this, source).output(outputUri).asSquare().start(this);
        } else if (IMG_PICK_TYPE_3 == mImgPickType) {
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), source);
                if(bitmap.getHeight() == 720 && bitmap.getWidth() == 1280){
                    uploadPicture(source);
                   // new Crop(this, source).output(outputUri).withAspect(16, 9).withMaxSize(1280, 720).start(this);
                }else{
                    Utils.showShortToast(BrowserActivity.this, R.string.error_message_picture_size, Gravity.CENTER);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
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

        // 获取符合条件的图片输入流
        Bitmap uploadImg = null;
        try {
            uploadImg = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        final InputStream in = ImageUtil.makeStream(uploadImg, Constants.IMAGE_MAX_WIDTH, Constants.IMAGE_MAX_HEIGHT);

        try {
            params.put("image", in);
            String url = NetUtils.getUrlFactory().uploadPicture();
            showProgressBar(false);
            UploadHttpClient.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
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
                    IOUtils.closeQuietly(in);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] responseBody, Throwable error) {
                    hideProgressBar();
                    Utils.showShortToast(BrowserActivity.this,
                            R.string.error_message_upload_picture_fail, Gravity.CENTER);
                    IOUtils.closeQuietly(in);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showShortToast(BrowserActivity.this,
                    R.string.error_message_upload_picture_fail, Gravity.CENTER);
            IOUtils.closeQuietly(in);
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

    public Dialog phoneDialog;
    public DialogPhoneLayout dialogPhoneLayout;
    public Dialog getLeaveWordsDialog() {
        return phoneDialog;
    }
    public void setLeaveWordsDialog(Dialog dialog) {
        this.phoneDialog = dialog;
    }
    public void initLeaveWordsDialog() {
        phoneDialog = new Dialog(this,R.style.FullScreenPhoneDialog);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        Window window = phoneDialog.getWindow();
        android.view.WindowManager.LayoutParams params = window.getAttributes();
        params.width = dm.widthPixels;
        params.height = dm.heightPixels;
        params.dimAmount = 0.7f;
        window.setAttributes(params);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialogPhoneLayout = new DialogPhoneLayout(this);
        phoneDialog.setContentView(dialogPhoneLayout, new Gallery.LayoutParams(dm.widthPixels, LinearLayout.LayoutParams.MATCH_PARENT));
        dialogPhoneLayout.setBtnCancelClicklListener(this);
        dialogPhoneLayout.setLayoutPhoneClicklListener(this);
        dialogPhoneLayout.setLayoutCameraClicklListener(this);
        phoneDialog.show();
    }

    String imgPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/feifandp/img.jpg";
    /**
     * 获取sdcar路径
     * @return
     */
    private boolean isHasSdCard(){
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        return sdCardExist;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_phone:
                if (null != phoneDialog) {
                    phoneDialog.dismiss();
                }
                Crop.pickImage(BrowserActivity.this);
                break;

            case R.id.dialog_camera:
                if (null != phoneDialog) {
                    phoneDialog.dismiss();
                }
                if(!isHasSdCard()){
                    Utils.showShortToast(BrowserActivity.this, R.string.sd_card_exist, Gravity.CENTER);
                }else{
                    Crop.cameraImage(BrowserActivity.this,imgPath);
                }
                break;

            case R.id.dialog_cancel:
                if (null != phoneDialog) {
                    phoneDialog.dismiss();
                }
                break;

            default:
                break;
        }
    }
}
