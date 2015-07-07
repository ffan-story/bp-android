package com.feifan.bp.scanner;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


import com.feifan.bp.R;

import bp.feifan.com.codescanner.CaptureActivity;

/**
 * Created by maning on 15/7/6.
 */
public class CodeScannerActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_scanner);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.content_container, new CaptureActivity());
        transaction.commit();

    }
}
