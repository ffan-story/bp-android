package com.feifan.bp.scanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.feifan.bp.BrowserActivity;
import com.feifan.bp.LogUtil;
import com.feifan.bp.PlatformHelper;
import com.feifan.bp.R;
import com.feifan.bp.base.BaseActivity;

import bp.feifan.com.codescanner.CaptureActivity;
import bp.feifan.com.codescanner.CaptureActivityOfResult;

/**
 * Created by maning on 15/7/6.
 */
public class CodeScannerActivity extends BaseActivity implements CaptureActivityOfResult {
    private static final String TAG = CodeScannerActivity.class.getSimpleName();

    public static void startActivity(Context context) {
        Intent i = new Intent(context, CodeScannerActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_scanner);
        initViews();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        CaptureActivity f = (CaptureActivity) Fragment.instantiate(this, CaptureActivity.class.getName());
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
        LogUtil.i(TAG, "getScanCodeResult() text=" + resultText + " time=" + timeStamp +
                " format=" + barcodeFormat);
        if (TextUtils.isEmpty(resultText)) {
            //TODO: Toast and return??
            return;
        }

        //TODO: Translate code to url and jump to h5 page.
        String urlStr = ScannerCtrl.getSignH5Url(resultText);
        BrowserActivity.startActivity(this, urlStr);
        finish();

    }

    @Override
    protected boolean isShowToolbar() {
        return true;
    }
}
