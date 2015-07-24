package com.feifan.bp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioGroup;

import com.feifan.bp.base.BaseActivity;
import com.feifan.bp.home.IndexFragment;
import com.feifan.bp.home.MessageFragment;
import com.feifan.bp.home.SettingsFragment;
import com.feifan.bp.login.LoginFragment;
import com.feifan.bp.login.UserCtrl;
import com.feifan.bp.password.ForgetPasswordFragment;
import com.feifan.bp.password.ResetPasswordFragment;
import com.feifan.bp.scanner.CodeScannerActivity;
import com.feifan.bp.widget.TabBar;

import java.util.ArrayList;
import java.util.List;


public class LaunchActivity extends BaseActivity implements OnFragmentInteractionListener {

    private TabBar mBottomBar;

    private List<Fragment> mFragments = new ArrayList<>();

    private Fragment mCurrentFragment;

    public static Intent buildIntent() {
        Intent intent = new Intent(PlatformState.getApplicationContext(), LaunchActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        //初始化数据
        mFragments.add(IndexFragment.newInstance());
        mFragments.add(MessageFragment.newInstance());
        mFragments.add(SettingsFragment.newInstance());

        // 初始化视图
        mBottomBar = (TabBar) findViewById(R.id.bottom_bar);
        mBottomBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switchFragment(mFragments.get(checkedId));
            }
        });

        // 加载内容视图
        initContent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initContent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected boolean isShowToolbar() {
        return true;
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
    }

    @Override
    public void onFragmentInteraction(Bundle args) {
        String from = args.getString(OnFragmentInteractionListener.INTERATION_KEY_FROM);
        String to = args.getString(OnFragmentInteractionListener.INTERATION_KEY_TO);
        int type = args.getInt(OnFragmentInteractionListener.INTERATION_KEY_TYPE, OnFragmentInteractionListener.TYPE_IDLE);
        if (from.equals(LoginFragment.class.getName())) {  // 来自登录界面，登录成功
            if (PlatformState.getInstance().getLastUrl() != null) {
                if (Utils.isNetworkAvailable()) {
                    BrowserActivity.startActivity(this, PlatformState.getInstance().getLastUrl());
                } else {
                    Utils.showShortToast(R.string.error_message_text_offline, Gravity.CENTER);
                }
            }
            showHome(true);
        } else if (from.equals(SettingsFragment.class.getName())) {
            if (to.equals(LaunchActivity.class.getName())) {
                startActivity(buildIntent());
            } else if (to.equals(ResetPasswordFragment.class.getName())) {
                showResetPassword();
            }else {
                startActivity(Utils.getSystemBrowser(to));
            }
        } else if (from.equals(ForgetPasswordFragment.class.getName())) {
            showForgetPassword();
        } else if (from.equals(ResetPasswordFragment.class.getName())) {
            if (type == OnFragmentInteractionListener.TYPE_NAVI_CLICK) {
                showHome(false);
            }
        } else if (from.equals(IndexFragment.class.getName())) {
            if (to.equals(CodeScannerActivity.class.getName())) {
                CodeScannerActivity.startActivity(this);
            } else {
                openBrowser(to);
            }
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
        transaction.replace(R.id.content_container, fragment);
        transaction.commitAllowingStateLoss();
        mCurrentFragment = fragment;
    }

    // 初始化界面内容
    private void initContent() {
        if (UserCtrl.getStatus() == UserCtrl.USER_STATUS_LOGOUT) { //登出状态
            showLogin();
        } else {
            showHome(true);
        }
    }

    // 显示主界面
    private void showHome(boolean reset) {
        if(reset) {
            mBottomBar.reset();
        }
        mBottomBar.setVisibility(View.VISIBLE);
        switchFragment(mFragments.get(mBottomBar.getCheckedRadioButtonId()));
    }

    // 显示忘记密码
    private void showForgetPassword() {
        mBottomBar.setVisibility(View.GONE);
        switchFragment(ForgetPasswordFragment.newInstance());
    }

    // 显示重置密码
    private void showResetPassword() {
        mBottomBar.setVisibility(View.GONE);
        switchFragment(ResetPasswordFragment.newInstance());
    }

    // 显示登录界面
    private void showLogin() {
        mBottomBar.setVisibility(View.GONE);
        switchFragment(LoginFragment.newInstance());
    }

    // 打开浏览器
    private void openBrowser(String url) {
        if (Utils.isNetworkAvailable()) {
            Intent intent = new Intent(this, BrowserActivity.class);
            intent.putExtra(BrowserActivity.EXTRA_KEY_URL, url);
            startActivity(intent);
        } else {
            Utils.showShortToast(R.string.error_message_text_offline, Gravity.CENTER);
        }
    }

    @Override
    public void onBackPressed() {
        if (mCurrentFragment != null && mCurrentFragment instanceof ForgetPasswordFragment) {
            showLogin();
        } else if (mCurrentFragment != null && mCurrentFragment instanceof ResetPasswordFragment) {
            showHome(false);
        } else {
            super.onBackPressed();
        }
    }

}
