package com.feifan.bp;

import android.app.Activity;
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
import com.feifan.bp.base.PlatformBaseActivity;
import com.feifan.bp.home.check.CheckManageFragment;
import com.feifan.bp.settings.helpcenter.HelpCenterFragment;
import com.feifan.bp.transactionflow.TransFlowTabActivity;


/**
 * 项目通用带有Topbar的活动
 *
 * Created by xuchunlei on 15/11/9.
 */
public class PlatformTopbarActivity extends PlatformBaseActivity implements OnFragmentInteractionListener {

    private Toolbar mToolbar;
    private TextView mCenterTitle;
    public static final String EXTRA_ARGS = "args";
    private Fragment mCurrentFragment;
    Bundle args;


    /**
     * 启动活动
     * @param context
     * @param fragmentName
     */
    public static void startActivity(Context context, String fragmentName) {
        Intent i = new Intent(context, PlatformTopbarActivity.class);
        i.putExtra(OnFragmentInteractionListener.INTERATION_KEY_TO,fragmentName);
        context.startActivity(i);
    }

    /**
     * startActivityForResult
     * @param context
     * @param fragmentName
     * @param titleName
     */
    public static void startActivityForResult(Activity context, String fragmentName,String titleName) {
        Intent i = new Intent(context, PlatformTopbarActivity.class);
        i.putExtra(OnFragmentInteractionListener.INTERATION_KEY_TO,fragmentName);
        i.putExtra(Constants.EXTRA_KEY_TITLE,titleName);
        context.startActivityForResult(i, Constants.REQUEST_CODE);
    }

    /**
     *startActivity
     * @param context
     * @param fragmentName
     * @param titleName
     */
    public static void startActivity(Context context, String fragmentName,String titleName) {
        Intent i = new Intent(context, PlatformTopbarActivity.class);
        i.putExtra(OnFragmentInteractionListener.INTERATION_KEY_TO,fragmentName);
        i.putExtra(Constants.EXTRA_KEY_TITLE,titleName);
        context.startActivity(i);
    }

    /**
     * startActivityForResult
     * 用bundle传参
     * @param context
     * @param fragmentName
     * @param titleName
     * @param args
     */
    public static void startActivityForResult(Activity context, String fragmentName,String titleName,Bundle args) {
        Intent i = new Intent(context, PlatformTopbarActivity.class);
        i.putExtra(OnFragmentInteractionListener.INTERATION_KEY_TO,fragmentName);
        i.putExtra(Constants.EXTRA_KEY_TITLE,titleName);
        i.putExtra(EXTRA_ARGS,args);
        context.startActivityForResult(i, Constants.REQUEST_CODE);
    }

    /**
     * 用bundle传参
     * @param context
     * @param fragmentName
     * @param titleName
     * @param args
     */
    public static void startActivity(Context context, String fragmentName,String titleName,Bundle args) {
        Intent i = new Intent(context, PlatformTopbarActivity.class);
        i.putExtra(OnFragmentInteractionListener.INTERATION_KEY_TO,fragmentName);
        i.putExtra(Constants.EXTRA_KEY_TITLE,titleName);
        i.putExtra(EXTRA_ARGS,args);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topbar);

        args = getIntent().getBundleExtra(EXTRA_ARGS);
        // 初始化标题栏
        mToolbar = (Toolbar)findViewById(R.id.topbar_header);
        mCenterTitle = (TextView)mToolbar.findViewById(R.id.header_center_title);
        mCenterTitle.setText(getIntent().getStringExtra(Constants.EXTRA_KEY_TITLE));
        initHeader(mToolbar);
        String fragmentName = getIntent().getStringExtra(OnFragmentInteractionListener.INTERATION_KEY_TO);
        if(!TextUtils.isEmpty(fragmentName)) {
            if(args!= null){
                switchFragment(Fragment.instantiate(this, fragmentName, args));
            }else{
                switchFragment(Fragment.instantiate(this,fragmentName));
            }
        }
    }

    @Override
    public int getContentContainerId() {
        return R.id.topbar_container;
    }

    @Override
    public void retryRequestNetwork() {
        ((HelpCenterFragment) mCurrentFragment).updateData();
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
        mCurrentFragment = fragment;

        // 改变标题
        Bundle args = fragment.getArguments();
        String title = null;
        if(args != null) {
            title = args.getString(Constants.EXTRA_KEY_TITLE);
            if(title != null) {
                mCenterTitle.setText(title);
                mToolbar.setVisibility(title == null ? View.GONE : View.VISIBLE);
            }
        }

    }

    @Override
    public void onFragmentInteraction(Bundle args) {
        String from = args.getString(OnFragmentInteractionListener.INTERATION_KEY_FROM);
        String to = args.getString(OnFragmentInteractionListener.INTERATION_KEY_TO);

        if(from.equals(CheckManageFragment.class.getName())) {
            if(to.equals(TransFlowTabActivity.class.getName())) {
                Intent intent = new Intent(this,TransFlowTabActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onTitleChanged(String title) {
        mCenterTitle.setText(title);
    }

    @Override
    public void onStatusChanged(boolean flag) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode ==RESULT_OK && requestCode == Constants.REQUEST_CODE){
            if (data !=null){
                setResult(RESULT_OK,data);
            }else{
                setResult(RESULT_OK);
            }
            finish();
        }
    }

    public Toolbar getToolbar(){
        return mToolbar;
    }
}
