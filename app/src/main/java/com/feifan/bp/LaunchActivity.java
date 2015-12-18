package com.feifan.bp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioGroup;

import com.android.volley.Response;
import com.feifan.bp.base.BaseActivity;
import com.feifan.bp.browser.BrowserActivity;
import com.feifan.bp.browser.BrowserTabActivity;
import com.feifan.bp.envir.EnvironmentManager;
import com.feifan.bp.home.HomeCtrl;
import com.feifan.bp.home.IndexFragment;
import com.feifan.bp.home.MessageFragment;
import com.feifan.bp.home.ReadMessageModel;
import com.feifan.bp.home.SettingsFragment;
import com.feifan.bp.home.check.CheckManageFragment;
import com.feifan.bp.home.check.IndicatorFragment;
import com.feifan.bp.home.code.CodeQueryResultFragment;
import com.feifan.bp.home.userinfo.UserInfoFragment;
import com.feifan.bp.login.LoginFragment;
import com.feifan.bp.login.UserCtrl;
import com.feifan.bp.network.UrlFactory;
import com.feifan.bp.password.ForgetPasswordFragment;
import com.feifan.bp.password.ResetPasswordFragment;
import com.feifan.bp.settings.feedback.FeedBackFragment;
import com.feifan.bp.settings.helpcenter.HelpCenterFragment;
import com.feifan.bp.widget.BadgerRadioButton;
import com.feifan.bp.widget.TabBar;
import com.feifan.statlib.FmsAgent;

import java.util.ArrayList;
import java.util.List;


public class LaunchActivity extends BaseActivity implements OnFragmentInteractionListener {

    private TabBar mBottomBar;
    private List<Fragment> mFragments = new ArrayList<>();
    private Fragment mCurrentFragment;

    // badger
    public static final String STORE_TYPE = "store";
    public static final String MERCHANTID = "merchant";
    public static final String USER_TYPE = "1";
    public static final String MESSAGE_ZERO = "0";
    public static final int MESSAGE_POSITION = 1;
    private String storeId = "";
    private String merchantId = "";
    private BadgerRadioButton mMessageTab;

    public static Intent buildIntent(Context context) {

        Intent intent = new Intent(context, LaunchActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        //统计埋点初始化
        FmsAgent.init(getApplicationContext(), EnvironmentManager.getHostFactory().getFFanApiPrefix() + "appstatlog");

        //初始化数据
        mFragments.add(IndexFragment.newInstance());
        mFragments.add(MessageFragment.newInstance());
        mFragments.add(SettingsFragment.newInstance());

        // 初始化视图
        mBottomBar = (TabBar) findViewById(R.id.bottom_bar);
        mBottomBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                //统计埋点 首页home、消息、设置
              switch (checkedId){
                  case 0:
                      FmsAgent.onEvent(getApplicationContext(),Statistics.FB_HOME_HOME);
                      break;
                  case 1:
                      FmsAgent.onEvent(getApplicationContext(),Statistics.FB_HOME_MESSAGE);
                      break;
                  case 2:
                      FmsAgent.onEvent(getApplicationContext(),Statistics.FB_HOME_SETTING);
                      break;

              }
                switchFragment(mFragments.get(checkedId));
            }
        });
        mMessageTab = (BadgerRadioButton) mBottomBar.getChildAt(MESSAGE_POSITION);
        // 加载内容视图
        initContent();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 获取未读提示状态
        if(UserProfile.getInstance().isStoreUser()){
            storeId = UserProfile.getInstance().getAuthRangeId();
        }else {
            merchantId = UserProfile.getInstance().getAuthRangeId();
        }
        HomeCtrl.getUnReadtatus(merchantId, storeId, USER_TYPE, new Response.Listener<ReadMessageModel>() {
            @Override
            public void onResponse(ReadMessageModel readMessageModel) {
                int refundId = Integer.valueOf(EnvironmentManager.getAuthFactory().getRefundId());
                PlatformState.getInstance().updateUnreadStatus(refundId, readMessageModel.refundCount > 0);

                // 更新消息提示
                if(readMessageModel.messageCount > 0) {
                    mMessageTab.showBadger();
                } else {
                    mMessageTab.hideBadger();
                }
//                if(mCurrentFragment instanceof OnTabLifetimeListener) {
//                    ((OnTabLifetimeListener)mCurrentFragment).onEnter();
//                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initContent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PlatformState.getInstance().reset();
        //统计埋点----用户启动APP
        FmsAgent.onEvent(getApplicationContext(), Statistics.CLOSE_APP);
    }

    @Override
    protected boolean isShowToolbar() {
        return true;
    }

    @Override
    public int getContentContainerId() {
        return R.id.content_container;
    }

    @Override
    public void retryRequestNetwork() {
        if(mCurrentFragment instanceof MessageFragment){
            ((MessageFragment)mCurrentFragment).updateData();
        }
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
    }

    @Override
    public void onFragmentInteraction(Bundle args) {
        String from = args.getString(OnFragmentInteractionListener.INTERATION_KEY_FROM);
        String to = args.getString(OnFragmentInteractionListener.INTERATION_KEY_TO);
        String title = args.getString(OnFragmentInteractionListener.INTERATION_KEY_TITLE);
        int type = args.getInt(OnFragmentInteractionListener.INTERATION_KEY_TYPE, OnFragmentInteractionListener.TYPE_IDLE);
        if (from.equals(LoginFragment.class.getName())) {  // 来自登录界面，登录成功
            if (PlatformState.getInstance().getLastUrl() != null) {
                if (Utils.isNetworkAvailable(this)) {
                    BrowserActivity.startActivity(this, PlatformState.getInstance().getLastUrl());

                } else {
                    Utils.showShortToast(this, R.string.error_message_text_offline, Gravity.CENTER);
                }
            }
            showHome(true);
        } else if (from.equals(SettingsFragment.class.getName())) {//设置界面
            if (to.equals(LaunchActivity.class.getName())) {
               startActivity(buildIntent(this));
            }else{
                Intent intent = new Intent(this, PlatformTopbarActivity.class);
                intent.putExtra(OnFragmentInteractionListener.INTERATION_KEY_FROM,from);
                intent.putExtra(OnFragmentInteractionListener.INTERATION_KEY_TO, to);
                intent.putExtra(PlatformTopbarActivity.EXTRA_TITLE,title);
                startActivity(intent);
            }
        } else if (from.equals(ForgetPasswordFragment.class.getName())) {
            showForgetPassword();
        } else if (from.equals(IndexFragment.class.getName())) {
            if (to.equals(CodeScannerActivity.class.getName())) {
                String mUrlStr = UrlFactory.searchCodeForHtml();
                CodeScannerActivity.startActivityForResult(this, mUrlStr);
                //add by tianjun 2015.10.27
            } else if (to.equals(UserInfoFragment.class.getName())) {
                if (Utils.isNetworkAvailable(this)) {//Utils.isCurrentNetworkAvailable(this)
                    showLoginInfo();
                } else {
                    Utils.showShortToast(this, R.string.error_message_text_offline, Gravity.CENTER);
                }
            }else if(to.equals(CheckManageFragment.class.getName())){
                if (Utils.isNetworkAvailable(this)) { //Utils.isCurrentNetworkAvailable(this)
                    Intent intent = new Intent(this, PlatformTopbarActivity.class);
                    intent.putExtra(OnFragmentInteractionListener.INTERATION_KEY_TO, CheckManageFragment.class.getName());
                    startActivity(intent);
                } else {
                    Utils.showShortToast(this, R.string.error_message_text_offline, Gravity.CENTER);
                }
            } else if (to.equals(IndicatorFragment.class.getName())) {
                showIndicatorInfo();
            } else if (to.equals(BrowserTabActivity.class.getName())) {
                openTabBrowser(args);
            }
//            else if(to.equals(PlatformTopbarActivity.class.getName())){
//                //提货码核销
//                PlatformTopbarActivity.startActivityForResult(this, CodeQueryResultFragment.class.getName(), "查询结果");
//            }
            else{
                openBrowser(args.getString(BrowserActivity.EXTRA_KEY_URL));
            }
        } else if (from.equals(UserInfoFragment.class.getName())) {
            if (type == OnFragmentInteractionListener.TYPE_NAVI_CLICK) {
                showHome(false);
            }
        }
        //end.
    }

    @Override
    public void onTitleChanged(String title) {

    }

    @Override
    public void onStatusChanged(boolean flag) {
        if(flag) {
            mMessageTab.showBadger();
        } else {
            mMessageTab.hideBadger();
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
        if (reset) {
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
//    private void showResetPassword() {
//        mBottomBar.setVisibility(View.GONE);
//        switchFragment(ResetPasswordFragment.newInstance());
//    }

    // 显示登录界面
    private void showLogin() {
        mBottomBar.setVisibility(View.GONE);
        switchFragment(LoginFragment.newInstance());
    }

    //帮助中心
//    private void showHelpCenter() {
//        mBottomBar.setVisibility(View.GONE);
//        switchFragment(HelpCenterFragment.newInstance());
//    }

    //显示意见反馈页面
//    private void showFeedBack() {
//        mBottomBar.setVisibility(View.GONE);
//        switchFragment(FeedBackFragment.newInstance());
//    }

    private void showLoginInfo() {
        mBottomBar.setVisibility(View.GONE);
        switchFragment(UserInfoFragment.newInstance());
    }

    private void showIndicatorInfo(){
        mBottomBar.setVisibility(View.GONE);
        switchFragment(IndicatorFragment.newInstance());
    }

    // 打开TAB浏览器
    private void openTabBrowser(Bundle args) {
        if (Utils.isNetworkAvailable(this)) {
            Intent intent = new Intent(this, BrowserTabActivity.class);
            intent.putExtra(BrowserTabActivity.EXTRA_KEY_URL, args.getString(BrowserTabActivity.EXTRA_KEY_URL));
            intent.putExtra(BrowserTabActivity.EXTRA_KEY_STATUS, args.getStringArray(BrowserTabActivity.EXTRA_KEY_STATUS));
            intent.putExtra(BrowserTabActivity.EXTRA_KEY_TITLES, args.getStringArray(BrowserTabActivity.EXTRA_KEY_TITLES));
            startActivity(intent);
        } else {
            Utils.showShortToast(this, R.string.error_message_text_offline, Gravity.CENTER);
        }
    }


    // 打开浏览器
    private void openBrowser(String url) {
        if (Utils.isNetworkAvailable(this)) {
            Intent intent = new Intent(this, BrowserActivity.class);
            intent.putExtra(BrowserActivity.EXTRA_KEY_URL, url);
            startActivity(intent);
        } else {
            Utils.showShortToast(this, R.string.error_message_text_offline, Gravity.CENTER);
        }
    }

    @Override
    public void onBackPressed() {
        if (mCurrentFragment != null && mCurrentFragment instanceof ForgetPasswordFragment) {
            showLogin();
        } else if (mCurrentFragment != null
                && mCurrentFragment instanceof ResetPasswordFragment
                || mCurrentFragment instanceof UserInfoFragment
                || mCurrentFragment instanceof FeedBackFragment
                || mCurrentFragment instanceof HelpCenterFragment) {
            showHome(false);
        }  else {
            super.onBackPressed();
        }
    }
}
