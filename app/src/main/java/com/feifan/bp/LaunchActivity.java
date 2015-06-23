package com.feifan.bp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.feifan.bp.factory.FactorySet;
import com.feifan.bp.home.BusinessManageFragment;
import com.feifan.bp.home.Model.CenterModel;
import com.feifan.bp.home.UserCenterFragment;
import com.feifan.bp.login.LoginFragment;
import com.feifan.bp.login.UserCtrl;
import com.feifan.bp.widget.CircleImageView;
import com.feifan.bp.widget.TabBar;

import java.util.ArrayList;
import java.util.List;


public class LaunchActivity extends FragmentActivity implements OnFragmentInteractionListener {

    private TabBar mBottomBar;
    private CircleImageView mLogoImv;
    private TextView mTitleTxt;

    private List<Fragment> mFragments = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        //初始化数据
        mFragments.add(BusinessManageFragment.newInstance());
        mFragments.add(UserCenterFragment.newInstance());

        // 初始化视图
        mTitleTxt = (TextView)findViewById(R.id.title_bar_center);
        mLogoImv = (CircleImageView)findViewById(R.id.title_bar_logo);
        mBottomBar = (TabBar)findViewById(R.id.bottom_bar);
        mBottomBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switchFragment(mFragments.get(checkedId));
                switch (checkedId) {
                    case 0:         // 业务管理
                        mLogoImv.setVisibility(View.GONE);
                        mTitleTxt.setText(R.string.home_business_manage_text);
                        break;
                    case 1:         // 商户中心
                        mLogoImv.setVisibility(View.VISIBLE);
                        mTitleTxt.setText(R.string.home_merchant_center_text);
                        break;
                }
            }
        });

        // 加载内容视图
        Fragment fragment = null;
        if(UserCtrl.getStatus() == UserCtrl.USER_STATUS_LOGOUT) { //登出状态
            fragment = LoginFragment.newInstance();
            mTitleTxt.setText(R.string.login_login_text);
        }else {
            fragment = mFragments.get(0);
            mTitleTxt.setText(R.string.home_business_manage_text);
            mBottomBar.setVisibility(View.VISIBLE);
            mBottomBar.check(0);

        }
        switchFragment(fragment);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("xuchunlei", "onNewIntent is called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onFragmentInteraction(Bundle args) {
        String from = args.getString(OnFragmentInteractionListener.INTERATION_KEY_FROM);
        if(from.equals(LoginFragment.class.getName())) {  // 来自登录界面，登录成功
            switchFragment(BusinessManageFragment.newInstance());
            // 显示底边栏
            mBottomBar.setVisibility(View.VISIBLE);
        } else if(from.equals(UserCenterFragment.class.getName())) {
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
                mTitleTxt.setText(model.primaryName);
                // + model.secondaryName
            }
        }
    }

    /**
     * 切换界面
     * @param fragment
     */
    private void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_container, fragment);
        transaction.commitAllowingStateLoss();
    }

}
