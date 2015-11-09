package com.feifan.bp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.feifan.bp.base.BaseActivity;
import com.feifan.bp.home.check.ReconciliationManagementFragment;

/**
 * Created by xuchunlei on 15/11/9.
 */
public class PlatformTopbarActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topbar);

        mToolbar = (Toolbar)findViewById(R.id.topbar_header);
        mToolbar.setTitle("对账管理");

        switchFragment(ReconciliationManagementFragment.newInstance());
    }

    /**
     * 切换界面
     *
     * @param fragment
     */
    private void switchFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.topbar_container, fragment);
        transaction.commitAllowingStateLoss();
//        mCurrentFragment = fragment;
    }

    @Override
    public void onFragmentInteraction(Bundle args) {

    }
}
