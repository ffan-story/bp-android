package com.feifan.bp;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

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


public class LaunchActivity extends FragmentActivity implements OnFragmentInteractionListener {

    private TabBar mBottomBar;
    private TextView mTitleTxt;

    private List<Fragment> mFragments = new ArrayList<Fragment>();

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
        mTitleTxt = (TextView) findViewById(R.id.title_bar_center);
        mBottomBar = (TabBar) findViewById(R.id.bottom_bar);
        mBottomBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switchFragment(mFragments.get(checkedId));
                switch (checkedId) {
                    case 0:         // 首页
                        mTitleTxt.setText(R.string.app_name);
                        break;
                    case 1:         // 消息
                        mTitleTxt.setText(R.string.home_message_text);
                        break;
                    case 2:         // 设置
                        mTitleTxt.setText(R.string.home_settings_text);
                        break;
                }
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
    public void onFragmentInteraction(Bundle args) {
        String from = args.getString(OnFragmentInteractionListener.INTERATION_KEY_FROM);
        String to = args.getString(OnFragmentInteractionListener.INTERATION_KEY_TO);
        if (from.equals(LoginFragment.class.getName())) {  // 来自登录界面，登录成功
            if(PlatformState.getInstance().getLastUrl() != null) {
                BrowserActivity.startActivity(this, PlatformState.getInstance().getLastUrl());
            }

            showHome();
        } else if (from.equals(SettingsFragment.class.getName())) {
            if (to.equals(LaunchActivity.class.getName())) {
                startActivity(buildIntent());
            }
        } else if (from.equals(ForgetPasswordFragment.class.getName())) {
            showForgetPassword();
        } else if (from.equals(ResetPasswordFragment.class.getName())) {
            showResetPassword();
        } else if (from.equals(IndexFragment.class.getName())) {
            if (to.equals(CodeScannerActivity.class.getName())) {
                CodeScannerActivity.startActivity(this);
            } else {
                Intent intent = new Intent(this, BrowserActivity.class);
                intent.putExtra(BrowserActivity.EXTRA_KEY_URL, to);
                startActivity(intent);
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
        Fragment tempFragment = manager.findFragmentByTag(fragment.getClass().getSimpleName());

        if (mCurrentFragment != null) {          // 隐藏当前界面
            transaction.hide(mCurrentFragment);
        }

        if (tempFragment == fragment) {    // 已经添加过则直接显示
            transaction.show(fragment);
        } else {
            transaction.add(R.id.content_container, fragment, fragment.getClass().getSimpleName());
        }
        transaction.commitAllowingStateLoss();
        mCurrentFragment = fragment;

    }

    // 初始化界面内容
    private void initContent() {
        if (UserCtrl.getStatus() == UserCtrl.USER_STATUS_LOGOUT) { //登出状态
            showLogin();
        } else {
            showHome();
        }
    }

    // 显示主界面
    private void showHome() {
        mBottomBar.reset();
        mBottomBar.setVisibility(View.VISIBLE);
        switchFragment(mFragments.get(mBottomBar.getCheckedRadioButtonId()));
        mTitleTxt.setText(R.string.app_name);
    }

    // 显示忘记密码
    private void showForgetPassword() {
        mBottomBar.setVisibility(View.GONE);
        switchFragment(ForgetPasswordFragment.newInstance());
        mTitleTxt.setText(R.string.reset_password);
    }

    // 显示重置密码
    private void showResetPassword() {
        mBottomBar.setVisibility(View.GONE);
        switchFragment(ResetPasswordFragment.newInstance());
        mTitleTxt.setText(R.string.reset_password);
    }

    // 显示登录界面
    private void showLogin() {
        mBottomBar.setVisibility(View.GONE);
        mTitleTxt.setText(R.string.login_login_text);
        switchFragment(LoginFragment.newInstance());
    }


    @Override
    public void onBackPressed() {
        if (mCurrentFragment != null && mCurrentFragment instanceof ForgetPasswordFragment) {
            showLogin();
        } else if (mCurrentFragment != null && mCurrentFragment instanceof ResetPasswordFragment) {
            showHome();
        } else {
            super.onBackPressed();
        }
    }

}
