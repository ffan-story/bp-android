package com.feifan.bp.browser;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feifan.bp.Constants;
import com.feifan.bp.LaunchActivity;
import com.feifan.bp.PlatformState;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.Statistics;
import com.feifan.bp.UserProfile;
import com.feifan.bp.Utils;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.network.UploadHttpClient;
import com.feifan.bp.network.UrlFactory;
import com.feifan.bp.refund.RefundFragment;
import com.feifan.bp.util.IOUtil;
import com.feifan.bp.util.ImageUtil;
import com.feifan.bp.util.LogUtil;
import com.feifan.bp.widget.DialogPhoneLayout;
import com.feifan.croplib.Crop;
import com.feifan.material.MaterialDialog;
import com.feifan.statlib.FmsAgent;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * congjing
 */
public class BrowserFragment extends BaseFragment implements View.OnClickListener,
        Toolbar.OnMenuItemClickListener, OnActionListener {

    private static final String TAG = "BrowserFragment";

    /**
     * 参数键名称－URL
     */
    public static final String EXTRA_KEY_URL = "url";

    //type=0不限制大小
    private static final int IMG_PICK_TYPE_0 = 0;
    //type=1是640*640
    private static final int IMG_PICK_TYPE_1 = 1;
    //type=2是亲子类目规格为16:9，尺寸：最小640px*360px，最大1280px*720px
    private static final int IMG_PICK_TYPE_2 = 2;
    //type=3是优惠券类目规格为16:9，图片不限制大小
    private static final int IMG_PICK_TYPE_3 = 3;

    private int mImgPickType = IMG_PICK_TYPE_0;

    public WebView mWebView;

    private int mWebViewProgress = 0;

    public String mUrl;
    private String mTitleName = "";

    public OnBrowserListener mListener;


    public Dialog phoneDialog;
    public DialogPhoneLayout dialogPhoneLayout;
    public String imgPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/feifandp/img.jpg";

    /**
     * 防止webview action 连续点击，页面重复
     */
    private boolean isOnclicked =false;

    private BrowserMatcher mMatcher = new BrowserMatcher();
    // dialog
    private MaterialDialog mDialog;
    @Override
    public void onReload() {
        if(mWebView != null) {
            mWebView.clearView();
            mWebView.loadUrl(mUrl);
            LogUtil.i(TAG, "Reload web page " + mUrl);
        }
    }


    /**
     * 监听浏览器接口
     */
    public interface OnBrowserListener {
        /**
         * 获取到标题
         *
         * @param title 标题
         */
        void OnTitleReceived(String title);

        /**
         * 获取到错误
         *
         * @param msg 错误消息
         */
        void OnErrorReceived(String msg, WebView web, String url);
    }

    public static BrowserFragment newInstance() {
        return new BrowserFragment();
    }

    public static BrowserFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(EXTRA_KEY_URL, url);
        BrowserFragment browserFragment = new BrowserFragment();
        browserFragment.setArguments(args);
        return browserFragment;
    }

    public BrowserFragment() {
        // Required empty public constructor

    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public void setmTitleName(String contextTitle) {
        this.mTitleName = contextTitle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mUrl = getArguments().getString(EXTRA_KEY_URL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_browser, container, false);
        mWebView = (WebView) v.findViewById(R.id.browser_content);
        initWeb(mWebView);
        if(mUrl != null) {
            mWebView.loadUrl(mUrl);
            PlatformState.getInstance().setLastUrl(mUrl);
            LogUtil.i(TAG, "mUrl==" + mUrl);
        }
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnBrowserListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement OnBrowserListener");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.topbar_menu, menu);
        BrowserMatcher.MenuInfo info = mMatcher.matchForMenu(mTitleName);
        if(info != null) {
            MenuItem item = menu.add(Menu.NONE, info.id, 1, info.titleRes);
            if(info.iconRes != Constants.NO_INTEGER) {
                item.setIcon(info.iconRes);
            }
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setTitle(mTitleName);
        toolbar.setNavigationIcon(R.mipmap.ic_left_arrow);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        hideEmptyView();
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
        if(TextUtils.isEmpty(getToolbar().getTitle())){
            return false;
        }
        // 特殊页面
        String title = getToolbar().getTitle().toString();
        if (title.equals(getString(R.string.browser_coupon_list))) {
            return false;
        }
        return mWebView.canGoBack();
    }

    public void goBack(){
        if(canGoBack()){
            mWebView.goBack();
        }else{
            getActivity().setResult(Activity.RESULT_CANCELED);
            getActivity().finish();
        }
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (mWebViewProgress < 100) {
            return true;
        }
        String url = "";
        switch (item.getItemId()) {
            case R.id.menu_staff_add://添加员工
                //统计埋点
                FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_STAFFMANA_ADD);
                url = UrlFactory.staffAddForHtml();
                Intent i = new Intent(getActivity(), BrowserActivity.class);
                i.putExtra(BrowserActivity.EXTRA_KEY_URL, url);
                getActivity().startActivityForResult(i,Constants.REQUEST_CODE);
                LogUtil.i(TAG, "menu onClick() staff url=" + url);
                return true;

            case R.id.menu_coupon_add:
                url = UrlFactory.couponAddForHtml();
                fetchMarketingData(UserProfile.getInstance().getAuthRangeId(),url);
                return true;

            case R.id.menu_commodity_add://发布商品
                //统计埋点
                FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_GOODSMANA_PUB);

                url = UrlFactory.getCommodityManageForHtmlUrl();
                mWebView.loadUrl(url);
                LogUtil.i(TAG, "menu onClick() commodity url=" + url);
                return true;

            case R.id.menu_picture_add:
                initLeaveWordsDialog();
                return true;
            case R.id.menu_refund_start:
                PlatformTopbarActivity.startActivityForResult(getActivity(), RefundFragment.class.getName(), getActivity().getResources().getString(R.string.start_refund));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 获取合同状态
     * @param storeId  商户id
     */
    private void fetchMarketingData(String storeId,final String url){
        MarketingCtrl.marketingStatus(storeId, new Response.Listener<MarketingModel>() {
            @Override
            public void onResponse(MarketingModel baseModel) {
                if (baseModel.hasContract == 1) {
                    mWebView.loadUrl(url);
                } else {
                    Utils.showShortToast(getActivity(),getResources().getString(R.string.coupone_marketing_contract_not_hint));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

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

            if (!isAdded()) {
                return;
            }

            mListener.OnTitleReceived(title);
            mTitleName = title;
            getToolbar().setTitle(title);
            getActivity().supportInvalidateOptionsMenu();
        }
    }

    private class PlatformWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "We got " + url + " in shouldOverrideUrlLoading via PlatformWebViewClient");
            Uri uri = Uri.parse(url);
            String schema = uri.getScheme();
            LogUtil.i(TAG, "schema======" + schema);
            if(TextUtils.isEmpty(schema)){
               return true;
            }

            if (schema.equals(Constants.URL_SCHEME_PLATFORM)) {
                if (url.contains(Constants.URL_SCHEME_PLATFORM_LOGIN)) {      // 重新登录
                    UserProfile.getInstance().clear();
                    startActivity(LaunchActivity.buildIntent(getActivity()));
                } else if (url.contains(Constants.URL_SCHEME_PLATFORM_EXIT)) {
                    if (getActivity() != null ) {
                        getActivity().finish();
                    }
                } else if (url.contains(Constants.URL_SCHEME_PLATFORM_HOME)) {
                    // 目前关闭当前界面即显示主界面
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                } else if (url.contains(Constants.URL_SCHEME_PLATFORM_CLOSE)) {//返回退款售后
                    if (getActivity() != null) {
                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
                    }
                } else if (url.contains(Constants.URL_SCHEME_PLATFORM_IMAGE)) {
                    addImage(url);
                }
            } else if (schema.equals(Constants.URL_SCHEME_ACTION)) {
                if(isOnclicked){
                    return true;
                }
                isOnclicked = true;
                Activity mActivity = getActivity();
                Uri actionUri = Uri.parse(url);
                String actionStrUri = UrlFactory.urlForHtml(actionUri.getAuthority() + actionUri.getEncodedPath() + "#" + actionUri.getEncodedFragment());
                if(actionStrUri.contains("/goods/search_result")){//券码历史  链接符为&
                    LogUtil.i(TAG, "actionStrUri======" +  actionStrUri);
                    actionStrUri = UrlFactory.actionUrlForHtml(actionUri.getAuthority() + actionUri.getEncodedPath() + "#" + actionUri.getEncodedFragment());
                    BrowserActivity.startActivity(mActivity, actionStrUri);
                    return true;
                }
                LogUtil.i(TAG, "actionStrUri======" +  actionStrUri);
                if(actionStrUri.contains("/refund/detail")){//退款单详情
                    BrowserTabActivity.startActivity(mActivity,
                            actionStrUri+"&status=",
                            mActivity.getResources().getStringArray(R.array.data_type),
                            mActivity.getResources().getStringArray(R.array.tab_title_refund_detail_titles),
                            true);
                }else if(actionStrUri.contains("/staff/edit/")){//员工管理 编辑
                    Intent i = new Intent(mActivity, BrowserActivity.class);
                    i.putExtra(BrowserActivity.EXTRA_KEY_URL, actionStrUri);
                    getActivity().startActivityForResult(i,Constants.REQUEST_CODE);
                }else if(actionStrUri.contains("/staff") && (mActivity instanceof BrowserActivity)){//添加员工成功  以及编辑成功
                    mActivity.setResult(Activity.RESULT_OK);
                    mActivity.finish();
                }else if (actionStrUri.contains("/order/detail")){//查看详情：验证历史  订单管理
                    BrowserActivity.startActivity(mActivity, actionStrUri);
                }else if (actionStrUri.contains("/staff") && (mActivity instanceof BrowserTabActivity)){//员工管理冻结、解冻刷新ViewPage
                    ((BrowserTabActivity) mActivity).refreshViewPage();
                }else if(actionStrUri.contains("/staff")){//添加员工
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                }else{
                    Activity a = getActivity();
                    if (a instanceof BrowserTabActivity) {
                        ((BrowserTabActivity) a).refreshViewPage();
                    }
                }
            }else if(schema.equals(Constants.URL_SCHEME_ERROR)) {  //错误消息
                mListener.OnErrorReceived(uri.getAuthority(), mWebView, mUrl);
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
            hideProgressBar();
            super.onPageFinished(view, url);
            isOnclicked = false;
        }
    }

    private void addImage(String url) {
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
            if (paramMap.get("source") != null) {
                source = paramMap.get("source");
            }
        }

        if ("both".equals(source)) {
            initLeaveWordsDialog();
        } else if ("photoLibrary".equals(source)) {
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
        } else if (requestCode == Crop.REQUEST_CARMAER && resultCode == getActivity().RESULT_OK) {
            beginCrop(Uri.fromFile(new File(imgPath)));
        }
    }

    public void beginCrop(Uri source) {
        Uri outputUri = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));

        switch (mImgPickType) {
            case IMG_PICK_TYPE_0:
                new Crop(getActivity(), source).output(outputUri).asSquare().start(getActivity());
                break;
            case IMG_PICK_TYPE_1:
                new Crop(getActivity(), source).output(outputUri).asSquare().start(getActivity());
                break;
            case IMG_PICK_TYPE_2:
            case IMG_PICK_TYPE_3:
                new Crop(getActivity(), source).output(outputUri).withAspect(16, 9).withMaxSize(1280, 720).start(getActivity());
                break;
            default:
                new Crop(getActivity(), source).output(outputUri).asSquare().start(getActivity());
                break;
        }
    }

    public void handleCrop(int resultCode, Intent result) {
        if (resultCode == getActivity().RESULT_OK) {

            int i = ImageUtil.readPictureDegree(Crop.getOutput(result).getPath());
            try {
                Bitmap myRoundBitmap = ImageUtil.rotaingImageView(i, MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Crop.getOutput(result)));
                uploadPicture(Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), myRoundBitmap, null, null)));
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
        LogUtil.i(TAG,"uri=="+uri);
        // 获取符合条件的图片输入流
        Bitmap uploadImg = null;
        try {
            uploadImg = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        final InputStream in = ImageUtil.makeStream(uploadImg, Constants.IMAGE_MAX_WIDTH, Constants.IMAGE_MAX_HEIGHT, Constants.IMAGE_MAX_BYTES);

        if (!uploadImg.isRecycled()) {
            uploadImg.recycle();
        }

        try {
            params.put("image", in);
            String url = UrlFactory.uploadPicture();
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
                            LogUtil.i(TAG, "Upload image successfully, file'md5 is " + name);
                            mWebView.loadUrl("javascript:imageCallback('" + name + "')");
                        } else {
                            Utils.showShortToast(getActivity(),
                                    R.string.error_message_upload_picture_fail, Gravity.CENTER);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    IOUtil.closeQuietly(in);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] responseBody, Throwable error) {
                    hideProgressBar();
                    Utils.showShortToast(getActivity(),
                            R.string.error_message_upload_picture_fail, Gravity.CENTER);
                    IOUtil.closeQuietly(in);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showShortToast(getActivity(),
                    R.string.error_message_upload_picture_fail, Gravity.CENTER);
            IOUtil.closeQuietly(in);
        }
    }

    public void initLeaveWordsDialog() {
        phoneDialog = new Dialog(getActivity(), R.style.FullScreenPhoneDialog);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_phone:
                if (null != phoneDialog) {
                    phoneDialog.dismiss();
                }
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
                try {
                    startActivityForResult(intent, Crop.REQUEST_PICK);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), com.feifan.croplib.R.string.crop_pick_error, Toast.LENGTH_SHORT).show();
                }

//                startActivityForResult();
//                Crop.pickImage(getActivity());
                break;

            case R.id.dialog_camera:
                if (null != phoneDialog) {
                    phoneDialog.dismiss();
                }
                if (!Utils.isHasSdCard()) {
                    Utils.showShortToast(getActivity(), R.string.sd_card_exist, Gravity.CENTER);
                } else {
                    Crop.cameraImage(getActivity(), imgPath);
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

    @Override
    public void onResume() {
        super.onResume();
        isOnclicked = false;
    }
}
