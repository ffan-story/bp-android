package com.feifan.bp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.feifan.bp.TransactionFlow.TransFlowTabActivity;
import com.feifan.bp.base.PlatformBaseActivity;
import com.feifan.bp.browser.BrowserTabActivity;
import com.feifan.bp.home.check.CheckManageFragment;
import com.feifan.bp.refund.RefundFragment;

/**
 * 项目通用带有Topbar的活动
 *
 * Created by xuchunlei on 15/11/9.
 */
public class PlatformTopbarActivity extends PlatformBaseActivity implements OnFragmentInteractionListener {

    private Toolbar mToolbar;
    private TextView mCenterTitle;
    public static final String EXTRA_TITLE = "titleName";
    private String mStrTitleName = "";

    /**
     *
     * @param context
     * @param fragmentName
     * @param titleName
     */
    public static void startActivity(Context context, String fragmentName,String titleName) {
        Intent i = new Intent(context, PlatformTopbarActivity.class);
        i.putExtra(OnFragmentInteractionListener.INTERATION_KEY_TO,fragmentName);
        i.putExtra(EXTRA_TITLE,titleName);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topbar);

        mStrTitleName = getIntent().getStringExtra(EXTRA_TITLE);
        // 初始化标题栏
        mToolbar = (Toolbar)findViewById(R.id.topbar_header);
        mCenterTitle = (TextView)mToolbar.findViewById(R.id.header_center_title);
        mCenterTitle.setText(mStrTitleName);
        initHeader(mToolbar);
        String fragmentName = getIntent().getStringExtra(OnFragmentInteractionListener.INTERATION_KEY_TO);
        if(!TextUtils.isEmpty(fragmentName)) {
            switchFragment(Fragment.instantiate(this, fragmentName));
        }

    }

    private void initHeader(Toolbar header) {
        header.setNavigationIcon(R.mipmap.ic_left_arrow);
        header.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    @Override
    public void onTitleChanged(String title) {
        mCenterTitle.setText(title);
    }
}
