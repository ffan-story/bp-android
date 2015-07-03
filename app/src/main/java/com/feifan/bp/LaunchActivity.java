package com.feifan.bp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.feifan.bp.factory.FactorySet;
import com.feifan.bp.home.IndexFragment;
import com.feifan.bp.home.MessageFragment;
import com.feifan.bp.home.Model.CenterModel;
import com.feifan.bp.home.SettingsFragment; 
import com.feifan.bp.login.LoginFragment;
import com.feifan.bp.login.UserCtrl;
import com.feifan.bp.password.ForgetPasswordFragment;
import com.feifan.bp.password.ResetPasswordFragment;
import com.feifan.bp.widget.CircleImageView;
import com.feifan.bp.widget.TabBar;

import java.util.ArrayList;
import java.util.List;


public class LaunchActivity extends FragmentActivity implements OnFragmentInteractionListener {

    private TabBar mBottomBar;
    private CircleImageView mLogoImv;
    private TextView mTitleTxt;

    private List<Fragment> mFragments = new ArrayList<Fragment>();

    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        //初始化数据
        mFragments.add(IndexFragment.newInstance());
        mFragments.add(MessageFragment.newInstance());
        mFragments.add(SettingsFragment.newInstance());

        // 初始化视图
        mTitleTxt = (TextView)findViewById(R.id.title_bar_center);
        mLogoImv = (CircleImageView)findViewById(R.id.title_bar_logo);
        mBottomBar = (TabBar)findViewById(R.id.bottom_bar);
        mBottomBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switchFragment(mFragments.get(checkedId));
                switch (checkedId) {
                    case 0:         // 首页
                        mLogoImv.setVisibility(View.GONE);
                        mTitleTxt.setText(R.string.home_index_text);
                        break;
                    case 1:         // 消息
                        mLogoImv.setVisibility(View.GONE);
                        mTitleTxt.setText(R.string.home_message_text);
                        break;
                    case 2:         // 设置
                        mLogoImv.setVisibility(View.VISIBLE);
                        mTitleTxt.setText(R.string.home_settings_text);
                        break;
                }
            }
        });

        // 加载内容视图
        if(UserCtrl.getStatus() == UserCtrl.USER_STATUS_LOGOUT) { //登出状态
            showLogin();
        }else {
            showHome();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onFragmentInteraction(Bundle args) {
        String from = args.getString(OnFragmentInteractionListener.INTERATION_KEY_FROM);
        if(from.equals(LoginFragment.class.getName())) {  // 来自登录界面，登录成功
            showHome();
        } else if(from.equals(SettingsFragment.class.getName())) {
            CenterModel model = args.getParcelable(OnFragmentInteractionListener.INTERATION_KEY_LOGO);
            if(model == null) {
                finish();
                Intent intent = new Intent(this, LaunchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {        // 加载图标
                ImageLoader.ImageListener listener = ImageLoader.getImageListener(mLogoImv, R.drawable.home_merchant_center_default_logo, R.drawable.home_merchant_center_default_logo);
                PlatformState.getInstance().getImageLoader().get(
                        FactorySet.getUrlFactory().getFFanImageHostUrl() +
                                model.logoSrc, listener);
                if(PlatformState.getInstance().getUserProfile().getAuthRangeType().equals(Constants.AUTH_RANGE_TYPE_STORE)) {  // 门店
                    if(model.secondaryName != null) {
                        mTitleTxt.setText(Html.fromHtml(getString(R.string.center_logo_text_format, model.primaryName, model.secondaryName)));
                    }else {
                        mTitleTxt.setText(model.primaryName);
                    }
                } else { // 商户只显示大字标题
                    mTitleTxt.setText(model.primaryName);
                }
            }
        } else if(from.equals(ForgetPasswordFragment.class.getName())) {
            showForgetPassword();
        } else if(from.equals(ResetPasswordFragment.class.getName())) {
            showResetPassword();
        }
    }

    /**
     * 切换界面
     * @param fragment
     */
    private void switchFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment tempFragment = manager.findFragmentByTag(fragment.getClass().getSimpleName());

        if(mCurrentFragment != null) {          // 隐藏当前界面
            transaction.hide(mCurrentFragment);
        }

        if(tempFragment == fragment) {    // 已经添加过则直接显示
            transaction.show(fragment);
        } else {
            transaction.add(R.id.content_container, fragment, fragment.getClass().getSimpleName());
        }
        transaction.commitAllowingStateLoss();
        mCurrentFragment = fragment;

    }

    // 显示主界面
    private void showHome() {
        mBottomBar.setVisibility(View.VISIBLE);
        switchFragment(mFragments.get(mBottomBar.getCheckedRadioButtonId()));
        mTitleTxt.setText(R.string.home_settings_text);
    }

    // 显示忘记密码
    private void showForgetPassword() {
        mBottomBar.setVisibility(View.GONE);
        switchFragment(ForgetPasswordFragment.newInstance());
        mTitleTxt.setText(R.string.reset_password);
    }

    // 显示重置密码
    private void showResetPassword() {
        mLogoImv.setVisibility(View.GONE);
        mBottomBar.setVisibility(View.GONE);
        switchFragment(ResetPasswordFragment.newInstance());
        mTitleTxt.setText(R.string.reset_password);
    }

    // 显示登录界面
    private void showLogin() {
        mTitleTxt.setText(R.string.login_login_text);
        switchFragment(LoginFragment.newInstance());
    }


    @Override
    public void onBackPressed() {
        if(mCurrentFragment != null&& mCurrentFragment instanceof ForgetPasswordFragment) {
            showLogin();
        }
        else if(mCurrentFragment != null&& mCurrentFragment instanceof ResetPasswordFragment){
            mLogoImv.setVisibility(View.VISIBLE);
            showHome();
        }
        else {
            super.onBackPressed();
        }
    }
}
