package com.feifan.bp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.feifan.bp.login.LogFragment;
import com.feifan.bp.login.UserCtrl;


public class LaunchActivity extends FragmentActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        // 加载标题栏

        TextView centerTxv = (TextView)findViewById(R.id.title_bar_center);

        // 加载内容视图
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;
        if(UserCtrl.getStatus() == UserCtrl.USER_STATUS_LOGOUT) { //登出状态
            fragment = LogFragment.newInstance();
            centerTxv.setText(R.string.login_login_text);
        }
        transaction.replace(R.id.content_container, fragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
