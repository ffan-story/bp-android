package com.feifan.bp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.feifan.bp.R;
import com.feifan.bp.base.BaseActivity;

/**
 * Created by maning on 15/6/15.
 */
public class MainTabActivity extends BaseActivity {

    public static void startActivity(Context context) {
        Intent i = new Intent(context, MainTabActivity.class);
        context.startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        setupActionBar();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.actionbar_main_tab);
    }


}
