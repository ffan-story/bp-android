package com.feifan.bp.home.storeanalysis;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.internal.widget.ViewStubCompat;

import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.feifan.bp.Constants;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.PlatformState;
import com.feifan.bp.PlatformTabActivity;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.Statistics;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.network.UrlFactory;
import com.feifan.bp.util.TimeUtil;
import com.feifan.bp.widget.SegmentedGroup;
import com.feifan.material.datetimepicker.date.DatePickerDialog;
import com.feifan.statlib.FmsAgent;

import java.util.Calendar;

/**
 * Created by Frank on 15/12/2.
 */
public class visitorsAnalysisFragment extends ProgressFragment implements RadioGroup.OnCheckedChangeListener
        , MenuItem.OnMenuItemClickListener
        , DatePickerDialog.OnDateSetListener
        , PlatformTabActivity.onPageSelectListener{

    public static final String EXTRA_KEY_URL = "url";

    private SegmentedGroup segmentedGroup;
    private RadioButton rb_week, rb_month, rb_define;
    private WebView mWebView;

    private String mUrl;
    private String startDate;
    private String endDate;
    private Boolean mCheckFlag = false;
    private int tabIndex = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mUrl = getArguments().getString(EXTRA_KEY_URL);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //统计埋点 访客分析
            FmsAgent.onEvent(getActivity(), Statistics.FB_STOREANA_VISITORANA);
        }
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.fragment_visitors, null);
//        segmentedGroup = (SegmentedGroup) v.findViewById(R.id.segmentedGroup);
//        rb_week = (RadioButton) v.findViewById(R.id.week);
//        rb_month = (RadioButton) v.findViewById(R.id.month);
//        rb_define = (RadioButton) v.findViewById(R.id.define);
//        mWebView = (WebView) v.findViewById(R.id.browser_content);
//        initWeb(mWebView);
//        if (mUrl != null) {
//            mWebView.loadUrl(mUrl + "&days=7");
//        }
//        rb_week.setChecked(true);
//        tabIndex = R.id.week;
//        segmentedGroup.setOnCheckedChangeListener(this);
//        rb_define.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mCheckFlag) {
//                    initDialog();
//                }
//            }
//        });
//        return v;
//    }

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_visitors);
        View v = stub.inflate();
        segmentedGroup = (SegmentedGroup) v.findViewById(R.id.segmentedGroup);
        rb_week = (RadioButton) v.findViewById(R.id.week);
        rb_month = (RadioButton) v.findViewById(R.id.month);
        rb_define = (RadioButton) v.findViewById(R.id.define);
        mWebView = (WebView) v.findViewById(R.id.browser_content);
        initWeb(mWebView);

        rb_week.setChecked(true);
        tabIndex = R.id.week;
        segmentedGroup.setOnCheckedChangeListener(this);
        rb_define.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckFlag) {
                    initDialog();
                }
            }
        });
        ((PlatformTabActivity)getActivity()).setOnPageSelectListener(this);
        return v;
    }

    @Override
    protected void requestData() {
        if (mUrl != null) {
            setContentEmpty(false);
            mWebView.loadUrl(mUrl + "&days=7");
        }
    }


    @Override
    protected MenuInfo getMenuInfo() {
        return new MenuInfo(R.id.menu_analysis_help, Constants.NO_INTEGER, R.string.indicator_title);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intent = new Intent(getActivity(), PlatformTopbarActivity.class);
        intent.putExtra(OnFragmentInteractionListener.INTERATION_KEY_TO, SimpleBrowserFragment.class.getName());
        intent.putExtra(PlatformTopbarActivity.EXTRA_URL, UrlFactory.storeDescriptionForHtml());
        intent.putExtra(PlatformTopbarActivity.EXTRA_TITLE, getString(R.string.indicator_title));
        startActivity(intent);
        return false;
    }

    private void initWeb(WebView webView) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSavePassword(false);

        // 缓存相关
        if (PlatformState.getInstance().isCacheClearable()) {
            webView.clearCache(true);
            webView.clearHistory();
            webView.clearFormData();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
        }

        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setAppCachePath(getActivity().getCacheDir().getAbsolutePath());
        webView.setWebViewClient(new PlatformWebViewClient());
        webView.requestFocus();
    }

    @Override
    public void onPageSelected() {
        switch (tabIndex){
            case R.id.week:
                mWebView.loadUrl(mUrl + "&days=7");
                break;
            case R.id.month:
                mWebView.loadUrl(mUrl + "&days=30");
                break;
            case R.id.define:
                mWebView.loadUrl(mUrl + "&sdate=" + startDate + "&edate=" + endDate);
                break;
        }
    }

    private class PlatformWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
//            startWaiting();
            setContentShown(false);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            finishWaiting();
            setContentShown(true);
//            setContentEmpty(true);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            setContentEmpty(true);
        }
    }

    private void initDialog() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                visitorsAnalysisFragment.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setAccentColor(getResources().getColor(R.color.accent));
        dpd.setStartDateTitle(getString(R.string.date_start_text));
        dpd.setStopDateTitle(getString(R.string.date_end_text));
        dpd.setTabBackground(R.drawable.date_tab_selector);
        dpd.show(getFragmentManager(), "Datepickerdialog");
        dpd.setOnDateSetListener(this);
        dpd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mCheckFlag = true;
                setTabFocus(tabIndex);
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {

        String FromDate = year + "-" + DataFormat(monthOfYear + 1) + "-" + DataFormat(dayOfMonth);

        String ToDate = yearEnd + "-" + DataFormat(monthOfYearEnd + 1) + "-" + DataFormat(dayOfMonthEnd);

        if (!TimeUtil.compare_date(TimeUtil.getToday(), FromDate)) {
            Toast.makeText(getActivity(), getString(R.string.date_error_4), Toast.LENGTH_LONG).show();
        } else if (!TimeUtil.compare_date(TimeUtil.getToday(), ToDate)) {
            Toast.makeText(getActivity(), getString(R.string.date_error_5), Toast.LENGTH_LONG).show();
        } else if (TimeUtil.compare_date(FromDate, ToDate)) {
            Toast.makeText(getActivity(), getString(R.string.date_error_3), Toast.LENGTH_LONG).show();
        } else if (TimeUtil.getIntervalDays(FromDate, ToDate) < 3) {
            Toast.makeText(getActivity(), getString(R.string.date_error_6), Toast.LENGTH_LONG).show();
        } else if (TimeUtil.getIntervalDays(FromDate, ToDate) > 30) {
            Toast.makeText(getActivity(), getString(R.string.date_error_7), Toast.LENGTH_LONG).show();
        } else {
            tabIndex = R.id.define;
            setTabFocus(tabIndex);
            mCheckFlag = true;

            startDate = FromDate;
            endDate = ToDate;
            if (Utils.isNetworkAvailable(getActivity())) {
                mWebView.loadUrl("about:blank");
                mWebView.loadUrl(mUrl + "&sdate=" + startDate + "&edate=" + endDate);
            } else {
                setContentEmpty(true);
            }
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.week:
                if (Utils.isNetworkAvailable(getActivity())) {
                    mWebView.loadUrl("about:blank");
                    mWebView.loadUrl(mUrl + "&days=7");
                    mCheckFlag = false;
                    tabIndex = R.id.week;
                } else {
                    setContentEmpty(true);
                }
                break;
            case R.id.month:
                if (Utils.isNetworkAvailable(getActivity())) {
                    mWebView.loadUrl("about:blank");
                    mWebView.loadUrl(mUrl + "&days=30");
                    tabIndex = R.id.month;
                    mCheckFlag = false;
                } else {
                    setContentEmpty(true);
                }
                break;
            case R.id.define:
                initDialog();
                break;
        }
    }

    private void setTabFocus(int tabIndex) {
        switch (tabIndex) {
            case R.id.week:
                rb_week.setChecked(true);
                break;
            case R.id.month:
                rb_month.setChecked(true);
                break;
            case R.id.define:
                rb_define.setChecked(true);
                break;
        }
    }

    private String DataFormat(int data) {
        if (data < 10) {
            return "0" + data;
        } else {
            return String.valueOf(data);
        }
    }
}
