package com.feifan.bp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.feifan.bp.base.PlatformBaseActivity;
import com.feifan.bp.browser.BrowserFragment;
import com.feifan.bp.widget.WebViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目通用带有Topbar和Tab的活动
 * <pre>
 *     目前不支持BrowserFragment
 * </pre>
 * <p/>
 * Created by xuchunlei on 15/11/19.
 */
public class PlatformTabActivity extends PlatformBaseActivity implements
        OnFragmentInteractionListener,
        BrowserFragment.OnBrowserListener {

    /**
     * 参数键值－fragments
     */
    public static final String EXTRA_KEY_FRAGMENTS = "fragments";
    /**
     * 参数键值－title
     */
    public static final String EXTRA_KEY_TITLE = "title";

    /**
     * 固定模式时最大显示的Tab数，超过这个数字后使用滚动模式
     */
    private static final int MAX_TAB_COUNT = 4;

    // view
    private Toolbar mToolbar;
    private TextView mCenterTitle;

    /**
     * 构建意图
     *
     * @param context
     * @param title
     * @param fragments
     * <p>
     * 构建意图，使用{@link ArgsBuilder}构造fragments参数
     * <pre>
     * {@code
     * Intent intent = PlatformTabActivity.buildIntent(getContext(), "测试中心", fragments);
     * startActivity(intent);
     * }
     * </p>
     * @return
     */
    public static Intent buildIntent(Context context, String title, Bundle fragments) {
        Intent intent = new Intent(context.getApplicationContext(), PlatformTabActivity.class);
        if (fragments != null) {
            intent.putExtra(EXTRA_KEY_FRAGMENTS, fragments);
        }
        intent.putExtra(EXTRA_KEY_TITLE, title);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        Bundle args = getIntent().getBundleExtra(EXTRA_KEY_FRAGMENTS);

        // 初始化标题栏
        mToolbar = (Toolbar) findViewById(R.id.tab_header);
        mCenterTitle = (TextView) mToolbar.findViewById(R.id.header_center_title);
        mCenterTitle.setText(getIntent().getStringExtra(EXTRA_KEY_TITLE));
        setSupportActionBar(mToolbar);//作为ActionBar使用，支持加载Menu
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initHeader(mToolbar);

        // 初始化Tab
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_bar);
        WebViewPager pager = (WebViewPager) findViewById(R.id.tab_pager);
        pager.setAdapter(createAdapter(args));
        tabLayout.setTabMode(pager.getAdapter().getCount() > MAX_TAB_COUNT ? TabLayout.MODE_SCROLLABLE : TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(pager);
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

    // 创建界面适配器
    private FragmentPagerAdapter createAdapter(Bundle args) {
        if (args != null) {
            final List<Fragment> fragments = new ArrayList<Fragment>();

            ArrayList<String> orderList = args.getStringArrayList(ArgsBuilder.EXTRA_KEY_ORDER_LIST);
            for(String key : orderList) {
                fragments.add(Fragment.instantiate(this, key, args.getBundle(key)));
            }

            return new FragmentPagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    return fragments.get(position);
                }

                @Override
                public int getCount() {
                    return fragments.size();
                }

                @Override
                public CharSequence getPageTitle(int position) {
                    return fragments.get(position).getArguments().getString(ArgsBuilder.EXTRA_KEY_TITLE);
                }
            };
        }
        return null;
    }

    @Override
    public void onFragmentInteraction(Bundle args) {

    }

    @Override
    public void onTitleChanged(String title) {
        mCenterTitle.setText(title);
    }

    @Override
    public void onStatusChanged(boolean flag) {

    }

    public void OnTitleReceived(String title) {
    }

    @Override
    public void OnErrorReceived(String msg, WebView web, String url) {
    }

    /**
     * 参数构建器，用来传递初始化需要的fragments及其参数，调用方式如下：
     * <p/>
     * <pre>
     * {@code
     *  Bundle fragmentArgs = new PlatformTabActivity.ArgsBuilder()
     *                        .addFragment(SettingsFragment.class.getName(), "设置")
     *                        .addArgument(SettingsFragment.class.getName(), "count", 1)
     *                        .addFragment(MessageFragment.class.getName(), "消息 ")
     *                        .addArgument(MessageFragment.class.getName(), "name", "消息列表")
     *                        .build();
     * }
     * </pre>
     */
    public static class ArgsBuilder {

        private static final String EXTRA_KEY_TITLE = "title";

        // 用于记录顺序
        private static final String EXTRA_KEY_ORDER_LIST = "order";
        private Bundle mArgs = new Bundle();

        public ArgsBuilder() {
            mArgs.putStringArrayList(EXTRA_KEY_ORDER_LIST, new ArrayList<String>());
        }

        public ArgsBuilder addFragment(String className, String title) {
            Bundle fArgs = new Bundle();
            fArgs.putString(EXTRA_KEY_TITLE, title);
            mArgs.putBundle(className, fArgs);
            mArgs.getStringArrayList(EXTRA_KEY_ORDER_LIST).add(className);
            return this;
        }

        /**
         * 增加参数q
         * <pre>
         *     目前支持Integer和String类型的参数
         * </pre>
         *
         * @param className
         * @param key
         * @param value
         * @return
         */
        public ArgsBuilder addArgument(String className, String key, Object value) {
            Bundle fArgs = mArgs.getBundle(className);
            if (fArgs == null) {
                throw new IllegalArgumentException("You should add " + className + " via addFragment method first!");
            } else {
                if (value instanceof Integer) {
                    fArgs.putInt(key, (Integer) value);
                } else if (value instanceof String) {
                    fArgs.putString(key, String.valueOf(value));
                }
            }
            return this;
        }

        public Bundle build() {
            return mArgs;
        }
    }
}
