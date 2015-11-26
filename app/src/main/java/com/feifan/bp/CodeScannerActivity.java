package com.feifan.bp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.feifan.bp.browser.BrowserActivity;
import com.feifan.bp.network.UrlFactory;
import com.feifan.bp.refund.RefundFragment;
import com.feifan.bp.util.LogUtil;
import com.feifan.bp.base.BaseActivity;

import bp.feifan.com.codescanner.CaptureActivityOfResult;
import bp.feifan.com.codescanner.CodeScannerFragment;

/**
 * Created by maning on 15/7/6.
 */
public class CodeScannerActivity extends BaseActivity implements CaptureActivityOfResult {
    private static final String TAG = CodeScannerActivity.class.getSimpleName();

    private static String INTERATION_KEY_FROM = "interation_key_from";

    private String mfrom ="";
    public static void startActivity(Context context) {
        Intent i = new Intent(context, CodeScannerActivity.class);
        context.startActivity(i);
    }

    /**
     *
     * @param context
     * @param from
     */
    public static void startActivity(Context context,String from) {
        Intent i = new Intent(context, CodeScannerActivity.class);
        i.putExtra(INTERATION_KEY_FROM,from);
        context.startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_scanner);
        if(null != getIntent().getStringExtra(INTERATION_KEY_FROM)){
            mfrom = getIntent().getStringExtra(INTERATION_KEY_FROM);
        }
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
        LogUtil.i(TAG, "getScanCodeResult() text=" + resultText + " time=" + timeStamp +
                " format=" + barcodeFormat);
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showShortToast(this, R.string.error_message_text_offline, Gravity.CENTER);
            finish();
            return;
        }
        if (TextUtils.isEmpty(resultText)) {
            //TODO: Toast and return??
            return;
        }
        String urlStr ="";
        if(!TextUtils.isEmpty(mfrom) && mfrom.equals(RefundFragment.class.getName())){
            LogUtil.i(TAG, "resultText="+resultText);
           urlStr = UrlFactory.refundQueryHtml(resultText);
        }else{
           urlStr = UrlFactory.searchCodeForHtml(resultText);
        }
        BrowserActivity.startActivity(this, urlStr);
        finish();
    }

    @Override
    protected boolean isShowToolbar() {
        return true;
    }
}
