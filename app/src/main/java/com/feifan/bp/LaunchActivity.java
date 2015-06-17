package com.feifan.bp;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.feifan.bp.login.LoginActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class LaunchActivity extends FragmentActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
