package com.feifan.bp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.feifan.bp.base.BaseActivity;
import com.feifan.bp.browser.BrowserActivity;
import com.feifan.bp.home.ErrorFragment;
import com.feifan.bp.home.code.CodeQueryResultFragment;
import com.feifan.bp.util.LogUtil;

import bp.feifan.com.codescanner.CaptureActivityOfResult;
import bp.feifan.com.codescanner.CodeScannerFragment;

/**
 * Created by maning on 15/7/6.
 */
public class CodeScannerActivity extends BaseActivity implements CaptureActivityOfResult {
    private static final String TAG = CodeScannerActivity.class.getSimpleName();

    public static final String INTERATION_KEY_URL = "inter_URL";
    private String mUrlStr = "";

    public static void startActivityForResult(Activity context,String url) {
        Intent i = new Intent(context, CodeScannerActivity.class);
        i.putExtra(INTERATION_KEY_URL,url);
        context.startActivityForResult(i,Constants.REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_scanner);

        mUrlStr = getIntent().getStringExtra(INTERATION_KEY_URL);
        initViews();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        CodeScannerFragment f = (CodeScannerFragment) Fragment.instantiate(this, CodeScannerFragment.class.getName());
        f.setCaptureActivityOfResult(this);
        transaction.add(R.id.content_container, f);
        transaction.commit();
    }

    private void initViews() {
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        toolbar.setTitle(R.string.scan_cade_title);
        toolbar.setNavigationIcon(R.mipmap.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void getScanCodeResult(String resultText, long timeStamp, String barcodeFormat) {
        LogUtil.i(TAG, "resultText=" + resultText);
        if (TextUtils.isEmpty(resultText)) {
            return;
        }

        Bundle args = new Bundle();
        int mIntCodeLength = resultText.trim().length();
        if (TextUtils.isDigitsOnly(resultText)){
            if (mIntCodeLength<Constants.CODE_LENGTH_TEN){
                args.putString(ErrorFragment.EXTRA_KEY_ERROR_MESSAGE, getApplicationContext().getApplicationContext().getString(R.string.error_message_text_sms_code_length_min));
                PlatformTopbarActivity.startActivity(CodeScannerActivity.this, ErrorFragment.class.getName(),
                        getApplicationContext().getApplicationContext().getString(R.string.query_result), args);
            }else{
                if (!Utils.isNetworkAvailable(getApplicationContext())) {
                    Utils.showShortToast(getApplicationContext(), R.string.error_message_text_offline, Gravity.CENTER);
                    return;
                }else {
                    if (mIntCodeLength==Constants.CODE_LENGTH_TEN){//提货吗
                        args.putBoolean(CodeQueryResultFragment.EXTRA_KEY_IS_COUPON, false);
                    }else if (mIntCodeLength>Constants.CODE_LENGTH_TEN) {//券码
                        args.putBoolean(CodeQueryResultFragment.EXTRA_KEY_IS_COUPON, true);
                    }
                    args.putString(CodeQueryResultFragment.CODE, resultText);
                    PlatformTopbarActivity.startActivity(CodeScannerActivity.this,CodeQueryResultFragment.class.getName(),
                            getApplicationContext().getApplicationContext().getString(R.string.query_result),args);
                }
            }
        }else{
            if (mIntCodeLength>Constants.CODE_LENGTH_TWENTY){//券码
                args.putString(CodeQueryResultFragment.CODE, resultText);
                args.putBoolean(CodeQueryResultFragment.EXTRA_KEY_IS_COUPON, true);
                PlatformTopbarActivity.startActivity(CodeScannerActivity.this, CodeQueryResultFragment.class.getName(),
                        getApplicationContext().getApplicationContext().getString(R.string.query_result), args);
            }else{
                args.putString(ErrorFragment.EXTRA_KEY_ERROR_MESSAGE, getApplicationContext().getApplicationContext().getString(R.string.error_message_text_code_incorrect));
                PlatformTopbarActivity.startActivity(CodeScannerActivity.this, ErrorFragment.class.getName(), getApplicationContext().getApplicationContext().getString(R.string.query_result), args);
            }
        }
        finish();
    }

    @Override
    protected boolean isShowToolbar() {
        return true;
    }

}
