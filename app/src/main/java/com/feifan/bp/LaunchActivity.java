package com.feifan.bp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.feifan.bp.home.BusinessManageFragment;
import com.feifan.bp.home.MerchantCenterFragment;
import com.feifan.bp.login.LoginFragment;
import com.feifan.bp.login.UserCtrl;
import com.feifan.bp.widget.TabBar;

import java.util.ArrayList;
import java.util.List;


public class LaunchActivity extends FragmentActivity implements OnFragmentInteractionListener {

    private TabBar mBottomBar;

    private List<Fragment> mFragments = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        // 初始化
        final TextView centerTxv = (TextView)findViewById(R.id.title_bar_center);
        mBottomBar = (TabBar)findViewById(R.id.bottom_bar);
        mBottomBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switchFragment(mFragments.get(checkedId));
                switch (checkedId) {
                    case 0:         // 业务管理
                        centerTxv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        centerTxv.setText(R.string.home_business_manage_text);
                        break;
                    case 1:         // 商户中心
                        centerTxv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home_merchant_center_default_logo, 0, 0);
                        centerTxv.setText(R.string.home_merchant_center_text);
                        break;
                }
            }
        });

        // 加载内容视图
        Fragment fragment = null;
        if(UserCtrl.getStatus() == UserCtrl.USER_STATUS_LOGOUT) { //登出状态
            fragment = LoginFragment.newInstance();
            centerTxv.setText(R.string.login_login_text);
        }else {
            mFragments.add(BusinessManageFragment.newInstance());
            mFragments.add(MerchantCenterFragment.newInstance());
            fragment = mFragments.get(0);
            centerTxv.setText(R.string.home_business_manage_text);
            mBottomBar.setVisibility(View.VISIBLE);

        }
        switchFragment(fragment);
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
