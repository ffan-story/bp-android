package com.feifan.bp.browser;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.feifan.bp.widget.FloatingActionButton;
import com.feifan.bp.widget.ObservableScrollView;
import com.feifan.croplib.Crop;
import com.feifan.bp.Constants;
import com.feifan.bp.LaunchActivity;
import com.feifan.bp.PlatformState;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.Utils;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.net.UrlFactory;
import com.feifan.bp.util.IOUtil;
import com.feifan.bp.util.ImageUtil;
import com.feifan.bp.util.LogUtil;
import com.feifan.bp.widget.DialogPhoneLayout;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class BrowserFragment extends BaseFragment implements View.OnClickListener ,MenuItem.OnMenuItemClickListener{
    private static final String TAG = "BrowserFragment";
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
    private ObservableScrollView mScrollView;
    private FloatingActionButton fab;
    private boolean mIsStaffManagementPage = false;
    private int mWebViewProgress = 0;

    public static BrowserFragment newInstance(String url ,boolean mIsStaffManagementPage) {
        Bundle args = new Bundle();
        args.putString(EXTRA_KEY_URL,url);
        args.putBoolean(EXTRA_KEY_STAFF_MANAGE, mIsStaffManagementPage);
        BrowserFragment browserFragment = new BrowserFragment();
        browserFragment.setArguments(args);
        return browserFragment;
    }

    public BrowserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.actvivity_browser_new, container, false);
        mWebView = (WebView)v.findViewById(R.id.browser_content);
        initWeb(mWebView);
        String mUrl = getArguments().getString(EXTRA_KEY_URL);
        mIsStaffManagementPage = getArguments().getBoolean(EXTRA_KEY_STAFF_MANAGE);
        mWebView.loadUrl(mUrl);
        PlatformState.getInstance().setLastUrl(mUrl);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mToolbarStatus == TOOLBAR_STATUS_STAFF) {
            inflater.inflate(R.menu.menu_staff_manage, menu);
            (menu.findItem(R.id.action_staff)).setOnMenuItemClickListener(this);
        } else if (mToolbarStatus == TOOLBAR_STATUS_COUPON) {
            inflater.inflate(R.menu.menu_coupon_add, menu);
            (menu.findItem(R.id.action_coupon)).setOnMenuItemClickListener(this);
        } else if (mToolbarStatus == TOOLBAR_STATUS_COMMODITY) {
            inflater.inflate(R.menu.menu_commodity_manage, menu);
            (menu.findItem(R.id.action_commodity)).setOnMenuItemClickListener(this);
        } else if(mToolbarStatus == TOOLBAR_STATUS_COMMODITY_DESC){
            inflater.inflate(R.menu.menu_commodity_manage_desc, menu);
            (menu.findItem(R.id.action_commodity_desc)).setOnMenuItemClickListener(this);
        } else {
            menu.clear();
        }
        super.onCreateOptionsMenu(menu, inflater);
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
                if (canGoBack()) {
                    mWebView.goBack();
                } else {
                    getActivity().finish();
                }
            }
        });
    }

    private void initWeb(WebView webView) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDomStorageEnabled(true);

        // 缓存相关
        if(PlatformState.getInstance().isCacheClearable()) {
            webView.clearCache(true);
            webView.clearHistory();
            webView.clearFormData();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            Log.i(TAG, "webview'cache has been cleared!");
        }
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setAppCachePath(getActivity().getCacheDir().getAbsolutePath());

        webView.setWebViewClient(new PlatformWebViewClient());
        webView.setWebChromeClient(new PlatformWebChromeClient());
        webView.requestFocus();
    }

    // 当前页面是否可以回退
    private boolean canGoBack() {
        // 特殊页面
        String title = getToolbar().getTitle().toString();
        if(title.equals(getString(R.string.browser_coupon_list))){
            return false;
        }
        return mWebView.canGoBack();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (mWebViewProgress < 100) {
            return true;
        }
        String url = "";
        switch (item.getItemId()) {
            case R.id.action_staff:
                url = UrlFactory.staffAddForHtml(getActivity());
                mWebView.loadUrl(url);
                LogUtil.i(TAG, "menu onClick() staff url=" + url);
                return true;

            case R.id.action_coupon:
                url = UrlFactory.couponAddForHtml(getActivity());
                mWebView.loadUrl(url);
                LogUtil.i(TAG, "menu onClick() coupon url=" + url);
                return true;

            case R.id.action_commodity:
                url = UrlFactory.commodityManageForHtml(getActivity());
                mWebView.loadUrl(url);
                LogUtil.i(TAG, "menu onClick() commodity url=" + url);
                return true;

            case R.id.action_commodity_desc:
                initLeaveWordsDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class PlatformWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mWebViewProgress = newProgress;
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            LogUtil.i(TAG, "onReceiveTitle() title=" + title);
            if (getToolbar() != null){
                getToolbar().setTitle(title);
            }
            if (getString(R.string.browser_staff_list).equals(title)) {
                mToolbarStatus =TOOLBAR_STATUS_STAFF;
            } else if (getString(R.string.browser_coupon_list).equals(title)) {
                mToolbarStatus = TOOLBAR_STATUS_COUPON;
            } else if (getString(R.string.index_commodity_text).equals(title)) {
                mToolbarStatus =TOOLBAR_STATUS_COMMODITY;
            } else if (getString(R.string.browser_commodity_desc).equals(title)){
                mToolbarStatus = TOOLBAR_STATUS_COMMODITY_DESC;
            } else {
                mToolbarStatus = TOOLBAR_STATUS_IDLE;
            }
            getActivity().supportInvalidateOptionsMenu();
        }
    }

    private class PlatformWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url);
            String schema = uri.getScheme();
            if (schema.equals(Constants.URL_SCHEME_PLATFORM)) {
                if (url.contains(Constants.URL_PATH_LOGIN)) {      // 重新登录
                    UserProfile.instance(getActivity()).clear();
                    startActivity(LaunchActivity.buildIntent(getActivity()));
                } else if (url.contains(Constants.URL_PATH_EXIT)) {
                    getActivity().finish();
                } else if (url.contains(Constants.URL_PATH_HOME)) {
                    // 目前关闭当前界面即显示主界面
                    getActivity().finish();
                } else if (url.contains(Constants.URL_LOCAL_IMAGE)) {
                    addImage(url);
                }
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showProgressBar(true);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            hideProgressBar();
        }
    }


    private void addImage(String url){
        mImgPickType = IMG_PICK_TYPE_0;
        String source = "both";
        int pos = url.indexOf("?");
        if (pos > -1) {
            String paramsStr = url.substring(pos + 1, url.length());
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
            Crop.pickImage(getActivity());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (requestCode == Crop.REQUEST_PICK && resultCode == getActivity().RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == getActivity().RESULT_OK) {
            handleCrop(resultCode, result);
        } else if(requestCode == Crop.REQUEST_CARMAER && resultCode == getActivity().RESULT_OK){
            beginCrop(Uri.fromFile(new File(imgPath)));
        }
    }

    private void beginCrop(Uri source) {
        Uri outputUri = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
        if (IMG_PICK_TYPE_1 == mImgPickType) {
            new Crop(getActivity(), source).output(outputUri).asSquare().start(getActivity());
        } else if (IMG_PICK_TYPE_2 == mImgPickType) {
            new Crop(getActivity(), source).output(outputUri).withAspect(16, 9).withMaxSize(1280, 720).start(getActivity());
        } else if (IMG_PICK_TYPE_0 == mImgPickType) {
            new Crop(getActivity(), source).output(outputUri).asSquare().start(getActivity());
        } else if (IMG_PICK_TYPE_3 == mImgPickType) {
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), source);
                if(bitmap.getHeight() == 720 && bitmap.getWidth() == 1280){
                    uploadPicture(source);
                    // new Crop(this, source).output(outputUri).withAspect(16, 9).withMaxSize(1280, 720).start(this);
                }else{
                    Utils.showShortToast(getActivity(), R.string.error_message_picture_size, Gravity.CENTER);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            new Crop(getActivity(), source).output(outputUri).asSquare().start(getActivity());
        }
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == getActivity().RESULT_OK) {

            int  i = ImageUtil.readPictureDegree(Crop.getOutput(result).getPath());
            try {
                Bitmap myRoundBitmap  = ImageUtil.rotaingImageView(i, MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Crop.getOutput(result)));
                uploadPicture(Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), myRoundBitmap, null,null)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //new File(Crop.getOutput(result).getPath());
            // uploadPicture(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(getActivity(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadPicture(Uri uri) {

        // 获取符合条件的图片输入流
        Bitmap uploadImg = null;
        try {
            uploadImg = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        final InputStream in = ImageUtil.makeStream(uploadImg,Constants.IMAGE_MAX_WIDTH, Constants.IMAGE_MAX_HEIGHT, Constants.IMAGE_MAX_BYTES);

        if(!uploadImg.isRecycled()) {
            uploadImg.recycle();
        }

        try {
            params.put("image", in);
            String url = UrlFactory.uploadPicture();
            showProgressBar(false);
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showShortToast(getActivity(),
                    R.string.error_message_upload_picture_fail, Gravity.CENTER);
            IOUtil.closeQuietly(in);
        }
    }

    public Dialog phoneDialog;
    public DialogPhoneLayout dialogPhoneLayout;
    public void initLeaveWordsDialog() {
        phoneDialog = new Dialog(getActivity(),R.style.FullScreenPhoneDialog);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        Window window = phoneDialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = dm.widthPixels;
        params.height = dm.heightPixels;
        params.dimAmount = 0.7f;
        window.setAttributes(params);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialogPhoneLayout = new DialogPhoneLayout(getActivity());
        phoneDialog.setContentView(dialogPhoneLayout, new Gallery.LayoutParams(dm.widthPixels, LinearLayout.LayoutParams.MATCH_PARENT));
        dialogPhoneLayout.setBtnCancelClicklListener(this);
        dialogPhoneLayout.setLayoutPhoneClicklListener(this);
        dialogPhoneLayout.setLayoutCameraClicklListener(this);
        phoneDialog.show();
    }

    String imgPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/feifandp/img.jpg";


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_phone:
                if (null != phoneDialog) {
                    phoneDialog.dismiss();
                }
                Crop.pickImage(getActivity());
                break;

            case R.id.dialog_camera:
                if (null != phoneDialog) {
                    phoneDialog.dismiss();
                }
                if(!Utils.isHasSdCard()){
                    Utils.showShortToast(getActivity(), R.string.sd_card_exist, Gravity.CENTER);
                }else{
                    Crop.cameraImage(getActivity(),imgPath);
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