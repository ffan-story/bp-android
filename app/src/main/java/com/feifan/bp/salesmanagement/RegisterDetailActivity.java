package com.feifan.bp.salesmanagement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.volley.Response;
import com.feifan.bp.Constants;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.widget.CustomViewPager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 活动报名详情
 * Created by Frank on 15/12/21.
 */
public class RegisterDetailActivity extends AppCompatActivity implements View.OnClickListener, GoodsNoCommitFragment.UpdateStatusListener {

    public static final String EXTRA_KEY_ID = "id";
    public static final String EXTRA_KEY_TITLE = "title";
    public static final String EXTRA_KEY_FLAG = "isCutOff";
    public static final String EXTRA_KEY_INDEX = "index";

    public String promotionId;
    private String promotionName;
    public Boolean isCutOff;

    private CollapsingToolbarLayout collapsingToolbar;
    private CustomViewPager viewPager;
    private TabLayout tabLayout;
    private RelativeLayout rlEventTitle;
    private View registerDetailHeader;
    private Button btnAddProduct;
    private TextView tvEnrollHeader;
    private List<String> mFragmentTabCountList = new ArrayList<>();
    private List<String> mFragmentTitleList = new ArrayList<>();
    private List<Integer> mFragmentTabIconList = new ArrayList<>();
    private GoodsNoCommitFragment goodsNoCommitFragment;
    private ViewPagerAdapter adapter;
    private int CurrentItemIndex = 0;

    public static void startActivity(Context context, String id, String title, boolean isCutOff) {
        Intent i = new Intent(context, RegisterDetailActivity.class);
        i.putExtra(EXTRA_KEY_ID, id);
        i.putExtra(EXTRA_KEY_TITLE, title);
        i.putExtra(EXTRA_KEY_FLAG, isCutOff);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_detail);
        promotionId = getIntent().getStringExtra(EXTRA_KEY_ID);
        promotionName = getIntent().getStringExtra(EXTRA_KEY_TITLE);
        isCutOff = getIntent().getBooleanExtra(EXTRA_KEY_FLAG, false);
        initViews();
        setupToolbar();
        getGoodsStatus(true);
    }

    private void initViews() {
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setTitleEnabled(false);
        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        rlEventTitle = (RelativeLayout) findViewById(R.id.rl_event_title);
        btnAddProduct = (Button) findViewById(R.id.btn_add_product);
        tvEnrollHeader = (TextView) findViewById(R.id.tv_enroll_detail_header);
        registerDetailHeader = findViewById(R.id.register_detail_header);
        tvEnrollHeader.setText(promotionName);
        registerDetailHeader.setOnClickListener(this);
        rlEventTitle.setOnClickListener(this);
        btnAddProduct.setOnClickListener(this);
        if (isCutOff) {
            btnAddProduct.setVisibility(View.GONE);
        }
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

    private void getGoodsStatus(final boolean isSetupViewPager) {
        mFragmentTabCountList.clear();

        String storeId = UserProfile.getInstance().getAuthRangeId();
        final String merchantId = UserProfile.getInstance().getMerchantId();
        String promotionCode = promotionId;

        Response.Listener<GoodsStatusModel> listener = new Response.Listener<GoodsStatusModel>() {
            @Override
            public void onResponse(GoodsStatusModel model) {

                if (model.mGoodsStatus != null && model.mGoodsStatus.size() != 0) {
                    for (int i = 0; i < model.mGoodsStatus.size(); i++) {
                        Iterator it = model.mGoodsStatus.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry entry = (Map.Entry) it.next();
                            String key = entry.getKey().toString();
                            if (key.equals(String.valueOf(i))) {
                                mFragmentTabCountList.add(entry.getValue().toString());
                            }
                        }
                    }
                    if (isSetupViewPager) {
                        setupViewPager(viewPager);
                    } else {
                        for (int i = 0; i < tabLayout.getTabCount(); i++) {
                            TabLayout.Tab tab = tabLayout.getTabAt(i);
                            if (tab != null) {
                                final View customView = tab.getCustomView();
                                TextView badgerView = (TextView) customView.findViewById(R.id.badger);
                                if (mFragmentTabCountList.get(i).equals("0")) {
                                    badgerView.setVisibility(View.GONE);
                                } else {
                                    badgerView.setVisibility(View.VISIBLE);
                                    badgerView.setText(mFragmentTabCountList.get(i));
                                }
                            }
                        }
                    }
                }
            }
        };
        PromotionCtrl.getGoodsStatus(storeId, merchantId, promotionCode, listener);
    }


    private void setupViewPager(CustomViewPager viewPager) {

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), RegisterDetailActivity.this);
        if (isCutOff) {
            adapter.addFrag(new GoodsListFragment(), getString(R.string.notsubmitted), R.mipmap.icon_notsubmitted,
                    GoodsListFragment.STATUS_NO_COMMIT, mFragmentTabCountList.get(0));
        } else {
            goodsNoCommitFragment = new GoodsNoCommitFragment();
            goodsNoCommitFragment.setUpdateListener(this);
            adapter.addFrag(goodsNoCommitFragment, getString(R.string.notsubmitted), R.mipmap.icon_notsubmitted,
                    GoodsListFragment.STATUS_NO_COMMIT, mFragmentTabCountList.get(0));
        }
        adapter.addFrag(new GoodsListFragment(), getString(R.string.review), R.mipmap.icon_review,
                GoodsListFragment.STATUS_AUDIT, mFragmentTabCountList.get(1));
        adapter.addFrag(new GoodsListFragment(), getString(R.string.approved), R.mipmap.icon_approved,
                GoodsListFragment.STATUS_AUDIT_PASS, mFragmentTabCountList.get(2));
        adapter.addFrag(new GoodsListFragment(), getString(R.string.auditrefused), R.mipmap.icon_auditrefused,
                GoodsListFragment.STATUS_AUDIT_DENY, mFragmentTabCountList.get(3));

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(CurrentItemIndex);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(adapter.getTabView(i));
            }
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getGoodsStatus(false);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_detail_header:
                Bundle args1 = new Bundle();
                args1.putString(EventDetailFragment.EXTRA_KEY_ID, promotionId);
                args1.putString(EventDetailFragment.EXTRA_KEY_NAME, promotionName);
                args1.putBoolean(EventDetailFragment.EXTRA_KEY_FLAG, false);
                PlatformTopbarActivity.startActivity(RegisterDetailActivity.this, EventDetailFragment.class.getName(), getString(R.string.promotionDetail), args1);
                break;
            case R.id.btn_add_product:
                Bundle args = new Bundle();
                args.putString(InstEventGoodsListFragment.EXTRA_PARTAKE_EVENT_ID, promotionId);
                PlatformTopbarActivity.startActivityForResult(this, InstEventGoodsListFragment.class.getName(), getString(R.string.instant_goods_list), args);
                break;
        }
    }

    /**
     * 提交或删除商品后刷新界面
     */
    @Override
    public void updateStatus() {
        CurrentItemIndex = 0;
        getGoodsStatus(true);//刷新页面
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data!=null) {
            CurrentItemIndex = data.getIntExtra(Constants.RETURN_STATUS,0);
            getGoodsStatus(true);
        }
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        private Context context;

        private List<Fragment> mFragmentList = new ArrayList<>();
        private List<String> mCountList = new ArrayList<>();

        public View getTabView(int position) {
            View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
            TextView tv = (TextView) v.findViewById(R.id.textView);
            tv.setText(mFragmentTitleList.get(position));
            ImageView img = (ImageView) v.findViewById(R.id.imageView);
            img.setImageResource(mFragmentTabIconList.get(position));
            TextView badgerView = (TextView) v.findViewById(R.id.badger);
            if (mCountList.get(position).equals("0")) {
                badgerView.setVisibility(View.GONE);
            } else {
                badgerView.setVisibility(View.VISIBLE);
                badgerView.setText(mCountList.get(position));
            }
            return v;
        }

        public ViewPagerAdapter(FragmentManager manager, Context context) {
            super(manager);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title, int iconId, int status, String count) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
            mFragmentTabIconList.add(iconId);
            mCountList.add(count);
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
