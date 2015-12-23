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
import android.widget.ImageView;
import android.widget.TextView;

import com.feifan.bp.R;
import com.feifan.bp.widget.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动报名详情
 * Created by Frank on 15/12/21.
 */
public class RegisterDetailActivity extends AppCompatActivity {

    public static final String EXTRA_KEY_ID = "id";

    private String activityId;

    private CollapsingToolbarLayout collapsingToolbar;
    private CustomViewPager viewPager;
    private TabLayout tabLayout;

    public static void startActivity(Context context, String activityId) {
        Intent i = new Intent(context, RegisterDetailActivity.class);
        i.putExtra(EXTRA_KEY_ID, activityId);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_detail);
        initViews();
        setupToolbar();
        initData();
    }


    private void initViews(){
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setTitleEnabled(false);
        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        setupViewPager(viewPager);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.register_detail));
        toolbar.setNavigationIcon(R.mipmap.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupViewPager(CustomViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),RegisterDetailActivity.this);

        adapter.addFrag(new ProductListFragment(), getString(R.string.notsubmitted),R.mipmap.icon_notsubmitted,"3");
        adapter.addFrag(new ProductListFragment(), getString(R.string.review),R.mipmap.icon_review,"0");
        adapter.addFrag(new ProductListFragment(), getString(R.string.approved),R.mipmap.icon_approved,"4");
        adapter.addFrag(new ProductListFragment(), getString(R.string.auditrefused),R.mipmap.icon_auditrefused,"0");

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

    private void initData() {
        activityId = getIntent().getStringExtra(EXTRA_KEY_ID);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        private Context context;

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private final List<Integer> mFragmentTabIconList = new ArrayList<>();
        private final List<String> mFragmentTabCountList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager,Context context) {
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
            if(mFragmentTabCountList.get(position).equals("0")){
                badgerView.setVisibility(View.GONE);
            }else{
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

        public void addFrag(Fragment fragment, String title,int iconId,String count) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
            mFragmentTabIconList.add(iconId);
            mFragmentTabCountList.add(count);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
