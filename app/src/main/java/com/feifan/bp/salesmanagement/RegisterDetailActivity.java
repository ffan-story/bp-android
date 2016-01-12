package com.feifan.bp.salesmanagement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.instantevent.InstEventGoodsListFragment;
import com.feifan.bp.widget.CustomViewPager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 活动报名详情
 * Created by Frank on 15/12/21.
 */
public class RegisterDetailActivity extends AppCompatActivity implements View.OnClickListener,GoodsNoCommitFragment.UpdateStatusListener {

    public static final String EXTRA_KEY_ID = "id";
    public static final String EXTRA_KEY_TITLE = "title";

    public String promotionId;
    private String promotionName;

    private CollapsingToolbarLayout collapsingToolbar;
    private CustomViewPager viewPager;
    private TabLayout tabLayout;
    private RelativeLayout rlEventTitle;
    private Button btnAddProduct;
    private TextView tvEnrollHeader;
    private List<String> mFragmentTabCountList = new ArrayList<>();
    private GoodsNoCommitFragment goodsNoCommitFragment;

    public static void startActivity(Context context, String id,String title) {
        Intent i = new Intent(context, RegisterDetailActivity.class);
        i.putExtra(EXTRA_KEY_ID, id);
        i.putExtra(EXTRA_KEY_TITLE, title);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_detail);
        promotionId = getIntent().getStringExtra(EXTRA_KEY_ID);
        promotionName = getIntent().getStringExtra(EXTRA_KEY_TITLE);
        initViews();
        setupToolbar();
        getGoodsStatus();
    }

    private void initViews() {
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setTitleEnabled(false);
        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        rlEventTitle = (RelativeLayout) findViewById(R.id.rl_event_title);
        btnAddProduct = (Button) findViewById(R.id.btn_add_product);
        tvEnrollHeader = (TextView) findViewById(R.id.tv_enroll_detail_header);
        tvEnrollHeader.setText(promotionName);
        rlEventTitle.setOnClickListener(this);
        btnAddProduct.setOnClickListener(this);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.register_detail));
        toolbar.setNavigationIcon(R.mipmap.ic_left_arrow);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getGoodsStatus() {
        //测试
        String storeId = "9050588";
        final String merchantId = "2075643";
        String promotionCode = "GP1452134293843000000";
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        };
        Response.Listener<GoodsStatusModel> listener = new Response.Listener<GoodsStatusModel>() {
            @Override
            public void onResponse(GoodsStatusModel model) {

                if (model.mGoodsStatus != null && model.mGoodsStatus.size() != 0) {
                    for (int i = 0;i<model.mGoodsStatus.size();i++){
                        Iterator it = model.mGoodsStatus.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry entry = (Map.Entry) it.next();
                            String key = entry.getKey().toString();
                            if(key.equals(String.valueOf(i))){
                                mFragmentTabCountList.add(entry.getValue().toString());
                            }
                        }
                    }
                    setupViewPager(viewPager);
                }
            }
        };
        PromotionCtrl.getGoodsStatus(storeId, merchantId, promotionCode, listener, errorListener);
    }


    private void setupViewPager(CustomViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), RegisterDetailActivity.this);
        goodsNoCommitFragment = new GoodsNoCommitFragment();
        goodsNoCommitFragment.setUpdateListener(this);

        adapter.addFrag(goodsNoCommitFragment, getString(R.string.notsubmitted), R.mipmap.icon_notsubmitted, GoodsListFragment.STATUS_NO_COMMIT);
        adapter.addFrag(new GoodsListFragment(), getString(R.string.review), R.mipmap.icon_review, GoodsListFragment.STATUS_AUDIT);
        adapter.addFrag(new GoodsListFragment(), getString(R.string.approved), R.mipmap.icon_approved, GoodsListFragment.STATUS_AUDIT_PASS);
        adapter.addFrag(new GoodsListFragment(), getString(R.string.auditrefused), R.mipmap.icon_auditrefused, GoodsListFragment.STATUS_AUDIT_DENY);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(adapter.getTabView(i));
            }
        }
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_event_title:
                break;
            case R.id.btn_add_product:
                // TODO 跳转商品添加页面
                Bundle args = new Bundle();
                args.putString(InstEventGoodsListFragment.EXTRA_PARTAKE_EVENT_ID, "GP1452134293843000000");
                PlatformTopbarActivity.startActivity(this, InstEventGoodsListFragment.class.getName(),getString(R.string.instant_goods_list), args);
                break;
        }
    }

    /**
     * 提交或删除商品后刷新界面
     */
    @Override
    public void updateStatus() {
        getGoodsStatus();//更新角标
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        private Context context;

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private final List<Integer> mFragmentTabIconList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager, Context context) {
            super(manager);
            this.context = context;
        }

        public View getTabView(int position) {
            View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
            TextView tv = (TextView) v.findViewById(R.id.textView);
            tv.setText(mFragmentTitleList.get(position));
            ImageView img = (ImageView) v.findViewById(R.id.imageView);
            img.setImageResource(mFragmentTabIconList.get(position));
            TextView badgerView = (TextView) v.findViewById(R.id.badger);
            if (mFragmentTabCountList.get(position).equals("0")) {
                badgerView.setVisibility(View.GONE);
            } else {
                badgerView.setText(mFragmentTabCountList.get(position));
            }
            return v;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title, int iconId, int status) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
            mFragmentTabIconList.add(iconId);
            Bundle args = new Bundle();
            args.putInt(GoodsListFragment.ENROLL_STATUS, status);
            fragment.setArguments(args);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
