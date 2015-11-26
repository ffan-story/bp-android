package com.feifan.bp.browser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.PopupWindow;

import com.feifan.bp.Constants;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.base.BaseActivity;
import com.feifan.bp.widget.FloatingActionButton;
import com.feifan.bp.widget.ObservableScrollView;
import com.feifan.bp.widget.SelectPopWindow;
import com.feifan.croplib.Crop;
import com.feifan.material.MaterialDialog;

import java.io.File;

import bp.feifan.com.codescanner.Contents;

public class BrowserActivity extends BaseActivity implements BrowserFragment.OnBrowserListener {
    /**
     * 参数键名称－URL
     */
    public static final String EXTRA_KEY_URL = "url";
    public static final String EXTRA_KEY_STAFF_MANAGE = "staff";

    private ObservableScrollView mScrollView;
    private FloatingActionButton fab;
    private SelectPopWindow mPopWindow;
    private View mShadowView;
    private int lastSelectPos = 0;

    private boolean mShowFab = false;
    private String sUrl;
    private String mUrl;
    private String mStoreId;

    // dialog
    private MaterialDialog mDialog;
    private transient boolean isShowDlg = true;

    public static void startActivity(Context context, String url) {
        Intent i = new Intent(context, BrowserActivity.class);
        i.putExtra(EXTRA_KEY_URL, url);
        context.startActivity(i);
    }

    public static void startForResultActivity(Activity activity, String url) {
        Intent i = new Intent(activity, BrowserActivity.class);
        i.putExtra(EXTRA_KEY_URL, url);
        activity.startActivityForResult(i, Constants.REQUEST_CODE_STAFF_EDIT);
    }

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, BrowserActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
            initViews();
            initDialog();
            mShowFab = UserProfile.getInstance().getAuthRangeType().equals("merchant");
            mUrl = getIntent().getStringExtra(EXTRA_KEY_URL);
            if(mShowFab){
                mStoreId = UserProfile.getInstance().getStoreId(lastSelectPos);
                sUrl = mUrl + "&storeId=" + mStoreId;
            }else{
            sUrl = mUrl;
        }
//        loadWeb(sUrl);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        loadWeb(sUrl);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    private void initViews() {
        mShadowView = findViewById(R.id.shadowView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMenu();
            }
        });
    }

    private void initDialog() {
        mDialog = new MaterialDialog(BrowserActivity.this)
                .setPositiveButton(R.string.common_retry_text, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                        isShowDlg = true;
                    }
                })
                .setNegativeButton(R.string.common_close_text, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                        isShowDlg = true;
                        finish();
                    }
                });
    }

    BrowserFragment mBrowserFragment ;
    /**
     * 载入网页
     */
    private void loadWeb(String url) {
        mBrowserFragment =BrowserFragment.newInstance(url);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.browser_fragment, mBrowserFragment);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 门店选择
     */
    private void selectMenu() {
        mPopWindow = new SelectPopWindow(BrowserActivity.this, lastSelectPos);
        mShadowView.setVisibility(View.VISIBLE);
        mShadowView.startAnimation(AnimationUtils.loadAnimation(BrowserActivity.this, R.anim.pop_bg_show));
        mPopWindow.showAtLocation(BrowserActivity.this.findViewById(R.id.fab),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mShadowView.startAnimation(AnimationUtils.loadAnimation(BrowserActivity.this, R.anim.pop_bg_hide));
                mShadowView.setVisibility(View.INVISIBLE);
                if (lastSelectPos != mPopWindow.getSelectPos()) {
                    lastSelectPos = mPopWindow.getSelectPos();
                    mStoreId = UserProfile.getInstance().getStoreId(lastSelectPos);
                    sUrl = mUrl + "&storeId=" + mStoreId;
                    loadWeb(sUrl);
                }
                mPopWindow.dismiss();
            }
        });
    }

    @Override
    protected boolean isShowToolbar() {
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    @Override
    public void OnTitleReceived(String title) {
        if(title.equals(getString(R.string.index_report_text))&&mShowFab){
            fab.setVisibility(View.VISIBLE);
        }else{
            fab.setVisibility(View.GONE);
        }
    }

    @Override
    public void OnErrorReceived(String msg, final WebView web, final String url) {
        if(isShowDlg && !isFinishing()) {
            mDialog.setMessage(msg)
                    .setPositiveButton(R.string.common_retry_text, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                            isShowDlg = true;
                            web.loadUrl(url);
                        }
                    })
                    .show();

            isShowDlg = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            mBrowserFragment.beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            mBrowserFragment.handleCrop(resultCode, data);
        } else if (requestCode == Crop.REQUEST_CARMAER && resultCode == RESULT_OK) {
            mBrowserFragment.beginCrop(Uri.fromFile(new File(mBrowserFragment.imgPath)));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK){
            mBrowserFragment.goBack();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
}
