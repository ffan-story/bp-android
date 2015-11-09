package com.feifan.bp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.feifan.bp.TransactionFlow.TransFlowTabActivity;
import com.feifan.bp.base.PlatformBaseActivity;
import com.feifan.bp.home.check.CheckManageFragment;

/**
 * Created by xuchunlei on 15/11/9.
 */
public class PlatformTopbarActivity extends PlatformBaseActivity implements OnFragmentInteractionListener {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topbar);

        mToolbar = (Toolbar)findViewById(R.id.topbar_header);

        String fragmentName = getIntent().getStringExtra(OnFragmentInteractionListener.INTERATION_KEY_TO);
        if(!TextUtils.isEmpty(fragmentName)) {
            switchFragment(Fragment.instantiate(this, fragmentName));
        }

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
        String from = args.getString(OnFragmentInteractionListener.INTERATION_KEY_FROM);
        String to = args.getString(OnFragmentInteractionListener.INTERATION_KEY_TO);

        if(from.equals(CheckManageFragment.class.getName())) {
            if(to.equals(TransFlowTabActivity.class.getName())) {
                Intent intent = new Intent(this, TransFlowTabActivity.class);
                startActivity(intent);
            }
        }
    }
}
